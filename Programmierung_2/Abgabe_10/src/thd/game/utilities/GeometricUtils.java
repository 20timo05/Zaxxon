package thd.game.utilities;

import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

/**
 * A class that includes various utility methods for geometry, that every {@code GameObject} has access to.
 */
public final class GeometricUtils {

    /**
     * Calculate a linearly interpolated position between two {@code Position}s.
     *
     * @param start the starting Position
     * @param end   the ending Position
     * @param inter the interpolation factor (between 0 and 1)
     * @return      the interpolated position
     */
    public Position interpolatePosition(Position start, Position end, double inter) {
        double interX = (1 - inter) * start.getX() + inter * end.getX();
        double interY = (1 - inter) * start.getY() + inter * end.getY();

        return new Position(interX, interY);
    }


    /**
     * Calculates the Point of Intersection from two lines defined by two {@code Position} each.
     *
     * @param line1 an array with two {@code Position}s
     * @param line2 an array with two {@code Position}s
     * @return the point of intersection
     */
    private Position calculateIntersection(Position[] line1, Position[] line2) {
        return calculateIntersection(
                calculateParametricLine(line1[0], line1[1]),
                calculateParametricLine(line2[0], line2[1])
        );
    }

    /**
     * Calculates the Point of Intersection from two lines.
     * One is defined by two points, and the other by their Position and Direction Vector.
     *
     * @param line1 an array with two {@code Position}s for the first line
     * @param line2 an array of size 2 with position and direction vector for the second line
     * @return the point of intersection
     */
    public Position calculateIntersection(Position[] line1, Vector2d[] line2) {
        return calculateIntersection(
                calculateParametricLine(line1[0], line1[1]),
                line2
        );
    }

    /**
     * Calculates the Point of Intersection from two lines in parametric form .
     *
     * @param line1  an array of size 2 with position and direction vector for the first line
     * @param line2 an array of size 2 with position and direction vector for the second line
     * @return the point of intersection
     */
    private Position calculateIntersection(Vector2d[] line1, Vector2d[] line2) {
        // check direction vectors for linear dependence
        if (Math.abs(line1[1].crossProduct(line2[1])) < 1e-10) {
            return null;
        }

        double dx = line2[0].getX() - line1[0].getX();
        double dy = line2[0].getY() - line1[0].getY();

        double numerator = dx * (-line2[1].getY()) + dy * line2[1].getX();
        double denominator = line1[1].getX() * (-line2[1].getY()) + line1[1].getY() * line2[1].getX();

        double lambda1 = numerator / denominator;

        Vector2d intersection = new Vector2d(line1[1]);
        intersection.scalarMultiplication(lambda1);
        intersection.add(line1[0]);

        return new Position(intersection);
    }

    /**
     * Calculates the Position and Direction Vector for a 2d line from two points.
     *
     * @param pos1 first position
     * @param pos2 second position
     * @return an array of size 2 with position and direction vector
     */
    private Vector2d[] calculateParametricLine(Position pos1, Position pos2) {
        Vector2d positionVec = new Vector2d(pos1);
        Vector2d directionVec = new Vector2d(pos2);
        directionVec.subtract(positionVec);

        return new Vector2d[] {positionVec, directionVec};
    }
}
