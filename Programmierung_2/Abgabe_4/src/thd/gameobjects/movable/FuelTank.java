package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code FuelTank} is a stationary GameObject that yields 300 points upon destruction. It also replenishes the Fuel Gauge.
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 *
 * @see GameObject
 */
public class FuelTank extends GameObject {
    private final StationaryMovementPattern stationaryMovementPattern;
    /**
     * Initializes a {@code FuelTank} object.
     *
     * @param gameView to display the {@code FuelTank} object on
     */
    public FuelTank(GameView gameView) {
        super(gameView);

        height = 130;
        width = 155;
        size = 0.5;

        stationaryMovementPattern = new StationaryMovementPattern();
        position.updateCoordinates(stationaryMovementPattern.startPosition());
        targetPosition.updateCoordinates(stationaryMovementPattern.nextPosition());
    }

    /**
     * Moves {@code FuelTank} object forward.
     */
    @Override
    public void updatePosition() {
        if (gameView.gameTimeInMilliseconds() > 1000 && position.getX() > 0 && position.getY() < GameSettings.GAME_HEIGHT - height) {
            position.moveToPosition(targetPosition, speedInPixel);
        }
    }

    /**
     * Renders {@code FuelTank} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addImageToCanvas("fuel.png", position.getX(), position.getY(), size, rotation);
    }

    /**
     * String representation of {@code FuelTank} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "Fuel: " + position;
    }
}
