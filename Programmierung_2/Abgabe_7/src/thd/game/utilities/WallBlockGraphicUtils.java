package thd.game.utilities;


import java.util.ArrayList;

import static thd.game.utilities.WallBlockImages.FULL_BLOCK_FRONT;

/**
 * This is a utility class used to precompute any possible Wall dynamically.
 * That includes both the BlockGraphic String and the Hitboxes the for Wall.
 * <p>
 * Since the calculation takes a few seconds, the result will have to be stored as a Level File and loaded instead of computed.
 * <p>
 * For an example, see the commented out main function which creates the BlockGraphic for {@link WallBlockImages#SAMPLE_WALL}.
 */
public class WallBlockGraphicUtils {
    private static final int MAX_NUM_ROWS = 9;
    private static final int NUM_BLOCKS_PER_ROW = 36;


    private final String[] wallRowOptimization;

    /*
    public static void main(String[] args) {
        WallBlockGraphicUtils utils = new WallBlockGraphicUtils();
        System.out.println(utils.preprocessWallDescription(SAMPLE_WALL));
        System.out.println(utils.generateWallBlockImage(SAMPLE_WALL));
    } */

    /**
     * precomputes a block image for bigger parts of one Wall.
     * => optimization O(n) => O(log(n))
     */
    public WallBlockGraphicUtils() {
        wallRowOptimization = new String[5];
        wallRowOptimization[0] = FULL_BLOCK_FRONT;

        for (int i = 1; i < wallRowOptimization.length; i++) {
            wallRowOptimization[i] = combineBlockImages(
                    wallRowOptimization[i - 1],
                    wallRowOptimization[i - 1],
                    (int) (WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_X * Math.pow(2, i - 1)),
                    (int) (WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_Y * Math.pow(2, i - 1))
            );
        }
    }

    /**
     * Generates the rows a complete Wall BlockGraphic String dynamically.
     * These can be used as seperate {@code GameObject}s, so that collisions can be correctly calculated based on altitude level.
     *
     * @param wallDescription a BlockGraphic like String with 'x' where a block should be
     * @return the block Image and hitboxes for each row
     */
    public DynamicWall generateDynamicWall(String wallDescription) {
        // takes ~0.01s => insignificant
        String preprocessedWallDescription = preprocessWallDescription(wallDescription);

        String[] wallDescriptionRows = preprocessedWallDescription.split("\n");

        String[] wallBlockImageInRows = new String[wallDescriptionRows.length];
        for (int y = 0; y < wallDescriptionRows.length; y++) {
            wallBlockImageInRows[y] = generateWallRowBlockImage(wallDescriptionRows[y]);
        }

        ArrayList<ArrayList<int[]>> hitboxIndicesInRows = calcHitboxIndicesInRows(preprocessedWallDescription);

        return new DynamicWall(wallBlockImageInRows, hitboxIndicesInRows);
    }

    /**
     * Generates a complete BlockGraphic String for the Wall dynamically.
     *
     * @param wallDescription a BlockGraphic like String with 'x' where a block should be
     * @return the block Image
     */
    public String generateWallBlockImage(String wallDescription) {
        DynamicWall dynamicWall = generateDynamicWall(wallDescription);
        String[] wallBlockImageInRows = dynamicWall.wallBlockImageInRows;

        if (wallBlockImageInRows == null || wallBlockImageInRows.length == 0) {
            return ""; // Or handle the empty case appropriately
        }

        String combinedBlockImage = wallBlockImageInRows[0];
        for (int y = 1; y < wallBlockImageInRows.length; y++) {
            combinedBlockImage = combineBlockImages(
                    combinedBlockImage,
                    wallBlockImageInRows[y],
                    0,
                    WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_Y * y
            );
        }

        return combinedBlockImage;
    }

    /**
     * Mirrors each row of the 2d BlockImage representation horizontally.
     *
     * @param blockImage the block Image String to mirror
     * @return the horizontally mirrored new BlockImage
     */
    public String mirrorBlockImage(String blockImage) {
        if (blockImage == null || blockImage.isEmpty()) {
            return blockImage;
        }

        String[] rows = blockImage.split("\n");
        StringBuilder mirroredBlockImage = new StringBuilder();

        for (int y = 0; y < rows.length; y++) {
            StringBuilder mirroredRow = new StringBuilder(rows[y]).reverse();
            mirroredBlockImage.append(mirroredRow.toString());
            if (y < rows.length - 1) {
                mirroredBlockImage.append("\n");
            }
        }

        return mirroredBlockImage.toString();
    }

