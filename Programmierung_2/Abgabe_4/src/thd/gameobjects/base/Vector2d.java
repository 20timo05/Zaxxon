package thd.gameobjects.base;

import thd.game.utilities.GameView;

/**
 * A wrapper for a {@link Position} object.
 * {@code Vector2d} includes more Vector operations, like support for Polar Coordinates & Matrix Multiplications.
 *
 * @see Position
 */
public class Vector2d extends Position {
    /**
     * Initializes the Vector at (0, 0).
     *
     * @see Position
     */
    public Vector2d() {
        super();
    }

    /**
     * Creates a vector from magnitude and angle (polar coordinates).
     * This assumes a Coordinate System where the (0, 0) is in the top left corner.
     *
     * @param magnitude The length of the vector.
     * @param angle     The angle in radians measured from the X-axis.
     * @see Position
     * @see GameView
     */
    public Vector2d(double magnitude, double angle) {
        super(magnitude * Math.cos(angle), -magnitude * Math.sin(angle));
    }

    /**
     * Creates a {@code Vector2d} with the coordinates of the given {@code Position}.
     *
     * @param other Another {@code Position}
     * @see Position
     */
    public Vector2d(Position other) {
        super(other);
    }

    /**
     * Calculates the Magnitude of this {@code Vector2d} object.
     *
     * @return the magnitude
     */
    private double calcMagnitude() {
        return Math.hypot(getX(), getY());
    }

    /**
     * Scales this {@code Vector2d} object to the desired magnitude.
     *
     * @param magnitude the desired magnitude
     */
    public void scaleToMagnitude(double magnitude) {
        normalize();
        scalarMultiplication(magnitude);
    }

    /**
     * Calculates the angle to the X-Axis of this {@code Vector2d} object.
     *
     * @return the angle
     */
    private double calcAngle() {
        return Math.atan2(getY(), getX());
    }

    /**
     * Sets the Polar Coordinate Angle measured from the X-Axis.
     *
     * @param angle the angle in radians
     */
    private void rotateToAngle(double angle) {
        updateCoordinates(new Vector2d(calcMagnitude(), angle));
    }

    /**
     * Performs Vector Addition on this {@code Position} object.
     *
     * @param other Vector to be added
     */
    public void add(Position other) {
        right(other.getX());
        down(other.getY());
    }

    /**
     * Performs Vector Subtraction on this {@code Position} object.
     *
     * @param other Vector to be subtracted
     */
    private void subtract(Position other) {
        left(other.getX());
        up(other.getY());
    }


    /**
     * Performs Scalar Multiplication on this {@code Vector2d} object.
     *
     * @param scalar the scaling factor
     */
    private void scalarMultiplication(double scalar) {
        updateCoordinates(getX() * scalar, getY() * scalar);
    }

    /**
     * Normalizes this {@code Vector2d} to a magnitude of 1 while preserving the direction.
     */
    public void normalize() {
        double magnitude = calcMagnitude();

        if (magnitude != 0) {
            scalarMultiplication(1 / magnitude);
        }
    }

    /**
     * Projects this {@code Vector2d} to a new Position through a matrix multiplication.
     *
     * @param matrix the 2x2 matrix
     * @throws IllegalArgumentException if matrix is not of shape 2x2
     */
    public void matrixMultiplication(double[][] matrix) {
        if (!(matrix.length == 2 && matrix[0].length == 2 && matrix[1].length == 2)) {
            throw new IllegalArgumentException("Matrix has to be 2x2.");
        }

        double newX = matrix[0][0] * getX() + matrix[0][1] * getY();
        double newY = matrix[1][0] * getX() + matrix[1][1] * getY();

        updateCoordinates(newX, newY);
    }

    /**
     * Moves this {@code Position} object so that it is linearly interpolated between this {@code Vector} and the other Position.
     *
     * @param other the ending Position
     * @param inter the interpolation factor (between 0 and 1)
     */
    public void interpolatePosition(Position other, double inter) {
        Vector2d interpolatedVec = new Vector2d(other);
        interpolatedVec.subtract(this);
        interpolatedVec.scalarMultiplication(inter);
        add(interpolatedVec);
    }

    @Override
    public String toString() {
        return "Vector2d (" + (int) Math.round(getX()) + ", " + (int) Math.round(getY()) + ")";
    }
}
