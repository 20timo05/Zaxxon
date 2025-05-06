package thd.gameobjects.base;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;

import java.awt.*;

/**
 * Represents an object in the game.
 */
public class GameObject {

    protected final GameView gameView;
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
     * @param gameView GameView to show the game object on.
     */
    public GameObject(GameView gameView) {
        this.gameView = gameView;
        position = new Position();
        targetPosition = new Position();
        size = 1;
        speedInPixel = GameSettings.SPEED_IN_PIXEL;
    }

    /**
     * Updates the position of the game object.
     */
    public void updatePosition() {
    }

    /**
     * Draws the game object to the canvas.
     */
    public void addToCanvas() {
    }

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

    /**
     * <h1>FOR DEBUGGING PURPOSES ONLY</h1>
     * Draws the Hitbox of all GameObjects based on {@code height}, {@code width} and {@code size}.
     */
    public void drawHitbox() {
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width*size, height*size, 1, false, Color.blue);
    }

    /**
     * Returns width of game object.
     *
     * @return Width of game object
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns height of game object.
     *
     * @return Height of game object
     */
    public double getHeight() {
        return height;
    }
}