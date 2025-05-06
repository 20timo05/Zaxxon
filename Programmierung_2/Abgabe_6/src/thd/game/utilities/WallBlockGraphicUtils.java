package thd.game.utilities;


import java.util.ArrayList;

/**
 * This is a utility class used to precompute any possible Wall dynamically.
 * That includes both the BlockGraphic String and the Hitboxes the for Wall.
 * <p>
 * Since the calculation takes a few seconds, the result will have to be stored as a Level File and loaded instead of computed.
 * <p>
 * For an example, see the commented out main function which creates the BlockGraphic for {@link #SAMPLE_WALL}.
 */
public class WallBlockGraphicUtils {
    private static final String FULL_BLOCK_FRONT = """
LL
LLLL
LLggLL
LLggggLL
LLggggggLL
LLggggggggLL
LLggggggggggLL
LLggggggggggggLL
LLggggggggggggggLL
  LLggggggggggggLL
    LLggggggggggLL
      LLggggggggLL
        LLggggggLL
          LLggggLL
            LLggLL
              LLLL
                LL
""";
    private static final String HALF_BLOCK_FRONT = """
LL
LLLL
LLggLL
LLggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
  LLggggLL
    LLggLL
      LLLL
        LL
""";

    private static final String FULL_BLOCK_TOP = """
      LLLL
    LLggggLL
  LLggggggggLL
LLggggggggggggLL
LLggggggggggggggLL
  LLggggggggggggggLL
    LLggggggggggggggLL
      LLggggggggggggggLL
        LLggggggggggggLL
          LLggggggggLL
            LLggggLL
              LLLL
""";

    private static final String HALF_BLOCK_TOP = """
      LLLL
    LLggggLL
  LLggggggggLL
LLggggggggggggLL
LLggggggggggggggLL
  LLggggggggggggLL
    LLggggggggLL
      LLggggLL
        LLLL
""";

    private static final String BLOCK_SIDE = """
      LLLL
    LLggLL
  LLggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggLLLL
LLggLL
LLLL
""";

    /**
     * This is an example {@code wallDescription} String for a sample wall.
     */
    public static final String SAMPLE_WALL = """
xxxxxxxxxx            xxxxxxxxxxxxxx
xxxxxxxxxx            xxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxx          xxxxxxxxxxxxxxxxxxxxxx
xxxx          xxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
""";

    /**
     * Smaller alternative to {@link #SAMPLE_WALL} that is faster to compute.
     */
    public static final String SMALL_SAMPLE_WALL = """
xxxxxxx         xxxxxx
xxxxxxx         xxxxxx
xxxxxxxxxxxxxxxxxxxxxx
""";

    private static final int MAX_NUM_ROWS = 9;
    private static final int NUM_BLOCKS_PER_ROW = 36;

    /**
     * This refers to the horizontal offset after a full block.
     */
    public static final int FULL_BLOCK_INCREASE_OFFSET_X = 16;
    /**
     * This refers to the vertical offset after a full block & between rows during rendering.
     */
    public static final int FULL_BLOCK_INCREASE_OFFSET_Y = 8;
    /**
     * This refers to the horizontal offset after a half block.
     */
    public static final int HALF_BLOCK_INCREASE_OFFSET_X = 8;
    /**
     * This refers to the vertical offset after a half block.
     */
    public static final int HALF_BLOCK_INCREASE_OFFSET_Y = 4;


    static final int FULL_BLOCK_TOP_OFFSET_X = 0;
    static final int FULL_BLOCK_TOP_OFFSET_Y = 4;
    static final int BLOCK_SIDE_OFFSET_X = 0;
    static final int BLOCK_SIDE_OFFSET_Y = 5;

