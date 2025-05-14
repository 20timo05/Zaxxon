package thd.game.utilities;

/**
 * A service class responsible for preprocessing the raw wall description string
 * into a character-based representation used for wall building.
 */
class WallPreprocessingService { // Package-private access

    private static final int MAX_NUM_ROWS = 9;

    /**
     * Private constructor to prevent instantiation.
     */
    private WallPreprocessingService() {
        // This constructor is intentionally private and empty.
    }

    /**
     * Preprocesses the wall description so that it calculates which blocks go where.
     * Mapping:
     * 0: Hole
     * 1: {@link WallBlockImages#FULL_BLOCK_FRONT}
     * 2: {@link WallBlockImages#HALF_BLOCK_FRONT}
     * 3: {@link WallBlockImages#FULL_BLOCK_TOP}
     * 4: {@link WallBlockImages#HALF_BLOCK_TOP}
     * 5: {@link WallBlockImages#BLOCK_SIDE}
     * 6: {@link WallBlockImages#BLOCK_SIDE_FULL_TOP}
     * 7: {@link WallBlockImages#BLOCK_SIDE_HALF_TOP}
     * 8: {@link WallBlockImages#BLOCK_SIDE_HALF}
     * <p>
     * ===== Optimization =====
     * A: 2x {@link WallBlockImages#FULL_BLOCK_FRONT}
     * B: 4x {@link WallBlockImages#FULL_BLOCK_FRONT}
     * C: 8x {@link WallBlockImages#FULL_BLOCK_FRONT}
     * D: 16x {@link WallBlockImages#FULL_BLOCK_FRONT}
     *
     * @param wallDescription a BlockGraphic-like String with 'x' where a block should be
     * @return a new preprocessed wall description
     */
    static String preprocessWallDescription(String wallDescription) { // Package-private access
        String trimmedWallDescription = trimLinebreaks(wallDescription);
        int[] wallDescriptionDimensions = WallBlockGraphicUtils.calcBlockImageDimension(trimmedWallDescription);

        if (wallDescriptionDimensions[0] > MAX_NUM_ROWS) {
            throw new IllegalArgumentException("Walls should have a max height of " + MAX_NUM_ROWS);
        }

        trimmedWallDescription = trimmedWallDescription.replaceAll(" ", "0"); // convert spaces to 0
        // add a new row above and column to the right
        String paddedWallDescription = "0".repeat(wallDescriptionDimensions[1] + 1) + "\n"
                + trimmedWallDescription.replaceAll("\n", "0\n") + "0";
        String[] paddedWallDescriptionRows = paddedWallDescription.split("\n");

        paddedWallDescriptionRows = fillSpaces(paddedWallDescriptionRows, wallDescriptionDimensions[1] + 1);
        paddedWallDescriptionRows = findFrontBlocks(paddedWallDescriptionRows);
        paddedWallDescriptionRows = findTopBlocks(paddedWallDescriptionRows);
        paddedWallDescriptionRows = findSideBlocks(paddedWallDescriptionRows);
        paddedWallDescriptionRows = findOptimization(paddedWallDescriptionRows);

        return String.join("\n", paddedWallDescriptionRows);
    }

    // Helper methods for preprocessing

