package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code GunImplacement} is a stationary GameObject that yields 200 or 500 points (random) upon destruction.
 * They will appear in the Motherbase and shoot straight bullets at the player, but only when he is at ground level.
 *
 * @see GameObject
 */
public class GunImplacement extends GameObject {
    private final StationaryMovementPattern stationaryMovementPattern;
    /**
     * Initializes a {@code GunImplacement} object.
     *
     * @param gameView to display the {@code GunImplacement} object on
     */
    public GunImplacement(GameView gameView) {
        super(gameView);

        height = 114;
        width = 157;
        size = 0.5;

        stationaryMovementPattern = new StationaryMovementPattern(1);
        position.updateCoordinates(stationaryMovementPattern.startPosition());
        targetPosition.updateCoordinates(stationaryMovementPattern.nextPosition());
    }

    /**
     * Moves {@code GunImplacement} object forward.
     */
    @Override
    public void updatePosition() {
        if (gameView.gameTimeInMilliseconds() > 2000 && position.getX() > 0 && position.getY() < GameSettings.GAME_HEIGHT - height) {
            position.moveToPosition(targetPosition, speedInPixel);
        }
    }

    /**
     * Renders {@code GunImplacement} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addImageToCanvas("tank.png", position.getX(), position.getY(), size, rotation);
    }

    /**
     * String representation of {@code GunImplacement} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "Tank: " + position;
    }
}
