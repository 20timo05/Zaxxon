package thd.game.utilities;

import java.util.HashMap;

import static thd.game.utilities.WallBlockImages.*;

/**
 * Given a char of the preprocessed Wall Description, this class calculates all necessary values for combining these BlockImages.
 *
 * @see WallBlockGraphicUtils
 */
public class WallBlockDimensionCalculator {
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
    static final int BLOCK_SIDE_OFFSET_Y = -3;


    final String blockImage;
    final int additionalOffsetX;
    final int additionalOffsetY;
    final int increaseOffsetX;
    final int increaseOffsetY;
    final int charWidth;

    private WallBlockDimensionCalculator(String blockImage, int additionalOffsetX, int additionalOffsetY, int increaseOffsetX, int increaseOffsetY, int charWidth) {
        this.blockImage = blockImage;
        this.additionalOffsetX = additionalOffsetX;
        this.additionalOffsetY = additionalOffsetY;
        this.increaseOffsetX = increaseOffsetX;
        this.increaseOffsetY = increaseOffsetY;
        this.charWidth = charWidth;
    }

    // @TODO put HashMap into Static Initializer once allowed
    WallBlockDimensionCalculator(char c, String[] wallRowOptimization) {
        HashMap<Character, WallBlockDimensionCalculator> wallBlockDimensions = initMap(wallRowOptimization);
        WallBlockDimensionCalculator dim = wallBlockDimensions.get(c);

        if (dim == null) {
            throw new IllegalArgumentException("Unknown char: " + c);
        }

        this.blockImage = dim.blockImage;
        this.additionalOffsetX = dim.additionalOffsetX;
        this.additionalOffsetY = dim.additionalOffsetY;
        this.increaseOffsetX = dim.increaseOffsetX;
        this.increaseOffsetY = dim.increaseOffsetY;
        this.charWidth = dim.charWidth;
    }


    // a dictionary of the sizes & specific information for all Blocks used in the Dynamic Wall Block Image Generation
    private HashMap<Character, WallBlockDimensionCalculator> initMap(String[] wallRowOptimization) {
        int increaseOffsetXDefault = HALF_BLOCK_INCREASE_OFFSET_X;
        int increaseOffsetYDefault = HALF_BLOCK_INCREASE_OFFSET_Y;

        // a dictionary of the sizes & specific information for all Blocks used in the Dynamic Wall Block Image Generation
        HashMap<Character, WallBlockDimensionCalculator> wallBlockDimensions = new HashMap<>();
        wallBlockDimensions.put('0', new WallBlockDimensionCalculator(null, 0, 0, HALF_BLOCK_INCREASE_OFFSET_X, HALF_BLOCK_INCREASE_OFFSET_Y, 1));
        wallBlockDimensions.put('1', new WallBlockDimensionCalculator(FULL_BLOCK_FRONT, 0, 0, FULL_BLOCK_INCREASE_OFFSET_X, FULL_BLOCK_INCREASE_OFFSET_Y, 2));
        wallBlockDimensions.put('2', new WallBlockDimensionCalculator(HALF_BLOCK_FRONT, 0, 0, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        wallBlockDimensions.put('3', new WallBlockDimensionCalculator(FULL_BLOCK_TOP, FULL_BLOCK_TOP_OFFSET_X, FULL_BLOCK_TOP_OFFSET_Y, FULL_BLOCK_INCREASE_OFFSET_X, FULL_BLOCK_INCREASE_OFFSET_Y, 2));
        wallBlockDimensions.put('4', new WallBlockDimensionCalculator(HALF_BLOCK_TOP, FULL_BLOCK_TOP_OFFSET_X, FULL_BLOCK_TOP_OFFSET_Y, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        wallBlockDimensions.put('5', new WallBlockDimensionCalculator(BLOCK_SIDE, BLOCK_SIDE_OFFSET_X, BLOCK_SIDE_OFFSET_Y, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        wallBlockDimensions.put('6', new WallBlockDimensionCalculator(BLOCK_SIDE_FULL_TOP, BLOCK_SIDE_OFFSET_X, BLOCK_SIDE_OFFSET_Y, FULL_BLOCK_INCREASE_OFFSET_X, FULL_BLOCK_INCREASE_OFFSET_Y, 2));
        wallBlockDimensions.put('7', new WallBlockDimensionCalculator(BLOCK_SIDE_HALF_TOP, BLOCK_SIDE_OFFSET_X, BLOCK_SIDE_OFFSET_Y, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        wallBlockDimensions.put('8', new WallBlockDimensionCalculator(BLOCK_SIDE_HALF, 0, 0, increaseOffsetXDefault, increaseOffsetYDefault, 1));
        wallBlockDimensions.put('A', new WallBlockDimensionCalculator(wallRowOptimization[1], 0, 0, FULL_BLOCK_INCREASE_OFFSET_X * 2, FULL_BLOCK_INCREASE_OFFSET_Y * 2, 4));
        wallBlockDimensions.put('B', new WallBlockDimensionCalculator(wallRowOptimization[2], 0, 0, FULL_BLOCK_INCREASE_OFFSET_X * 4, FULL_BLOCK_INCREASE_OFFSET_Y * 4, 8));
        wallBlockDimensions.put('C', new WallBlockDimensionCalculator(wallRowOptimization[3], 0, 0, FULL_BLOCK_INCREASE_OFFSET_X * 8, FULL_BLOCK_INCREASE_OFFSET_Y * 8, 16));
        wallBlockDimensions.put('D', new WallBlockDimensionCalculator(wallRowOptimization[4], 0, 0, FULL_BLOCK_INCREASE_OFFSET_X * 16, FULL_BLOCK_INCREASE_OFFSET_Y * 16, 32));

        return wallBlockDimensions;
    }
}