    /*
    public static void main(String[] args) {
        WallBlockGraphicUtils utils = new WallBlockGraphicUtils();
        String result = utils.generateWallBlockImage(SMALL_SAMPLE_WALL, true);
        System.out.println(result);
    }
     */

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
         * @param hitboxesInRows the hitbox indices
         */
        public DynamicWall(String[] wallBlockImageInRows, ArrayList<ArrayList<int[]>> hitboxesInRows) {
            this.wallBlockImageInRows = wallBlockImageInRows;
            this.hitboxIndicesInRows = hitboxesInRows;
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

    /*
     * Generates a complete BlockGraphic String for the Wall dynamically.
     *
     * @param wallDescription a BlockGraphic like String with 'x' where a block should be
     * @return the block Image
     *
     * public String generateWallBlockImage(String wallDescription) {
     *         DynamicWall dynamicWall = generateDynamicWall(wallDescription);
     *         String[] wallBlockImageInRows = dynamicWall.wallBlockImageInRows;
     *
     *         String combinedBlockImage = wallBlockImageInRows[0];
     *         for (int y = 1; y < wallBlockImageInRows.length; y++) {
     *             combinedBlockImage = combineBlockImages(
     *                     combinedBlockImage,
     *                     wallBlockImageInRows[y],
     *                     0,
     *                     FULL_BLOCK_INCREASE_OFFSET_Y*y
     *             );
     *         }
     *
     *         return combinedBlockImage;
     *     }
     */

    private String generateWallRowBlockImage(String wallDescriptionRow) {
        String combinedRowBlockImage = "";

        int offsetX = 0;
        int offsetY = 0;

        int x = 0;
        while (x < wallDescriptionRow.length()) {

            int blockType = Character.getNumericValue(wallDescriptionRow.charAt(x));
            String blockImage = new String[]{null, FULL_BLOCK_FRONT, HALF_BLOCK_FRONT, FULL_BLOCK_TOP, HALF_BLOCK_TOP, BLOCK_SIDE}[blockType];

            int additionalOffsetX = (blockType == 3 || blockType == 4) ? FULL_BLOCK_TOP_OFFSET_X : blockType == 5 ? BLOCK_SIDE_OFFSET_X : 0;
            int additionalOffsetY = (blockType == 3 || blockType == 4) ? FULL_BLOCK_TOP_OFFSET_Y : blockType == 5 ? BLOCK_SIDE_OFFSET_Y : 0;

            int increaseOffsetX = (blockType == 1 || blockType == 3) ? FULL_BLOCK_INCREASE_OFFSET_X : HALF_BLOCK_INCREASE_OFFSET_X;
            int increaseOffsetY = (blockType == 1 || blockType == 3) ? FULL_BLOCK_INCREASE_OFFSET_Y : HALF_BLOCK_INCREASE_OFFSET_Y;
            int thisWidth = (blockType == 1 || blockType == 3) ? 2 : 1;

            if (blockImage != null) {
                combinedRowBlockImage = combineBlockImages(
                        combinedRowBlockImage,
                        blockImage,
                        offsetX+additionalOffsetX,
                        offsetY+additionalOffsetY
                );
            }
            offsetX += increaseOffsetX;
            offsetY += increaseOffsetY;
            x += thisWidth;
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
        // ===== draw the first image on the combined Block Image =====
        for (int y = 0; y < firstBlockImageDimensions[0]; y++) {
            for (int x = 0; x < firstBlockImageDimensions[1]; x++) {
                int xPosOnCombined = x + columnsAddedToLeft;
                int yPosOnCombined = y + rowsAddedToTop;

                combinedBlockImage[yPosOnCombined][xPosOnCombined] = blockImageCharAt(firstBlockImage, y, x);
            }
        }

        // ===== draw the second image on the combined Block Image =====
        for (int y = 0; y < secondBlockImageDimensions[0]; y++) {
            for (int x = 0; x < secondBlockImageDimensions[1]; x++) {
                int xPosOnCombined = x + Math.max(offsetX, 0);
                int yPosOnCombined = y + Math.max(offsetY, 0);

                char charFromSecondBlockImage = blockImageCharAt(secondBlockImage, y, x);
                if (charFromSecondBlockImage != ' ') {
                    combinedBlockImage[yPosOnCombined][xPosOnCombined] = charFromSecondBlockImage;
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

    private char blockImageCharAt(String blockImage, int y, int x) {
        String[] blockImageRows = blockImage.split("\n");
        if (y >= blockImageRows.length || x >= blockImageRows[y].length()) {
            return ' ';
        }

        return blockImageRows[y].charAt(x);
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

    // ================================================================================================================
    // ======================================= PREPROCESSING OF WALLDESCRIPTION =======================================
    // ================================================================================================================

    /**
     * Preprocesses the {@code wallDescription} so that it calculates which blocks go where.
     * 0: Hole
     * 1: {@link #FULL_BLOCK_FRONT}
     * 2: {@link #HALF_BLOCK_FRONT}
     * 3: {@link #FULL_BLOCK_TOP}
     * 4: {@link #HALF_BLOCK_TOP}
     * 5: {@link #BLOCK_SIDE}
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
                        || wallDescriptionDimensions[1] > NUM_BLOCKS_PER_ROW
        ) {
            throw new IllegalArgumentException("Walls should have an odd height (1, 3, 5, 7, 9) and should not be wider then " + NUM_BLOCKS_PER_ROW);
        }

        trimmedWallDescription = trimmedWallDescription.replaceAll(" ", "0"); // convert spaces to 0
        String paddedWallDescription = "0".repeat(wallDescriptionDimensions[1]) + "\n" + trimmedWallDescription;
        paddedWallDescription = paddedWallDescription.replaceAll("\n", "0\n") + "0";
        String[] paddedWallDescriptionRows = paddedWallDescription.split("\n");

        paddedWallDescriptionRows = findFrontBlocks(paddedWallDescriptionRows);
        paddedWallDescriptionRows = findTopBlocks(paddedWallDescriptionRows);
        paddedWallDescriptionRows = findSideBlocks(paddedWallDescriptionRows);

        return String.join("\n", paddedWallDescriptionRows);
    }

    /**
     * this method trims of '\n' from start and end of wallDescription String
     *
     * @param wallDescription string describing a wall
     * @return the trimmed wallDescription string
     *
     * @see #SAMPLE_WALL
     */
    private String trimLinebreaks(String wallDescription) {
        int startIdx = 0;
        int endIdx = wallDescription.length();

        while (startIdx < endIdx && wallDescription.charAt(startIdx) == '\n') {
            startIdx++;
        }

        while (endIdx > startIdx && wallDescription.charAt(endIdx-1) == '\n') {
            endIdx--;
        }

        return wallDescription.substring(startIdx, endIdx);
    }

    private String[] findFrontBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 0; y < paddedWallDescriptionRows.length; y++) {

            String row = paddedWallDescriptionRows[y];
            boolean isFirstBlockFull = y % 2 == 1;
            int x = 0;
            while (x < paddedWallDescriptionRows[y].length()) {
                if (paddedWallDescriptionRows[y].charAt(x) == '0') {
                    x += 1;
                    continue;
                }

                boolean isNextBlockAFullBlock = x + 1 <= row.length() - 1 && row.charAt(x+1) != '0';
                if (x == 0 && !isFirstBlockFull) {
                    isNextBlockAFullBlock = false;
                }

                if (isNextBlockAFullBlock) {
                    row = replaceChar(row, x, '1');
                    row = replaceChar(row, x+1, '1');
                    x += 2;
                } else {
                    row = replaceChar(row, x, '2');
                    x++;
                }
            }

            newWallDescriptionRows[y] = row;
        }

        return newWallDescriptionRows;
    }

    private String[] findTopBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 1; y < paddedWallDescriptionRows.length; y++) {

            String aboveRow = paddedWallDescriptionRows[y-1];
            for (int x = 0; x < paddedWallDescriptionRows[y].length(); x++) {
                if (aboveRow.charAt(x) != '0') {
                    continue;
                }

                if (paddedWallDescriptionRows[y].charAt(x) == '1') {
                    if (aboveRow.charAt(x+1) == '0') {
                        aboveRow = replaceChar(aboveRow, x, '3');
                    } else {
                        aboveRow = replaceChar(aboveRow, x, '4');
                    }
                }
                if (paddedWallDescriptionRows[y].charAt(x) == '2') {
                    aboveRow = replaceChar(aboveRow, x, '4');
                }
            }

            newWallDescriptionRows[y-1] = aboveRow;
        }
        newWallDescriptionRows[newWallDescriptionRows.length-1] = paddedWallDescriptionRows[paddedWallDescriptionRows.length - 1];

        return newWallDescriptionRows;
    }

    private String[] findSideBlocks(String[] paddedWallDescriptionRows) {
        String[] newWallDescriptionRows = new String[paddedWallDescriptionRows.length];

        for (int y = 1; y < paddedWallDescriptionRows.length; y++) {

            String row = paddedWallDescriptionRows[y];
            String rowAbove = paddedWallDescriptionRows[y-1];
            for (int x = 0; x < row.length()-1; x++) {
                if ((row.charAt(x) == '1' || row.charAt(x) == '2') && !(row.charAt(x + 1) == '1' || row.charAt(x + 1) == '2')) {
                    rowAbove = replaceChar(rowAbove, x + 1, '5');
                }
            }

            newWallDescriptionRows[y-1] = rowAbove;
            newWallDescriptionRows[y] = row;
        }

        return newWallDescriptionRows;
    }

    private String replaceChar(String str, int idx, char newChar) {
        return str.substring(0, idx) + newChar + str.substring(idx+1);
    }


    private ArrayList<ArrayList<int[]>> calcHitboxIndicesInRows(String preprocessedWallDescription) {
        // convert padded preprocessed wall description back to original (but still in the same dimension)
        String wallDescription = preprocessedWallDescription;
        for (String ch : new String[]{"0", "3", "4", "5"}) {
            wallDescription = wallDescription.replaceAll(ch, " ");
        }
        for (String ch : new String[]{"1", "2"}) {
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
}
