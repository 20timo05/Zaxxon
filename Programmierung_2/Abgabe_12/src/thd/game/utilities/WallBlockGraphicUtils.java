package thd.game.utilities;

import java.util.ArrayList;

import static thd.game.utilities.WallBlockImages.FULL_BLOCK_FRONT;

/**
 * This is a utility class used to precompute any possible wall dynamically.
 * It precomputes both the BlockGraphic String and the hitboxes for a wall.
 * <p>
 * Since the calculation takes a few seconds, the result should eventually be stored
 * as a level file and loaded instead of computed.
 * <p>
 * For an example, see the commented out main function which creates the BlockGraphic for
 * {@link WallBlockImages#SAMPLE_WALL}.
 */
public class WallBlockGraphicUtils {

    // These constants might still be useful here or could move to WallPreprocessingService
    // based on where they are primarily used. Keeping them here for now as they define overall wall constraints.
    private static final int MAX_NUM_ROWS = 9;
    private static final int NUM_BLOCKS_PER_ROW = 36;


    // Precompute the optimized front block rows immediately.
    private static final String[] WALL_ROW_OPTIMIZATION = precomputeOptimizedFrontBlockRows();

    /**
     * Generates a dynamic wall for a given wall description.
     *
     * @param wallDescription a BlockGraphic-like String with 'x' where a block should be
     * @return a DynamicWall instance containing the block image and hitboxes per row
     */
    public static DynamicWall generateDynamicWall(String wallDescription) {
        // Use the preprocessing service
        String preprocessedWallDescription = WallPreprocessingService.preprocessWallDescription(wallDescription);

        String[] wallDescriptionRows = preprocessedWallDescription.split("\n");

        String[] wallBlockImageInRows = new String[wallDescriptionRows.length];
        for (int y = 0; y < wallDescriptionRows.length; y++) {
            // Use the wall building service to generate block images per row
            wallBlockImageInRows[y] = WallBuildingService.generateWallRowBlockImage(wallDescriptionRows[y], WALL_ROW_OPTIMIZATION);
        }

        // Use the wall building service to calculate hitboxes
        ArrayList<ArrayList<int[]>> hitboxIndicesInRows = WallBuildingService.calcHitboxIndicesInRows(preprocessedWallDescription);

        return new DynamicWall(wallBlockImageInRows, hitboxIndicesInRows);
    }

    /**
     * Generates a complete BlockGraphic String for the wall dynamically.
     *
     * @param wallDescription a BlockGraphic-like String with 'x' where a block should be
     * @return the block image
     */
    public static String generateWallBlockImage(String wallDescription) {
        DynamicWall dynamicWall = generateDynamicWall(wallDescription);
        String[] wallBlockImageInRows = dynamicWall.wallBlockImageInRows;

        if (wallBlockImageInRows == null || wallBlockImageInRows.length == 0) {
            return ""; // Or handle the empty case appropriately
        }

        String combinedBlockImage = wallBlockImageInRows[0];
        for (int y = 1; y < wallBlockImageInRows.length; y++) {
            // Use the wall building service to combine images
            combinedBlockImage = WallBuildingService.combineBlockImages(
                    combinedBlockImage,
                    wallBlockImageInRows[y],
                    0,
                    WallBuildingService.FULL_BLOCK_INCREASE_OFFSET_Y * y
            );
        }

        return combinedBlockImage;
    }

    /**
     * Mirrors each row of the 2D BlockImage representation horizontally.
     *
     * @param blockImage the block image String to mirror
     * @return the horizontally mirrored BlockImage
     */
    public static String mirrorBlockImage(String blockImage) {
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

    /**
     * Given a multiline String, it calculates the width (longest line) and height (number of lines).
     * This remains in WallBlockGraphicUtils as it's a general utility for BlockImages.
     *
     * @param blockImage the String to calculate dimensions for
     * @return an array with {height, width}
     */
    public static int[] calcBlockImageDimension(String blockImage) {
        String[] blockImageRows = blockImage.split("\n");

        int height = blockImageRows.length;
        int width = 0;
        if (blockImageRows.length > 0) {
            width = blockImageRows[0].length();
        }


        for (String row : blockImageRows) {
            if (row.length() > width) {
                width = row.length();
            }
        }

        return new int[]{height, width};
    }

    // --- Private Static Methods ---

    private static String[] precomputeOptimizedFrontBlockRows() {
        String[] wallRowOptimization = new String[5];
        wallRowOptimization[0] = FULL_BLOCK_FRONT;

        for (int i = 1; i < wallRowOptimization.length; i++) {
            // Use the wall building service for combining
            wallRowOptimization[i] = WallBuildingService.combineBlockImages(
                    wallRowOptimization[i - 1],
                    wallRowOptimization[i - 1],
                    (int) (WallBuildingService.FULL_BLOCK_INCREASE_OFFSET_X * Math.pow(2, i - 1)),
                    (int) (WallBuildingService.FULL_BLOCK_INCREASE_OFFSET_Y * Math.pow(2, i - 1))
            );
        }

        // Initialize the WallBlockDimensionCalculator's map after wallRowOptimization is ready
        // This needs to be done here because WallBlockDimensionCalculator (now its data)
        // is in WallBuildingService and relies on the wallRowOptimization array from WallBlockGraphicUtils.
        WallBuildingService.initializeDimensionMap(wallRowOptimization);

        return wallRowOptimization;
    }


    /**
     * Private constructor to prevent instantiation.
     */
    private WallBlockGraphicUtils() {
        // This constructor is intentionally private and empty.
    }

    /**
     * A class that serves as a wrapper for two important pieces of information for {@link thd.gameobjects.movable.Wall} GameObject.
     *
     * <ol>
     *   <li>The BlockGraphic String for {@link GameView#addBlockImageToCanvas(String, double, double, double, double)}</li>
     *   <li>The hitboxes for {@link thd.gameobjects.base.CollidingGameObject}</li>
     * </ol>
     */
    public static final class DynamicWall {

        /**
         * The BlockGraphic String for each row of the wall.
         */
        public final String[] wallBlockImageInRows;
        /**
         * The indices for the hitboxes for this DynamicWall.
         */
        public final ArrayList<ArrayList<int[]>> hitboxIndicesInRows;

        /**
         * Creates a new {@code DynamicWall} instance.
         *
         * @param wallBlockImageInRows the block image String per row
         * @param hitboxesInRows       the hitbox indices per row
         */
        public DynamicWall(String[] wallBlockImageInRows,
                           ArrayList<ArrayList<int[]>> hitboxesInRows) {
            this.wallBlockImageInRows = wallBlockImageInRows;
            this.hitboxIndicesInRows = hitboxesInRows;
        }
    }
}