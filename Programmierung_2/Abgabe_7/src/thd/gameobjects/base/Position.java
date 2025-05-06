package thd.gameobjects.base;

import thd.game.utilities.GameView;

import java.util.Objects;

/**
 * This class is a Wrapper for X- & Y-Coordinate with various utility methods.
 * It can be used for every GameObject.
 * For Position to be visible, keep inside Dimensions of {@link GameView}.
 *
 * @see GameView
 */
public class Position {

    private double x;
    private double y;

    /**
     * Creates a position on (0, 0).
     */
    public Position() {
        this(0, 0);
    }

    /**
     * Creates a position with the coordinates of the given position.
     *
     * @param other Another position.
     */
    public Position(Position other) {
        this(other.getX(), other.getY());
    }

    /**
     * Creates a position on (x, y).
     *
     * @param x X-coordinate on the window.
     * @param y Y-coordinate on the window.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates Euclidean Distance between two {@code Position} objects.
     *
     * @param other {@code Position} object to calculate distance to
     * @return euclidean distance
     */
    public double distance(Position other) {
        return Math.sqrt(Math.pow((other.getX() - x), 2) + Math.pow((other.getY() - y), 2));
    }

    /**
     * Moves towards the given position with the given speed.
     *
     * @param other        Another position.
     * @param speedInPixel Speed of movement in a single frame.
     */
    public void moveToPosition(Position other, double speedInPixel) {
        double distance = distance(other);
        if (distance <= speedInPixel) {
            updateCoordinates(other);
        } else {
            right((other.x - x) / distance * speedInPixel);
            down((other.y - y) / distance * speedInPixel);
        }
    }

    /**
     * Checks if this position is similar to the other position.
     *
     * @param other Another position.
     * @return True if this position has the same x- and y-coordinates as the other position, when both are rounded to <code>int</code>.
     */
    public boolean similarTo(Position other) {
        return Math.round(x) == Math.round(other.x)
                && Math.round(y) == Math.round(other.y);
    }

    /**
     * Gets x coordinate.
     *
     * @return x coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets y coordinate.
     *
     * @return y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Updates this position to the coordinates of the given position.
     *
     * @param other Another position.
     */
    public void updateCoordinates(Position other) {
        x = other.getX();
        y = other.getY();
    }

    /**
     * Updates this position to the coordinates of the new position.
     *
     * @param x X-coordinate on the window.
     * @param y Y-coordinate on the window.
     */
    public void updateCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * One pixel to the right.
     */
    public void right() {
        x++;
    }

    /**
     * To the right by the given number of pixels.
     *
     * @param pixel Number of pixels.
     */
    public void right(double pixel) {
        x += pixel;
    }

    /**
     * One pixel to the left.
     */
    public void left() {
        x--;
    }

    /**
     * To the left by the given number of pixels.
     *
     * @param pixel Number of pixels.
     */
    public void left(double pixel) {
        x -= pixel;
    }

    /**
     * One pixel upwards.
     */
    public void up() {
        y--;
    }

    /**
     * Upwards by the given number of pixels.
     *
     * @param pixel Number of pixels.
     */
    public void up(double pixel) {
        y -= pixel;
    }

    /**
     * One pixel downwards.
     */
    public void down() {
        y++;
    }

    /**
     * Downwards by the given number of pixels.
     *
     * @param pixel Number of pixels.
     */
    public void down(double pixel) {
        y += pixel;
    }

    @Override
    public String toString() {
        return "Position (" + (int) Math.round(x) + ", " + (int) Math.round(y) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Position other = (Position) o;
        return Double.compare(x, other.x) == 0 && Double.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}