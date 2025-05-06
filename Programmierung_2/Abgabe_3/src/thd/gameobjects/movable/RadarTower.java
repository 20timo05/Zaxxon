package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code RadarTower} is a stationary GameObject that yields 1000 points upon destruction.
 * They will appear in the Motherbase as passive objects that does not shoot or move.
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

        speedInPixel = 2;
        rotation = 0;
        height = 50;
        width = 50;
        size = 0;

        stationaryMovementPattern = new StationaryMovementPattern();
        position.updateCoordinates(stationaryMovementPattern.startPosition());
        targetPosition.updateCoordinates(stationaryMovementPattern.nextPosition());
    }

    /**
     * Moves {@code RadarTower} object forward.
     */
    @Override
    public void updatePosition() {
        if (position.getY() < GameView.HEIGHT - height) {
            position.moveToPosition(targetPosition, speedInPixel);
        }
    }

    /**
     * Renders {@code HeightStatusBar} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addImageToCanvas("satellitedish.png", position.getX(), position.getY(), 0.5, rotation);
    }

    /**
     * String representation of {@code RadarTower} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "SatelliteDish: " + position;
    }
}
