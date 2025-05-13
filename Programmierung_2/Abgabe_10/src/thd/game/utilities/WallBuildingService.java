package thd.game.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import static thd.game.utilities.WallBlockImages.*;

/**
 * A service class responsible for building the graphical representation
 * and calculating hitboxes from a preprocessed wall description string.
 */
public class WallBuildingService { // Package-private access

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


    private static final int FULL_BLOCK_TOP_OFFSET_X = 0;
    private static final int FULL_BLOCK_TOP_OFFSET_Y = 4;
    private static final int BLOCK_SIDE_OFFSET_X = 0;
    private static final int BLOCK_SIDE_OFFSET_Y = -3;

    // Structure to hold dimension and offset data directly
    private static class BlockDimensions {
        final String blockImage;
        final int additionalOffsetX;
        final int additionalOffsetY;
        final int increaseOffsetX;
        final int increaseOffsetY;
        final int charWidth;

        BlockDimensions(String blockImage, int additionalOffsetX, int additionalOffsetY, int increaseOffsetX, int increaseOffsetY, int charWidth) {
            this.blockImage = blockImage;
            this.additionalOffsetX = additionalOffsetX;
            this.additionalOffsetY = additionalOffsetY;
            this.increaseOffsetX = increaseOffsetX;
            this.increaseOffsetY = increaseOffsetY;
            this.charWidth = charWidth;
        }
    }

    private static HashMap<Character, BlockDimensions> wallBlockDimensionsMap;

