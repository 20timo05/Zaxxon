package thd.gameobjects.base;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.game.utilities.GeometricUtils;

import java.util.Objects;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

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
    protected int spawnDelayInMilis;
    protected double spawnLineInter; // for stationary GameObjects
    protected boolean hasDespawned;
    protected char distanceToBackground;
    private StationaryMovementPattern stationaryMovementPattern;

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
        hasDespawned = false;
    }

    /**
     * Creates a new Stationary GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param spawnDelayInMilis measure for how long before GameObject enters the Screen
     * @param spawnLineInter    interpolation factor: where on the SpawnLine to spawn the object
     */
    public GameObject(GameView gameView, GamePlayManager gamePlayManager, int spawnDelayInMilis,
                      double spawnLineInter) {
        this(gameView, gamePlayManager);

        this.spawnDelayInMilis = spawnDelayInMilis;
        this.spawnLineInter = spawnLineInter;

        speedInPixel = GameSettings.SPEED_IN_PIXEL;

        stationaryMovementPattern = new StationaryMovementPattern(spawnLineInter);
        position.updateCoordinates(stationaryMovementPattern.startPosition());
        targetPosition.updateCoordinates(stationaryMovementPattern.nextPosition());

        distanceToBackground = 1;
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
     * Moves Shiftable Game Objects diagonally over the screen.
     *
     * @param pixels how far
     */
    public void moveShiftableForward(double pixels) {
        if (this instanceof ShiftableGameObject) {
            position.moveToPosition(targetPosition, pixels);
        }
    }

    /**
     * Sets the status of the {@code GameObject}.
     */
    public void updateStatus() {
        if (this instanceof ShiftableGameObject) {
            if (position.similarTo(targetPosition)) {
                gamePlayManager.destroyGameObject(this);
                hasDespawned = true;
            }
        }
    }

    /**
     * calculates how much percent of the way the GameObject is.
     *
     * @return the interpolation factor
     */
    public double calcInterpolation() {
        if (!(this instanceof ShiftableGameObject)) {
            throw new IllegalStateException("Only allowed for Stationary GameObjects!");
        }
        return 1 - position.distance(targetPosition) / TRAVEL_PATH_CALCULATOR.getDistanceToDespawnLine();
    }

    /**
     * Returns width of game object.
     *
     * @return Width of game object
     */
    public double getWidth() {
        return width * size;
    }

    /**
     * Returns height of game object.
     *
     * @return Height of game object
     */
    public double getHeight() {
        return height * size;
    }

    /**
     * Calculates the middle Point of game object.
     *
     * @return Middle point of game object
     */
    public Position calcMiddlePoint() {
        return new Position(
                position.getX() - getWidth() / 2,
                position.getY() - getHeight() / 2);
    }

    /**
     * Returns whether this {@code GameObject} has reached its target Position.
     *
     * @return boolean whether it should have despawned already
     */
    public boolean hasDespawned() {
        return hasDespawned;
    }

    /**
     * Returns the z-Index distance to the background.
     *
     * @return z-Index to background
     */
    public char getDistanceToBackground() {
        return distanceToBackground;
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameObject other = (GameObject) o;

        return position.equals(other.position)
                && targetPosition.equals(other.targetPosition)
                && Double.compare(speedInPixel, other.speedInPixel) == 0
                && Double.compare(rotation, other.rotation) == 0
                && Double.compare(size, other.size) == 0
                && Double.compare(width, other.width) == 0
                && Double.compare(height, other.height) == 0
                && Double.compare(spawnLineInter, other.spawnLineInter) == 0
                && hasDespawned == other.hasDespawned
                && distanceToBackground == other.distanceToBackground;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, targetPosition, speedInPixel, rotation, size, width, height, spawnLineInter,
                hasDespawned, distanceToBackground);
    }
}