package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.game.utilities.WallBlockDimensionCalculator;
import thd.game.utilities.WallBlockGraphicUtils;
import thd.gameobjects.base.*;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * The WallBackground GameObject moves along with the other GameObjects, but is
 * solely for design purposes.
 */
public class WallBackground extends GameObject implements ShiftableGameObject, ActivatableGameObject<WallBackground> {
    private final String backgroundWallBlockImage;
    private boolean active;
    /**
     * Creates a new {@code WallBackground} GameObject.
     *
     * @param gameView                 GameView to show the game object on.
     * @param gamePlayManager          reference to the gamePlayManager
     * @param backgroundWallBlockImage the pregenerated BlockImage String for the
     *                                 background Wall
     * @param isFirst                  boolean because the first Wall should be active by default
     */
    public WallBackground(GameView gameView, GamePlayManager gamePlayManager, String backgroundWallBlockImage, boolean isFirst) {
        super(gameView, gamePlayManager, 0, -0.03);

        this.backgroundWallBlockImage = backgroundWallBlockImage;

        int[] wallBlockDimensions = new WallBlockGraphicUtils().calcBlockImageDimension(backgroundWallBlockImage);
        height = wallBlockDimensions[0];
        width = wallBlockDimensions[1];
        size = (int) (GameSettings.MAX_PLAYER_ALTITUDE / (9 * WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_Y));

        position.moveToPosition(targetPosition, 20); // move background a little forwards (just for visuals)
        rotation = 0;

        Vector2d newTargetPosition = new Vector2d(targetPosition);
        newTargetPosition.add(new Vector2d(
                -TRAVEL_PATH_CALCULATOR.getDistanceToDespawnLine(),
                GameSettings.MOVEMENT_ANGLE_IN_RADIANS
        ));
        targetPosition.updateCoordinates(newTargetPosition);

        this.active = isFirst;

        distanceToBackground = 1;

        if (active) {
            gamePlayManager.spawnGameObject(new BackgroundFloor(gameView, gamePlayManager, height, true));
        }
    }

    /**
     * Renders {@code WallBackground} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        if (active) {
            gameView.addBlockImageToCanvas(backgroundWallBlockImage, position.getX(), position.getY()-height*size, size, rotation);
        }
    }

    /**
     * Moves the background.
     */
    @Override
    public void updatePosition() {
        if (active) {
            position.moveToPosition(targetPosition, speedInPixel);
        }
    }


    /**
     * Activates this {@code WallBackground} GameObject once the preceding {@code WallBackground} GameObject is far enough already.
     * This results in an alternating sequence of two {@code WallBackground} GameObjects, making it seem like one infinite continuous Wall.
     *
     * @param info the preceding {@code WallBackground} GameObject
     * @return boolean whether object is ready
     */
    @Override
    public boolean tryToActivate(WallBackground info) {
        if (!active) {
            active = info.calcInterpolation() > -0.2;
            if (active) {
                gamePlayManager.spawnGameObject(new BackgroundFloor(gameView, gamePlayManager, height, false));
            }
            return active;
        }
        return false;
    }
}