    /**
     * Initializes the internal map of block dimensions.
     * This must be called before using generateWallRowBlockImage.
     *
     * @param wallRowOptimization The precomputed optimized wall rows from WallBlockGraphicUtils.
     */
    static void initializeDimensionMap(String[] wallRowOptimization) {
        if (wallBlockDimensionsMap == null) {
            wallBlockDimensionsMap = initMap(wallRowOptimization);
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private WallBuildingService() {
        // This constructor is intentionally private and empty.
    }

    /**
     * Generates a complete BlockGraphic String for a preprocessed wall row dynamically.
     *
     * @param wallDescriptionRow a preprocessed wall description row
     * @param wallRowOptimization the precomputed optimized wall rows (needed for map initialization if not already done)
     * @return the block image for the row
     */
    static String generateWallRowBlockImage(String wallDescriptionRow, String[] wallRowOptimization) { // Package-private access
        // Ensure the map is initialized
        initializeDimensionMap(wallRowOptimization);

        String combinedRowBlockImage = "";

        int offsetX = 0;
        int offsetY = 0;

        int x = 0;
        while (x < wallDescriptionRow.length()) {
            char currentChar = wallDescriptionRow.charAt(x);
            BlockDimensions blockDim = wallBlockDimensionsMap.get(currentChar);

            if (blockDim == null) {
                throw new IllegalArgumentException("Unknown char: " + currentChar);
            }


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
     * Combines two BlockImages together.
     * If they overlap, the pixels from the second one are used.
     *
     * @param firstBlockImage  first BlockImage string
     * @param secondBlockImage second BlockImage string
     * @param offsetX          horizontal offset of the second BlockImage
     * @param offsetY          vertical offset of the second BlockImage
     * @return a new combined BlockImage
     */
    static String combineBlockImages(String firstBlockImage, String secondBlockImage,
                                     int offsetX, int offsetY) { // Package-private access
        int[] firstBlockImageDimensions = WallBlockGraphicUtils.calcBlockImageDimension(firstBlockImage);
        int[] secondBlockImageDimensions = WallBlockGraphicUtils.calcBlockImageDimension(secondBlockImage);

        // calculate if the offset of the second image has "moved" the first image relative to the top left corner
        int rowsAddedToTop = offsetY < 0 ? -offsetY : 0;
        int columnsAddedToLeft = offsetX < 0 ? -offsetX : 0;

        int combinedHeight = Math.max(firstBlockImageDimensions[0],
                secondBlockImageDimensions[0] + offsetY) + rowsAddedToTop;
        int combinedWidth = Math.max(firstBlockImageDimensions[1],
                secondBlockImageDimensions[1] + offsetX) + columnsAddedToLeft;

        char[][] combinedBlockImage = new char[combinedHeight][combinedWidth];

        // Initialize the combined image with spaces
        for (int y = 0; y < combinedHeight; y++) {
            for (int x = 0; x < combinedWidth; x++) {
                combinedBlockImage[y][x] = ' ';
            }
        }

        String[] firstBlockImageInRows = firstBlockImage.split("\n");
        // ===== draw the first image on the combined BlockImage =====
        for (int y = 0; y < firstBlockImageDimensions[0]; y++) {
            for (int x = 0; x < firstBlockImageDimensions[1]; x++) {
                int xPosOnCombined = x + columnsAddedToLeft;
                int yPosOnCombined = y + rowsAddedToTop;

                if (y < firstBlockImageInRows.length && x < firstBlockImageInRows[y].length()) {
                    if (xPosOnCombined >= 0 && xPosOnCombined < combinedWidth &&
                            yPosOnCombined >= 0 && yPosOnCombined < combinedHeight) {
                        combinedBlockImage[yPosOnCombined][xPosOnCombined] =
                                firstBlockImageInRows[y].charAt(x);
                    }
                }
            }
        }

        // ===== draw the second image on the combined BlockImage =====
        String[] secondBlockImageInRows = secondBlockImage.split("\n");
        for (int y = 0; y < secondBlockImageDimensions[0]; y++) {
            for (int x = 0; x < secondBlockImageDimensions[1]; x++) {
                int xPosOnCombined = x + Math.max(offsetX, 0) + columnsAddedToLeft; // Adjusted for potential left shift
                int yPosOnCombined = y + Math.max(offsetY, 0) + rowsAddedToTop;     // Adjusted for potential up shift


                if (y < secondBlockImageInRows.length && x < secondBlockImageInRows[y].length()) {
                    char charFromSecondBlockImage = secondBlockImageInRows[y].charAt(x);
                    if (charFromSecondBlockImage != ' ') {
                        if (xPosOnCombined >= 0 && xPosOnCombined < combinedWidth &&
                                yPosOnCombined >= 0 && yPosOnCombined < combinedHeight) {
                            combinedBlockImage[yPosOnCombined][xPosOnCombined] =
                                    charFromSecondBlockImage;
                        }
                    }
                }
            }
        }

        return combineCharArray(combinedBlockImage);
    }

    private static String combineCharArray(char[][] combinedBlockImage) {
        StringBuilder result = new StringBuilder();

        for (int y = 0; y < combinedBlockImage.length; y++) {
            result.append(combinedBlockImage[y]);
            if (y < combinedBlockImage.length - 1) {
                result.append('\n');
            }
        }

        return result.toString().replace('\u0000', ' ');
    }


    /**
     * Calculates the hitbox indices for each row of the preprocessed wall description.
     *
     * @param preprocessedWallDescription the preprocessed wall description string
     * @return a list of lists, where each inner list contains int arrays representing hitbox start and end indices for a row.
     */
    static ArrayList<ArrayList<int[]>> calcHitboxIndicesInRows(String preprocessedWallDescription) { // Package-private access
        // Convert padded preprocessed wall description back to original (but still in the same dimension)
        String wallDescription = preprocessedWallDescription;
        for (String ch : new String[]{"0", "3", "4", "5", "6", "7", "8"}) { // Added '8' to ignore
            wallDescription = wallDescription.replaceAll(ch, " ");
        }
        for (String ch : new String[]{"1", "2", "A", "B", "C", "D"}) { // Added 'E' if needed, but not in optimization
            wallDescription = wallDescription.replaceAll(ch, "x");
        }

        String[] wallDescriptionRows = WallPreprocessingService.trimLinebreaks(wallDescription).split("\n"); // Reusing trimLinebreaks

        ArrayList<ArrayList<int[]>> hitboxesInRows = new ArrayList<>(wallDescriptionRows.length);

        for (String row : wallDescriptionRows) {
            ArrayList<int[]> hitboxIdx = new ArrayList<>();

            // find "blocks" of 'x's that follow one another
            // Ex.: "xxx  xx   xxxxx" => new int[][]{{0, 2}, {5, 6}, {10, 14}}
            int x = 0;
            while (x < row.length()) {
                if (row.length() > 0 && row.charAt(x) == 'x') { // Added check for row length
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

    // a dictionary of the sizes & specific information for all Blocks used in the Dynamic Wall Block Image Generation
    private static HashMap<Character, BlockDimensions> initMap(String[] wallRowOptimization) {
        HashMap<Character, BlockDimensions> map = new HashMap<>();
        int increaseOffsetXDefault = HALF_BLOCK_INCREASE_OFFSET_X;
        int increaseOffsetYDefault = HALF_BLOCK_INCREASE_OFFSET_Y;

        map.put('0', new BlockDimensions(null, 0, 0, HALF_BLOCK_INCREASE_OFFSET_X, HALF_BLOCK_INCREASE_OFFSET_Y, 1));
        map.put('1', new BlockDimensions(FULL_BLOCK_FRONT, 0, 0, FULL_BLOCK_INCREASE_OFFSET_X, FULL_BLOCK_INCREASE_OFFSET_Y, 2));
        map.put('2', new BlockDimensions(HALF_BLOCK_FRONT, 0, 0, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        map.put('3', new BlockDimensions(FULL_BLOCK_TOP, FULL_BLOCK_TOP_OFFSET_X, FULL_BLOCK_TOP_OFFSET_Y, FULL_BLOCK_INCREASE_OFFSET_X, FULL_BLOCK_INCREASE_OFFSET_Y, 2));
        map.put('4', new BlockDimensions(HALF_BLOCK_TOP, FULL_BLOCK_TOP_OFFSET_X, FULL_BLOCK_TOP_OFFSET_Y, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        map.put('5', new BlockDimensions(BLOCK_SIDE, BLOCK_SIDE_OFFSET_X, BLOCK_SIDE_OFFSET_Y, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        map.put('6', new BlockDimensions(BLOCK_SIDE_FULL_TOP, BLOCK_SIDE_OFFSET_X, BLOCK_SIDE_OFFSET_Y, FULL_BLOCK_INCREASE_OFFSET_X, FULL_BLOCK_INCREASE_OFFSET_Y, 2));
        map.put('7', new BlockDimensions(BLOCK_SIDE_HALF_TOP, BLOCK_SIDE_OFFSET_X, BLOCK_SIDE_OFFSET_Y, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        map.put('8', new BlockDimensions(BLOCK_SIDE_HALF, 0, 0, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        // Accessing wallRowOptimization from the outer class (or passed as a parameter)
        map.put('A', new BlockDimensions(wallRowOptimization[1], 0, 0, FULL_BLOCK_INCREASE_OFFSET_X * 2, FULL_BLOCK_INCREASE_OFFSET_Y * 2, 4));
        map.put('B', new BlockDimensions(wallRowOptimization[2], 0, 0, FULL_BLOCK_INCREASE_OFFSET_X * 4, FULL_BLOCK_INCREASE_OFFSET_Y * 4, 8));
        map.put('C', new BlockDimensions(wallRowOptimization[3], 0, 0, FULL_BLOCK_INCREASE_OFFSET_X * 8, FULL_BLOCK_INCREASE_OFFSET_Y * 8, 16));
        map.put('D', new BlockDimensions(wallRowOptimization[4], 0, 0, FULL_BLOCK_INCREASE_OFFSET_X * 16, FULL_BLOCK_INCREASE_OFFSET_Y * 16, 32));

        return map;
    }
}