    private static String[] fillSpaces(String[] paddedWallDescriptionRows, int width) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 0; y < paddedWallDescriptionRows.length; y++) {
            int currentLength = paddedWallDescriptionRows[y].length();
            if (currentLength < width) {
                String spacesPadding = " ".repeat(width - currentLength);
                newWallDescriptionRows[y] = paddedWallDescriptionRows[y] + spacesPadding;
            } else {
                newWallDescriptionRows[y] = paddedWallDescriptionRows[y];
            }
        }

        return newWallDescriptionRows;
    }

    /**
     * Trims off '\n' characters from the start and end of the wall description.
     *
     * @param wallDescription the wall description string
     * @return the trimmed wall description string
     */
    static String trimLinebreaks(String wallDescription) {
        int startIdx = 0;
        int endIdx = wallDescription.length();

        while (startIdx < endIdx && wallDescription.charAt(startIdx) == '\n') {
            startIdx++;
        }

        while (endIdx > startIdx && wallDescription.charAt(endIdx - 1) == '\n') {
            endIdx--;
        }

        return wallDescription.substring(startIdx, endIdx);
    }

    private static String[] findFrontBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 0; y < paddedWallDescriptionRows.length; y++) {

            String row = paddedWallDescriptionRows[y];
            StringBuilder updatedRow = new StringBuilder(row);

            int x = 0;
            while (x < row.length()) {
                if (row.charAt(x) == '0') {
                    x += 1;
                    continue;
                }

                // Determine if the next block *should* be a full block based on position
                boolean shouldBeFullBlock = (y % 2 == 1 && x % 2 == 0) || (y % 2 == 0 && x % 2 == 1);

                // Check if there's a block character to the right that makes it a full block
                boolean canBeFullBlock = (x + 1 < row.length() && row.charAt(x + 1) != '0');

                if (shouldBeFullBlock && canBeFullBlock) {
                    updatedRow.setCharAt(x, '1');
                    updatedRow.setCharAt(x + 1, '1');
                    x += 2;
                } else {
                    updatedRow.setCharAt(x, '2');
                    x++;
                }
            }

            newWallDescriptionRows[y] = updatedRow.toString();
        }

        return newWallDescriptionRows;
    }


    private static String[] findTopBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 1; y < paddedWallDescriptionRows.length; y++) {

            String aboveRow = paddedWallDescriptionRows[y - 1];
            String currentRow = paddedWallDescriptionRows[y];
            StringBuilder updatedAboveRow = new StringBuilder(aboveRow);

            for (int x = 0; x < currentRow.length(); x++) {
                if (aboveRow.charAt(x) != '0') {
                    continue;
                }

                char currentChar = currentRow.charAt(x);
                if (currentChar == '1') {
                    if (x + 1 < aboveRow.length() && aboveRow.charAt(x + 1) == '0') {
                        updatedAboveRow.setCharAt(x, '3');
                    } else {
                        updatedAboveRow.setCharAt(x, '4');
                    }
                } else if (currentChar == '2') {
                    updatedAboveRow.setCharAt(x, '4');
                }
            }

            newWallDescriptionRows[y - 1] = updatedAboveRow.toString();
        }
        // Copy the last row as is
        newWallDescriptionRows[newWallDescriptionRows.length - 1] =
                paddedWallDescriptionRows[paddedWallDescriptionRows.length - 1];

        return newWallDescriptionRows;
    }


    private static String[] findSideBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 0; y < paddedWallDescriptionRows.length; y++) {

            String row = paddedWallDescriptionRows[y];
            StringBuilder updatedRow = new StringBuilder(row);

            for (int x = 1; x < row.length() - 1; x++) {

                char charToLeft = row.charAt(x - 1);
                char currentChar = row.charAt(x);
                char charAbove = (y > 0) ? paddedWallDescriptionRows[y - 1].charAt(x) : '0';

                if (charToLeft == '1' || charToLeft == '2') {

                    if (currentChar == '0') {
                        if (charAbove == '1' || charAbove == '2') {
                            updatedRow.setCharAt(x, '8');
                        } else {
                            updatedRow.setCharAt(x, '5');
                        }
                    } else if (currentChar == '3') {
                        updatedRow.setCharAt(x, '6');
                        if (x + 1 < updatedRow.length()) {
                            updatedRow.setCharAt(x + 1, '6');
                        }
                    } else if (currentChar == '4') {
                        updatedRow.setCharAt(x, '7');
                    }
                }
            }
            newWallDescriptionRows[y] = updatedRow.toString();
        }

        return newWallDescriptionRows;
    }


    private static String[] findOptimization(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 0; y < paddedWallDescriptionRows.length; y++) {
            String row = paddedWallDescriptionRows[y];
            StringBuilder optimizedRow = new StringBuilder(row);
            int x = 0;

            while (x < optimizedRow.length()) {
                if (optimizedRow.charAt(x) == '1') {
                    int start = x;
                    while (x < optimizedRow.length() && optimizedRow.charAt(x) == '1') {
                        x++;
                    }
                    // optimization: replace sequences of FULL_BLOCK_FRONT with wallRowOptimization[i]
                    // Sequences of '1' have a minimum length of 2 (representing one full block)
                    int sequenceLength = x - start;
                    while (sequenceLength >= 4) { // Optimization applies to sequences of 4 or more '1's
                        // Calculate the largest power of 2 * 2 that fits in the remaining sequence
                        // 2 because each '1' represents half a block width in the description
                        int numBlocks = sequenceLength / 2; // Number of conceptual blocks
                        int log = (int) (Math.log(numBlocks) / Math.log(2));
                        log = Math.min(4, log); // Cap at index 4 ('D')

                        char optimizationChar = new char[]{'1', 'A', 'B', 'C', 'D'}[log];
                        int numOptimizationBlocks = (int) Math.pow(2, log);
                        int endIdx = start + numOptimizationBlocks * 2; // Index in the char description

                        // Replace the '1's with the optimization character
                        for (int i = start; i < endIdx; i++) {
                            if (i < optimizedRow.length()) { // Ensure index is within bounds
                                optimizedRow.setCharAt(i, optimizationChar);
                            }
                        }

                        // Move the start for the next potential optimization within the same sequence
                        start = endIdx;
                        sequenceLength = x - start; // Recalculate remaining length
                    }
                } else {
                    x++;
                }
            }

            newWallDescriptionRows[y] = optimizedRow.toString();
        }

        return newWallDescriptionRows;
    }
}
