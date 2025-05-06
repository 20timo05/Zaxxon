package thd.gameobjects.base;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.game.utilities.GeometricUtils;

/**
 * Represents an object in the game.
 */

public abstract class GameObject {

    protected final GameView gameView;
    protected final GamePlayManager gamePlayManager;
    protected final GeometricUtils geometricUtils;
    protected final Position position;
    protected final Position targetPosition;
    protected double speedInPixel;
    protected double rotation;
    protected double size;
    protected double width;
    protected double height;

    /**
     * Crates a new GameObject.
     *
     * @param gameView        GameView to show the game object on.
     * @param gamePlayManager reference to the gamePlayManager
     */
    public GameObject(GameView gameView, GamePlayManager gamePlayManager) {
        this.gameView = gameView;
        this.gamePlayManager = gamePlayManager;

        geometricUtils = new GeometricUtils();
        position = new Position();
        targetPosition = new Position();
        size = 1;
    }

    /**
     * Updates the position of the game object.
     */
    public void updatePosition() {
    }

    /**
     * Draws the game object to the canvas.
     */
    public abstract void addToCanvas();

    /**
     * Returns the current position of the game object.
     *
     * @return position of the game object.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the status of the {@code GameObject}.
     */
    public void updateStatus(){

    }

    /*
     * <h1>FOR DEBUGGING PURPOSES ONLY</h1>
     * Draws the Hitbox of all GameObjects based on {@code height}, {@code width} and {@code size}.
     *
     * public void drawHitbox() {
     *         try {
     *             gameView.addRectangleToCanvas(position.getX(), position.getY(), getWidth(), getHeight(), 1, false, Color.blue);
     *         } catch (IllegalArgumentException i){}
     *     }
     */

    /**
     * Returns width of game object.
     *
     * @return Width of game object
     */
    public double getWidth() {
        return width*size;
    }

    /**
     * Returns height of game object.
     *
     * @return Height of game object
     */
    public double getHeight() {
        return height*size;
    }

    /**
     * Calculates the middle Point of game object.
     *
     * @return Middle point of game object
     */
    public Position calcMiddlePoint() {
        return new Position(
                position.getX() - getWidth()/2,
                position.getY() - getHeight()/2
        );
    }


    /**
     * String representation of this {@code GameObject}.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + position;
    }
}