    private String generateWallRowBlockImage(String wallDescriptionRow) {
        String combinedRowBlockImage = "";

        int offsetX = 0;
        int offsetY = 0;

        int x = 0;
        while (x < wallDescriptionRow.length()) {

            WallBlockDimensionCalculator blockDim = new WallBlockDimensionCalculator(wallDescriptionRow.charAt(x), wallRowOptimization);

            if (blockDim.blockImage != null) {
                combinedRowBlockImage = combineBlockImages(
                        combinedRowBlockImage,
                        blockDim.blockImage,
                        offsetX + blockDim.additionalOffsetX,
                        offsetY + blockDim.additionalOffsetY
                );
            }
            offsetX += blockDim.increaseOffsetX;
            offsetY += blockDim.increaseOffsetY;
            x += blockDim.charWidth;
        }

        return combinedRowBlockImage;
    }

    /**
     * Combines two Block Images together.
     * If they overlap, the pixels from the second one are used.
     *
     * @param firstBlockImage  first Block Image string
     * @param secondBlockImage second Block Image string
     * @param offsetX          horizontal offset of second Block Image
     * @param offsetY          vertical offset of second Block Image
     * @return a new combined BlockImage
     */
    private String combineBlockImages(String firstBlockImage, String secondBlockImage, int offsetX, int offsetY) {
        int[] firstBlockImageDimensions = calcBlockImageDimension(firstBlockImage);
        int[] secondBlockImageDimensions = calcBlockImageDimension(secondBlockImage);

        // calculate if the offset of the second image has "moved" the first image relative to the top left corner
        int rowsAddedToTop = offsetY < 0 ? -offsetY : 0;
        int columnsAddedToLeft = offsetX < 0 ? -offsetX : 0;

        int combinedHeight = Math.max(firstBlockImageDimensions[0], secondBlockImageDimensions[0] + offsetY) + rowsAddedToTop;
        int combinedWidth = Math.max(firstBlockImageDimensions[1], secondBlockImageDimensions[1] + offsetX) + columnsAddedToLeft;

        char[][] combinedBlockImage = new char[combinedHeight][combinedWidth];

        String[] firstBlockImageInRows = firstBlockImage.split("\n");
        // ===== draw the first image on the combined Block Image =====
        for (int y = 0; y < firstBlockImageDimensions[0]; y++) {
            for (int x = 0; x < firstBlockImageDimensions[1]; x++) {
                int xPosOnCombined = x + columnsAddedToLeft;
                int yPosOnCombined = y + rowsAddedToTop;

                if (y < firstBlockImageInRows.length && x < firstBlockImageInRows[y].length()) {
                    combinedBlockImage[yPosOnCombined][xPosOnCombined] = firstBlockImageInRows[y].charAt(x);
                } else {
                    combinedBlockImage[yPosOnCombined][xPosOnCombined] = ' ';
                }
            }
        }

        // ===== draw the second image on the combined Block Image =====
        String[] secondBlockImageInRows = secondBlockImage.split("\n");
        for (int y = 0; y < secondBlockImageDimensions[0]; y++) {
            for (int x = 0; x < secondBlockImageDimensions[1]; x++) {
                int xPosOnCombined = x + Math.max(offsetX, 0);
                int yPosOnCombined = y + Math.max(offsetY, 0);

                if (y < secondBlockImageInRows.length && x < secondBlockImageInRows[y].length()) {
                    char charFromSecondBlockImage = secondBlockImageInRows[y].charAt(x);
                    if (charFromSecondBlockImage != ' ') {
                        combinedBlockImage[yPosOnCombined][xPosOnCombined] = charFromSecondBlockImage;
                    }
                }
            }
        }

        return combineCharArray(combinedBlockImage);
    }

    /**
     * Given a Multiline String, it calculates the width (longest line) and height (number of lines).
     *
     * @param blockImage the String to calculate the dimensions of
     * @return an array with new int[]{height, width}
     */
    public int[] calcBlockImageDimension(String blockImage) {
        String[] blockImageRows = blockImage.split("\n");

        int height = blockImageRows.length;
        int width = blockImageRows[0].length();

        for (String row : blockImageRows) {
            if (row.length() > width) {
                width = row.length();
            }
        }

        return new int[]{height, width};
    }

    private String combineCharArray(char[][] combinedBlockImage) {
        StringBuilder result = new StringBuilder();

        for (int y = 0; y < combinedBlockImage.length; y++) {
            result.append(combinedBlockImage[y]);
            if (y < combinedBlockImage.length - 1) {
                result.append('\n');
            }
        }

        // uninitialized values in a char array have the value '\u0000', not a space
        return result.toString().replace('\u0000', ' ');
    }

