package thd.game.utilities;

import thd.game.managers.GameSettings;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

/**
 * The {@code TravelPathCalculator} calculates many important values needed
 * for isometric movement based on {@link GameView#HEIGHT}, {@link GameView#WIDTH},
 * and {@link GameSettings#MOVEMENT_ANGLE_IN_RADIANS}.
 *
 * <p>Primitive and simple datatypes are exposed directly,
 * whereas reference and mutable types are accessed via getters (which return defensive copies)
 * to maintain immutability.</p>
 */
public final class TravelPathCalculator {

    // Private static variables for reference data types - ORDER IS CRUCIAL.
    private static final Position[] OUTER_SCREEN_ENTRY = calculateOuterScreenEntry();
    private static final double SPAWN_LINE_OFFSET = calculateSpawnLineOffset();
    private static final Position[] OUTER_SCREEN_EXIT = calculateOuterScreenExit();
    private static final Position[] SPAWN_LINE = calculateSpawnLinePosition();
    private static final Position[] DESPAWN_LINE = calculateDespawnLinePosition();
    private static final Position[] PLAYER_MOVEMENT_LINE = calculatePlayerMovementLine();

    // Public primitives (constants)
    /** The width of the designated travel path, measured along the spawn line. */
    public static final double TRAVEL_PATH_WIDTH = SPAWN_LINE[0].distance(SPAWN_LINE[1]);
    /** The longitudinal distance from the spawn line to the despawn line. */
    public static final double DISTANCE_TO_DESPAWN_LINE = SPAWN_LINE[0].distance(DESPAWN_LINE[0]);
    /** The distance from the spawn line to the player's designated movement line. */
    public static final double DISTANCE_PLAYER_MOVEMENT_TO_SPAWN_LINE = SPAWN_LINE[0].distance(PLAYER_MOVEMENT_LINE[0]);


    private static final double[][] ISOMETRIC_PROJECTION_MATRIX = new double[][]{
            {Math.cos(-GameSettings.MOVEMENT_ANGLE_IN_RADIANS), 0},
            {Math.sin(GameSettings.MOVEMENT_ANGLE_IN_RADIANS), -1}
    };
    private static final double[][] STRETCHED_ISOMETRIC_PROJECTION_MATRIX = new double[][]{
            {1, 0},
            {Math.tan(GameSettings.MOVEMENT_ANGLE_IN_RADIANS), -1}
    };

    // Private constructor to prevent instantiation.
    private TravelPathCalculator() {
    }

    // --- Getters for Reference Data Types (Defensive Copying) --- //

    /**
     * Returns a defensive copy of the outer screen entry coordinates.
     *
     * @return a new {@code Position[]} containing the outer screen entry positions.
     */
    public static Position[] copyOuterScreenEntry() {
        return new Position[]{
                new Position(OUTER_SCREEN_ENTRY[0]),
                new Position(OUTER_SCREEN_ENTRY[1])
        };
    }

    /**
     * Returns a defensive copy of the outer screen exit coordinates.
     *
     * @return a new {@code Position[]} containing the outer screen exit positions.
     */
    public static Position[] copyOuterScreenExit() {
        return new Position[]{
                new Position(OUTER_SCREEN_EXIT[0]),
                new Position(OUTER_SCREEN_EXIT[1])
        };
    }

    /**
     * Returns a defensive copy of the spawn line positions.
     *
     * @return a new {@code Position[]} containing the spawn line positions.
     */
    public static Position[] copySpawnLine() {
        return new Position[]{
                new Position(SPAWN_LINE[0]),
                new Position(SPAWN_LINE[1])
        };
    }

    /**
     * Returns a defensive copy of the despawn line positions.
     *
     * @return a new {@code Position[]} containing the despawn line positions.
     */
    public static Position[] copyDespawnLine() {
        return new Position[]{
                new Position(DESPAWN_LINE[0]),
                new Position(DESPAWN_LINE[1])
        };
    }

    /**
     * Returns a defensive copy of the player movement line positions.
     *
     * @return a new {@code Position[]} containing the player movement line positions.
     */
    public static Position[] copyPlayerMovementLine() {
        return new Position[]{
                new Position(PLAYER_MOVEMENT_LINE[0]),
                new Position(PLAYER_MOVEMENT_LINE[1])
        };
    }

    /**
     * Returns a deep copy of the isometric projection matrix.
     *
     * @return a new {@code double[][]} with the isometric projection matrix.
     */
    public static double[][] copyIsometricProjectionMatrix() {
        return new double[][]{
                {ISOMETRIC_PROJECTION_MATRIX[0][0], ISOMETRIC_PROJECTION_MATRIX[0][1]},
                {ISOMETRIC_PROJECTION_MATRIX[1][0], ISOMETRIC_PROJECTION_MATRIX[1][1]}
        };
    }

    /**
     * Returns a deep copy of the stretched isometric projection matrix.
     *
     * @return a new {@code double[][]} with the stretched isometric projection matrix.
     */
    public static double[][] copyStretchedIsometricProjectionMatrix() {
        return new double[][]{
                {
                        STRETCHED_ISOMETRIC_PROJECTION_MATRIX[0][0],
                        STRETCHED_ISOMETRIC_PROJECTION_MATRIX[0][1]
                },
                {
                        STRETCHED_ISOMETRIC_PROJECTION_MATRIX[1][0],
                        STRETCHED_ISOMETRIC_PROJECTION_MATRIX[1][1]
                }
        };
    }

    // --- Private utility methods for internal calculations --- //

    private static double calculateSpawnLineOffset() {
        if (GameSettings.USE_DEBUG_SPAWN_OFFSET) {
            return -Math.cos(GameSettings.MOVEMENT_ANGLE_IN_RADIANS)
                    * (GameView.WIDTH - OUTER_SCREEN_ENTRY[0].getX());
        } else {
            return GameSettings.DEFAULT_SPAWN_LINE_OFFSET;
        }
    }

