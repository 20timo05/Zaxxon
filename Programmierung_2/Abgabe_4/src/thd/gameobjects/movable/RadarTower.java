package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code RadarTower} is a stationary GameObject that yields 1000 points upon destruction.
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 *
 * @see GameObject
 */
public class RadarTower extends GameObject {
    private final StationaryMovementPattern stationaryMovementPattern;

    /**
     * Initializes a {@code RadarTower} object.
     *
     * @param gameView to display the {@code RadarTower} object on
     */
    public RadarTower(GameView gameView) {
        super(gameView);

        height = 129;
        width = 109;
        size = 0.5;

        stationaryMovementPattern = new StationaryMovementPattern(Math.random());
        position.updateCoordinates(stationaryMovementPattern.startPosition());
        targetPosition.updateCoordinates(stationaryMovementPattern.nextPosition());
    }

    /**
     * Moves {@code RadarTower} object forward.
     */
    @Override
    public void updatePosition() {
        if (position.getX() > 0 && position.getY() < GameSettings.GAME_HEIGHT - height) {
            position.moveToPosition(targetPosition, speedInPixel);
        }
    }

    /**
     * Renders {@code RadarTower} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addImageToCanvas("radartower.png", position.getX(), position.getY(), size, rotation);
    }

    /**
     * String representation of {@code RadarTower} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "RadarTower: " + position;
    }
}
