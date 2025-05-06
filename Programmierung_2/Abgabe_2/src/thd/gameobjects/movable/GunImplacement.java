package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.Position;

/**
 * The {@code GunImplacement} is a stationary GameObject that yields 200 or 500 points (random) upon destruction.
 * They will appear in the Motherbase and shoot straight bullets at the player, but only when he is at ground level.
 */
public class GunImplacement {
    private final GameView gameView;
    private final Position position;
    private final double speedInPixel;
    private final double imageScaleFactor;
    private final double height;
    private final double width;
    private final double size;
    private double rotation;

    /**
     * Initializes a {@code GunImplacement} object.
     *
     * @param gameView to display the {@code GunImplacement} object on
     */
    public GunImplacement(GameView gameView) {
        this.gameView = gameView;
        position = new Position(0, GameView.HEIGHT / 2.0);
        speedInPixel = 5;
        imageScaleFactor = 0.5;
        rotation = 0;

        height = 0;
        width = 0;
        size = 0;
    }

    /**
     * Moves {@code GunImplacement} object forward.
     */
    public void updatePosition() {
        position.right(speedInPixel);
        rotation++;
    }

    /**
     * Renders {@code GunImplacement} object as .png Image on {@code gameView}.
     *
     * @see thd.game.utilities.GameView#addImageToCanvas
     */
    public void addToCanvas() {
        gameView.addImageToCanvas("tank.png", position.getX(), position.getY(), imageScaleFactor, rotation);
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