    private static Position[] calculateSpawnLinePosition() {
        // Calculate the spawn line ensuring that objects always spawn off-screen.
        Position relativeStartPositionOfSpawnLine =
                computeApexCoordinatesFromIsosceles(
                        GameView.WIDTH - OUTER_SCREEN_ENTRY[0].getX(),
                        GameSettings.MOVEMENT_ANGLE_IN_RADIANS);

        Vector2d absoluteStartPositionOfSpawnLine =
                new Vector2d(new Position(
                        OUTER_SCREEN_ENTRY[0].getX()
                                + relativeStartPositionOfSpawnLine.getX(),
                        -relativeStartPositionOfSpawnLine.getY()));

        Position relativeEndPositionOfSpawnLine =
                computeApexCoordinatesFromIsosceles(
                        OUTER_SCREEN_ENTRY[1].getY(),
                        Math.toRadians(90 - GameSettings.MOVEMENT_ANGLE_IN_DEGREE));

        Vector2d absoluteEndPositionOfSpawnLine =
                new Vector2d(new Position(
                        GameView.WIDTH + relativeEndPositionOfSpawnLine.getY(),
                        relativeEndPositionOfSpawnLine.getX()));

        absoluteStartPositionOfSpawnLine.add(
                new Vector2d(SPAWN_LINE_OFFSET,
                        GameSettings.MOVEMENT_ANGLE_IN_RADIANS));
        absoluteEndPositionOfSpawnLine.add(
                new Vector2d(SPAWN_LINE_OFFSET,
                        GameSettings.MOVEMENT_ANGLE_IN_RADIANS));

        return new Position[]{
                absoluteStartPositionOfSpawnLine,
                absoluteEndPositionOfSpawnLine
        };
    }

    private static Position[] calculateDespawnLinePosition() {
        Position relativeStartPositionOfDespawnLine =
                computeApexCoordinatesFromIsosceles(
                        GameSettings.GAME_HEIGHT - OUTER_SCREEN_EXIT[0].getY(),
                        Math.toRadians(90 - GameSettings.MOVEMENT_ANGLE_IN_DEGREE));

        Vector2d absoluteStartPositionOfDespawnLine =
                new Vector2d(new Position(
                        -relativeStartPositionOfDespawnLine.getY(),
                        GameSettings.GAME_HEIGHT
                                - relativeStartPositionOfDespawnLine.getX()));

        Position relativeEndPositionOfDespawnLine =
                computeApexCoordinatesFromIsosceles(
                        OUTER_SCREEN_EXIT[1].getX(),
                        GameSettings.MOVEMENT_ANGLE_IN_RADIANS);

        Vector2d absoluteEndPositionOfDespawnLine =
                new Vector2d(new Position(
                        OUTER_SCREEN_EXIT[1].getX() - relativeEndPositionOfDespawnLine.getX(),
                        GameSettings.GAME_HEIGHT + relativeEndPositionOfDespawnLine.getY()));

        absoluteStartPositionOfDespawnLine.add(
                new Vector2d(-SPAWN_LINE_OFFSET,
                        GameSettings.MOVEMENT_ANGLE_IN_RADIANS));
        absoluteEndPositionOfDespawnLine.add(
                new Vector2d(-SPAWN_LINE_OFFSET,
                        GameSettings.MOVEMENT_ANGLE_IN_RADIANS));

        return new Position[]{
                absoluteStartPositionOfDespawnLine,
                absoluteEndPositionOfDespawnLine
        };
    }

    private static Position[] calculateOuterScreenEntry() {
        Position topSideEntry = new Position(
                GameView.WIDTH * GameSettings.SCREEN_ENTRY_FACTOR_TOP, 0);
        Position rightSideEntry = new Position(
                GameView.WIDTH, GameView.HEIGHT * GameSettings.SCREEN_ENTRY_FACTOR_RIGHT);
        return new Position[]{topSideEntry, rightSideEntry};
    }

    private static Position[] calculateOuterScreenExit() {
        Position leftSideExit = new Position(
                0,
                Math.tan(GameSettings.MOVEMENT_ANGLE_IN_RADIANS) * OUTER_SCREEN_ENTRY[0].getX());
        Position bottomSideExit = new Position(
                GameView.WIDTH
                        - (GameSettings.GAME_HEIGHT - OUTER_SCREEN_ENTRY[1].getY())
                        / Math.tan(GameSettings.MOVEMENT_ANGLE_IN_RADIANS),
                GameSettings.GAME_HEIGHT);
        return new Position[]{leftSideExit, bottomSideExit};
    }

    private static Position computeApexCoordinatesFromIsosceles(double lengthHypotenuse,
                                                                double angle) {
        double apexX = lengthHypotenuse / 2;
        double apexY = Math.tan(angle) * apexX;
        return new Position(apexX, apexY);
    }

    private static Position[] calculatePlayerMovementLine() {
        Vector2d[] absolutePositionOfPlayerMovementLine = {
                new Vector2d(SPAWN_LINE[0]),
                new Vector2d(SPAWN_LINE[1])
        };

        Vector2d playerMovementLineOffset = new Vector2d(
                -SPAWN_LINE[1].distance(OUTER_SCREEN_EXIT[1]) + 20,
                GameSettings.MOVEMENT_ANGLE_IN_RADIANS);
        absolutePositionOfPlayerMovementLine[0].add(playerMovementLineOffset);
        absolutePositionOfPlayerMovementLine[1].add(playerMovementLineOffset);

        return absolutePositionOfPlayerMovementLine;
    }
}