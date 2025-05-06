package thd.gameobjects.base;

import java.util.Random;

/**
 * Serves as a Base-Class for various MovementPatterns.
 */
public class MovementPattern {
    protected final Random random;

    protected MovementPattern() {
        random = new Random();
    }

    protected Position startPosition() {
        return new Position(0, 0);
    }

    protected Position nextPosition() {
        return new Position(0, 0);
    }
}
