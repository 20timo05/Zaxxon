package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.Position;

/**
 * The {@code RadarTower} is a stationary GameObject that yields 1000 points upon destruction.
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 */
public class RadarTower {
    private final GameView gameView;
    private final Position position;
    private final double speedInPixel;
    private final double imageScaleFactor;
    private final double rotation;

    private final double height;
    private final double width;
    private final double size;

    /**
     * Initializes a {@code RadarTower} object.
     *
     * @param gameView to display the {@code RadarTower} object on
     */
    public RadarTower(GameView gameView) {
        this.gameView = gameView;
        position = new Position(1000, 300);
        speedInPixel = 0;
        imageScaleFactor = 0.5;
        rotation = 0;

        height = 0;
        width = 0;
        size = 0;
    }

    /**
     * Moves {@code RadarTower} object forward.
     */
    public void updatePosition() {
        position.left(speedInPixel);
    }

    /**
     * Renders {@code HeightStatusBar} object as .png Image on {@code gameView}.
     *
     * @see thd.game.utilities.GameView#addImageToCanvas
     */
    public void addToCanvas() {
        gameView.addImageToCanvas("satellitedish.png", position.getX(), position.getY(), imageScaleFactor, rotation);
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