    /**
     * Preprocesses the {@code wallDescription} so that it calculates which blocks go where.
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
     * @param wallDescription a BlockGraphic like String with 'x' where a block should be
     * @return a new {@code wallDescription}
     */
    private String preprocessWallDescription(String wallDescription) {
        String trimmedWallDescription = trimLinebreaks(wallDescription);
        int[] wallDescriptionDimensions = calcBlockImageDimension(trimmedWallDescription);

        if (
                wallDescriptionDimensions[0] % 2 != 1
                        || wallDescriptionDimensions[0] > MAX_NUM_ROWS
        ) {
            throw new IllegalArgumentException("Walls should have an odd height (1, 3, 5, 7, 9)");
        }

        trimmedWallDescription = trimmedWallDescription.replaceAll(" ", "0"); // convert spaces to 0
        // add a new row above and column to the right
        String paddedWallDescription = "0".repeat(wallDescriptionDimensions[1]) + "\n" + trimmedWallDescription;
        paddedWallDescription = paddedWallDescription.replaceAll("\n", "0\n") + "0";
        String[] paddedWallDescriptionRows = paddedWallDescription.split("\n");

        paddedWallDescriptionRows = findFrontBlocks(paddedWallDescriptionRows);
        paddedWallDescriptionRows = findTopBlocks(paddedWallDescriptionRows);
        paddedWallDescriptionRows = findSideBlocks(paddedWallDescriptionRows);
        paddedWallDescriptionRows = findOptimization(paddedWallDescriptionRows);

        return String.join("\n", paddedWallDescriptionRows);
    }

    // ================================================================================================================
    // ======================================= PREPROCESSING OF WALLDESCRIPTION =======================================
    // ================================================================================================================

    /**
     * this method trims of '\n' from start and end of wallDescription String
     *
     * @param wallDescription string describing a wall
     * @return the trimmed wallDescription string
     * @see WallBlockImages#SAMPLE_WALL
     */
    private String trimLinebreaks(String wallDescription) {
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

    private String[] findFrontBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 0; y < paddedWallDescriptionRows.length; y++) {

            String row = paddedWallDescriptionRows[y];
            boolean isNextBlockAFullBlock = y % 2 == 1;

            int x = 0;
            while (x < paddedWallDescriptionRows[y].length()) {
                if (paddedWallDescriptionRows[y].charAt(x) == '0') {
                    x += 1;
                    continue;
                }

                if (isNextBlockAFullBlock) {
                    isNextBlockAFullBlock = x + 1 <= row.length() - 1 && row.charAt(x + 1) != '0';

                    // check if it has to start with half block again after gap
                    if (x > 0 && paddedWallDescriptionRows[y].charAt(x - 1) == '0') {
                        if (y % 2 == 0 && x % 2 == 0 || y % 2 == 1 && x % 2 == 1) {
                            isNextBlockAFullBlock = false;
                        }
                    }
                }

                if (isNextBlockAFullBlock) {
                    row = replaceChar(row, x, '1');
                    row = replaceChar(row, x + 1, '1');
                    x += 2;
                } else {
                    row = replaceChar(row, x, '2');
                    x++;
                }

                isNextBlockAFullBlock = true;
            }

            newWallDescriptionRows[y] = row;
        }

