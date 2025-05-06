package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;


/**
 * A status bar, that displays the current height of the Player.
 *
 * @see GameObject
 */
public class HeightStatusBar extends GameObject {
    private static final int MAX_NUM_LINES_PER_BLOCK = 6;
    private static final int MAX_NUM_BLOCKS = 5;

    private double playerHeightInterpolation;

    /**
     * Initializes a {@code HeightStatusBar} object.
     *
     * @param gameView to display the {@code HeightStatusBar} object on
     * @param gamePlayManager   reference to the gamePlayManager
     */
    public HeightStatusBar(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);

        size = 7;
        width = 5;
        height = MAX_NUM_LINES_PER_BLOCK * MAX_NUM_BLOCKS;
        playerHeightInterpolation = 0;

        position.updateCoordinates(new Position(0, GameView.HEIGHT / 2.0 - height * size));
    }

    /**
     * Renders {@code HeightStatusBar} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        String heightBarBlockImage = createBlockImage(playerHeightInterpolation);
        gameView.addBlockImageToCanvas(heightBarBlockImage, position.getX(), position.getY(), size, rotation);
    }

    /**
     * Updates the {@link #playerHeightInterpolation} based on the new {@code playerHeightInterpolation}.
     *
     * @param playerAltitudeInterpolation the new {@code playerHeightInterpolation}
     */
    public void updateStatus(double playerAltitudeInterpolation) {
        this.playerHeightInterpolation = playerAltitudeInterpolation;
    }

    /**
     * Creates the BlockImage Graphic String dynamically based on the height of the Player.
     *
     * @param playerHeightInterpolation interpolation factor of how high the player currently is
     * @return the BlockImage Graphic String
     */
    private String createBlockImage(double playerHeightInterpolation) {
        if (playerHeightInterpolation < 0 || playerHeightInterpolation > 1) {
            throw new IllegalArgumentException("playerHeightInterpolation should be in [0, 1]");
        }

        // calculate shape of height bar
        int numPixelsToDisplay = (int) Math.round(MAX_NUM_LINES_PER_BLOCK * MAX_NUM_BLOCKS * playerHeightInterpolation);
        int numBlocks = numPixelsToDisplay / MAX_NUM_LINES_PER_BLOCK;
        int heightOfLastBlock = numPixelsToDisplay % MAX_NUM_LINES_PER_BLOCK;

        // create the BlockImage String for the Height Bar
        StringBuilder blockImage = new StringBuilder();
        for (int block = 0; block < MAX_NUM_BLOCKS; block++) {

            for (int line = 0; line < MAX_NUM_LINES_PER_BLOCK; line++) {

                if (block < numBlocks || (block == numBlocks && line < heightOfLastBlock)) {
                    blockImage.insert(0, "C".repeat((int) width) + "\n");
                } else {
                    blockImage.insert(0, " ".repeat((int) width) + "\n");
                }

            }

            if (block < numBlocks) {
                blockImage.insert(0, " ".repeat((int) width) + "\n");
            } else {
                blockImage.insert(0, "C".repeat((int) width) + "\n");
            }
        }

        return blockImage.toString();
    }
}
