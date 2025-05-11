package thd.game.utilities;

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

    WallBlockDimensionCalculator(char c, String[] wallRowOptimization) {
        String blockImageTemp = null;
        int additionalOffsetXTemp = 0;
        int additionalOffsetYTemp = 0;
        int increaseOffsetXTemp = HALF_BLOCK_INCREASE_OFFSET_X;
        int increaseOffsetYTemp = HALF_BLOCK_INCREASE_OFFSET_Y;
        int charWidthTemp = 1;

        switch (c) {
            case '1':
                blockImageTemp = FULL_BLOCK_FRONT;
                increaseOffsetXTemp = FULL_BLOCK_INCREASE_OFFSET_X;
                increaseOffsetYTemp = FULL_BLOCK_INCREASE_OFFSET_Y;
                charWidthTemp = 2;
                break;
            case '2':
                blockImageTemp = HALF_BLOCK_FRONT;
                break;
            case '3':
                blockImageTemp = FULL_BLOCK_TOP;
                additionalOffsetXTemp = FULL_BLOCK_TOP_OFFSET_X;
                additionalOffsetYTemp = FULL_BLOCK_TOP_OFFSET_Y;
                increaseOffsetXTemp = FULL_BLOCK_INCREASE_OFFSET_X;
                increaseOffsetYTemp = FULL_BLOCK_INCREASE_OFFSET_Y;
                charWidthTemp = 2;
                break;
            case '4':
                blockImageTemp = HALF_BLOCK_TOP;
                additionalOffsetXTemp = FULL_BLOCK_TOP_OFFSET_X;
                additionalOffsetYTemp = FULL_BLOCK_TOP_OFFSET_Y;
                break;
            case '5':
                blockImageTemp = BLOCK_SIDE;
                additionalOffsetXTemp = BLOCK_SIDE_OFFSET_X;
                additionalOffsetYTemp = BLOCK_SIDE_OFFSET_Y;
                break;
            case '6':
                blockImageTemp = BLOCK_SIDE_FULL_TOP;
                additionalOffsetXTemp = BLOCK_SIDE_OFFSET_X;
                additionalOffsetYTemp = BLOCK_SIDE_OFFSET_Y;
                increaseOffsetXTemp = FULL_BLOCK_INCREASE_OFFSET_X;
                increaseOffsetYTemp = FULL_BLOCK_INCREASE_OFFSET_Y;
                charWidthTemp = 2;
                break;
            case '7':
                blockImageTemp = BLOCK_SIDE_HALF_TOP;
                additionalOffsetXTemp = BLOCK_SIDE_OFFSET_X;
                additionalOffsetYTemp = BLOCK_SIDE_OFFSET_Y;
                break;
            case '8':
                blockImageTemp = BLOCK_SIDE_HALF;
                break;
            case 'A':
                blockImageTemp = wallRowOptimization[1];
                increaseOffsetXTemp = FULL_BLOCK_INCREASE_OFFSET_X*2;
                increaseOffsetYTemp = FULL_BLOCK_INCREASE_OFFSET_Y*2;
                charWidthTemp = 4;
                break;
            case 'B':
                blockImageTemp = wallRowOptimization[2];
                increaseOffsetXTemp = FULL_BLOCK_INCREASE_OFFSET_X*4;
                increaseOffsetYTemp = FULL_BLOCK_INCREASE_OFFSET_Y*4;
                charWidthTemp = 8;
                break;
            case 'C':
                blockImageTemp = wallRowOptimization[3];
                increaseOffsetXTemp = FULL_BLOCK_INCREASE_OFFSET_X*8;
                increaseOffsetYTemp = FULL_BLOCK_INCREASE_OFFSET_Y*8;
                charWidthTemp = 16;
                break;
            case 'D':
                blockImageTemp = wallRowOptimization[4];
                increaseOffsetXTemp = FULL_BLOCK_INCREASE_OFFSET_X*16;
                increaseOffsetYTemp = FULL_BLOCK_INCREASE_OFFSET_Y*16;
                charWidthTemp = 32;
                break;
        }

        blockImage = blockImageTemp;
        additionalOffsetX = additionalOffsetXTemp;
        additionalOffsetY = additionalOffsetYTemp;
        increaseOffsetX = increaseOffsetXTemp;
        increaseOffsetY = increaseOffsetYTemp;
        charWidth = charWidthTemp;
    }
}