        return newWallDescriptionRows;
    }

    private String[] findTopBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 1; y < paddedWallDescriptionRows.length; y++) {

            String aboveRow = paddedWallDescriptionRows[y - 1];
            for (int x = 0; x < paddedWallDescriptionRows[y].length(); x++) {
                if (aboveRow.charAt(x) != '0') {
                    continue;
                }

                if (paddedWallDescriptionRows[y].charAt(x) == '1') {
                    if (x + 1 < aboveRow.length() && aboveRow.charAt(x + 1) == '0') {
                        aboveRow = replaceChar(aboveRow, x, '3');
                    } else {
                        aboveRow = replaceChar(aboveRow, x, '4');
                    }
                }
                if (paddedWallDescriptionRows[y].charAt(x) == '2') {
                    aboveRow = replaceChar(aboveRow, x, '4');
                }
            }

            newWallDescriptionRows[y - 1] = aboveRow;
        }
        newWallDescriptionRows[newWallDescriptionRows.length - 1] = paddedWallDescriptionRows[paddedWallDescriptionRows.length - 1];

        return newWallDescriptionRows;
    }

    private String[] findSideBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 0; y < paddedWallDescriptionRows.length; y++) {

            String row = paddedWallDescriptionRows[y];
            for (int x = 1; x < row.length() - 1; x++) {

                if (row.charAt(x - 1) == '1' || row.charAt(x - 1) == '2') {

                    if (row.charAt(x) == '0') {

                        if (y > 0 && (paddedWallDescriptionRows[y - 1].charAt(x) == '1' || paddedWallDescriptionRows[y - 1].charAt(x) == '2')) {
                            row = replaceChar(row, x, '8');

                        } else {
                            row = replaceChar(row, x, '5');
                        }

                    } else if (row.charAt(x) == '3') {
                        row = replaceChar(row, x, '6');
                        row = replaceChar(row, x + 1, '6');

                    } else if (row.charAt(x) == '4') {
                        row = replaceChar(row, x, '7');

                    }
                }
            }

            newWallDescriptionRows[y] = row;
        }

        return newWallDescriptionRows;
    }

    private String[] findOptimization(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 0; y < paddedWallDescriptionRows.length; y++) {
            String row = paddedWallDescriptionRows[y];
            int x = 0;

            while (x < row.length()) {
                if (row.charAt(x) == '1') {
                    int start = x;
                    while (x < row.length() && row.charAt(x) == '1') {
                        x++;
                    }
                    // optimization: replace sequences of FULL_BLOCK_FRONT with wallRowOptimization[i]
                    while (x - start > 3) {
                        int log = (int) (Math.log((x - start) / 2.0) / Math.log(2));
                        log = Math.min(4, log);

                        char optimizationChar = new char[]{'1', 'A', 'B', 'C', 'D'}[log];
                        int endIdx = start + (int) Math.pow(2, log) * 2;

                        for (int i = start; i < endIdx; i++) {
                            row = replaceChar(row, i, optimizationChar);
                        }

                        start = endIdx;
                    }

                } else {
                    x++;
                }
            }

            newWallDescriptionRows[y] = row;
        }

        return newWallDescriptionRows;
    }

    private String replaceChar(String str, int idx, char newChar) {
        return str.substring(0, idx) + newChar + str.substring(idx + 1);
    }

    private ArrayList<ArrayList<int[]>> calcHitboxIndicesInRows(String preprocessedWallDescription) {
        // convert padded preprocessed wall description back to original (but still in the same dimension)
        String wallDescription = preprocessedWallDescription;
        for (String ch : new String[]{"0", "3", "4", "5", "6", "7"}) {
            wallDescription = wallDescription.replaceAll(ch, " ");
        }
        for (String ch : new String[]{"1", "2", "A", "B", "C", "D", "E"}) {
            wallDescription = wallDescription.replaceAll(ch, "x");
        }

        String[] wallDescriptionRows = trimLinebreaks(wallDescription).split("\n");

        ArrayList<ArrayList<int[]>> hitboxesInRows = new ArrayList<>(wallDescriptionRows.length);

        for (String row : wallDescriptionRows) {
            ArrayList<int[]> hitboxIdx = new ArrayList<>();

            // find "blocks" of 'x's that follow one another
            // Ex.: "xxx  xx   xxxxx" => new int[][]{{0, 2}, {5, 6}, {10, 14}}
            int x = 0;
            while (x < row.length()) {
                if (row.charAt(x) == 'x') {
                    int start = x;
                    while (x < row.length() && row.charAt(x) == 'x') {
                        x++;
                    }
                    hitboxIdx.add(new int[]{start, x - 1});
                } else {
                    x++;
                }
            }

            hitboxesInRows.add(hitboxIdx);
        }

        return hitboxesInRows;
    }

    /**
     * A class that is a Wrapper for two important information for the {@link thd.gameobjects.movable.Wall} GameObject.
     * <ol>
     *    <li>The BlockGraphic String for {@link GameView#addBlockImageToCanvas(String, double, double, double, double)}</li>
     *    <li>The Hitboxes for {@link thd.gameobjects.base.CollidingGameObject}</li>
     * </ol>
     */
    public static final class DynamicWall {

        /**
         * The BlockGraphic String for {@link GameView#addBlockImageToCanvas(String, double, double, double, double)}.
         */
        public final String[] wallBlockImageInRows;
        /**
         * The indices for the hitboxes for this DynamicWall. Can be used to actually compute the Hitboxes.
         */
        public final ArrayList<ArrayList<int[]>> hitboxIndicesInRows;

        /**
         * Creates a new {@code DynamicWall} object.
         *
         * @param wallBlockImageInRows the BlockGraphic String
         * @param hitboxesInRows       the hitbox indices
         */
        public DynamicWall(String[] wallBlockImageInRows, ArrayList<ArrayList<int[]>> hitboxesInRows) {
            this.wallBlockImageInRows = wallBlockImageInRows;
            this.hitboxIndicesInRows = hitboxesInRows;
        }
    }
}
