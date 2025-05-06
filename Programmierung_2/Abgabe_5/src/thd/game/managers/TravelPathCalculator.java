package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;
import thd.gameobjects.movable.PlayerLaserShot;

/**
 * This {@code TravelPathCalculator} calculates many important values needed for the isometric Movement to work
 * based on  {@link GameView#HEIGHT}, {@link GameView#WIDTH} and {@link GameSettings#MOVEMENT_ANGLE_IN_RADIANS}.
 * For visualization refer to {@link thd.gameobjects.unmovable.DebuggingLines}.
 *
 * @see thd.gameobjects.unmovable.DebuggingLines
 */
public final class TravelPathCalculator {
    private final Position[] outerScreenEntry;
    private final Position[] outerScreenExit;

    private final double spawnLineOffset;

    private final Position[] spawnLine;
    private final Position[] despawnLine;

    private final double distanceToDespawnLine;
    private final double travelPathWidth;

    private final Position[] playerMovementLine;
    private final double distancePlayerMovementToSpawnLine;

    private final double[][] isometricProjectionMatrix;

    /**
     * Initializes a {@link TravelPathCalculator} object and calculates the values for all instance variables.
     */
    public TravelPathCalculator() {
        outerScreenEntry = calculateOuterScreenEntry();
        spawnLineOffset = calculateSpawnLineOffset();
        spawnLine = calculateSpawnLinePosition();
        outerScreenExit = calculateOuterScreenExit();
        despawnLine = calculateDespawnLinePosition();

        travelPathWidth = spawnLine[0].distance(spawnLine[1]);
        distanceToDespawnLine = spawnLine[0].distance(despawnLine[0]);

        playerMovementLine = calculatePlayerMovementLine();
        distancePlayerMovementToSpawnLine = spawnLine[0].distance(playerMovementLine[0]);

        isometricProjectionMatrix = new double[][]{
                {Math.cos(-Math.toRadians(90 - GameSettings.MOVEMENT_ANGLE_IN_DEGREE)), 0},
                {Math.sin(Math.toRadians(90 - GameSettings.MOVEMENT_ANGLE_IN_DEGREE)), -1}
        };
    }

    private double calculateSpawnLineOffset() {
        if (GameSettings.USE_DEBUG_SPAWN_OFFSET) {
            return -Math.cos(GameSettings.MOVEMENT_ANGLE_IN_RADIANS) * (GameView.WIDTH - outerScreenEntry[0].getX());
        } else {
            return GameSettings.DEFAULT_SPAWN_LINE_OFFSET;
        }
    }

    private Position[] calculateSpawnLinePosition() {
        // calculates Position so that GameObjects always spawn outside the visible GameView
        Position relativeStartPositionOfSpawnLine = computeApexCoordinatesFromHypotenuseAndAngle(
                GameView.WIDTH - outerScreenEntry[0].getX(),
                GameSettings.MOVEMENT_ANGLE_IN_RADIANS
        );
        Vector2d absoluteStartPositionOfSpawnLine = new Vector2d(new Position(
                outerScreenEntry[0].getX() + relativeStartPositionOfSpawnLine.getX(),
                -relativeStartPositionOfSpawnLine.getY()
        ));

        Position relativeEndPositionOfSpawnLine = computeApexCoordinatesFromHypotenuseAndAngle(
                outerScreenEntry[1].getY(),
                GameSettings.MOVEMENT_ANGLE_IN_RADIANS
        );
        Vector2d absoluteEndPositionOfSpawnLine = new Vector2d(new Position(
                GameView.WIDTH + relativeEndPositionOfSpawnLine.getY(),
                relativeEndPositionOfSpawnLine.getX()
        ));

        absoluteStartPositionOfSpawnLine.add(new Vector2d(spawnLineOffset, GameSettings.MOVEMENT_ANGLE_IN_RADIANS));
        absoluteEndPositionOfSpawnLine.add(new Vector2d(spawnLineOffset, GameSettings.MOVEMENT_ANGLE_IN_RADIANS));

        return new Position[]{absoluteStartPositionOfSpawnLine, absoluteEndPositionOfSpawnLine};
    }

    private Position[] calculateDespawnLinePosition() {
        Position relativeStartPositionOfDespawnLine = computeApexCoordinatesFromHypotenuseAndAngle(
                GameSettings.GAME_HEIGHT - outerScreenExit[0].getY(),
                GameSettings.MOVEMENT_ANGLE_IN_RADIANS
        );

        Vector2d absoluteStartPositionOfDespawnLine = new Vector2d(new Position(
                -relativeStartPositionOfDespawnLine.getY(),
                GameSettings.GAME_HEIGHT - relativeStartPositionOfDespawnLine.getX()
        ));

        Position relativeEndPositionOfDespawnLine = computeApexCoordinatesFromHypotenuseAndAngle(
                outerScreenExit[1].getX(),
                GameSettings.MOVEMENT_ANGLE_IN_RADIANS
        );

        Vector2d absoluteEndPositionOfDespawnLine = new Vector2d(new Position(
                outerScreenExit[1].getX() - relativeEndPositionOfDespawnLine.getX(),
                GameSettings.GAME_HEIGHT + relativeEndPositionOfDespawnLine.getY()
        ));

        absoluteStartPositionOfDespawnLine.add(new Vector2d(-spawnLineOffset, GameSettings.MOVEMENT_ANGLE_IN_RADIANS));
        absoluteEndPositionOfDespawnLine.add(new Vector2d(-spawnLineOffset, GameSettings.MOVEMENT_ANGLE_IN_RADIANS));

        return new Position[]{absoluteStartPositionOfDespawnLine, absoluteEndPositionOfDespawnLine};
    }

    private Position[] calculateOuterScreenEntry() {
        Position topSideEntry = new Position(
                GameView.WIDTH * GameSettings.SCREEN_ENTRY_FACTOR_TOP,
                0
        );
        Position rightSideEntry = new Position(
                GameView.WIDTH,
                GameView.HEIGHT * GameSettings.SCREEN_ENTRY_FACTOR_RIGHT
        );

        return new Position[]{topSideEntry, rightSideEntry};
    }

    private Position[] calculateOuterScreenExit() {
        Position leftSideExit = new Position(
                0,
                Math.tan(GameSettings.MOVEMENT_ANGLE_IN_RADIANS) * outerScreenEntry[0].getX()
        );

        Position bottomSideExit = new Position(
                GameView.WIDTH - (GameSettings.GAME_HEIGHT - outerScreenEntry[1].getY()) / Math.tan(GameSettings.MOVEMENT_ANGLE_IN_RADIANS),
                GameSettings.GAME_HEIGHT
        );

        return new Position[]{leftSideExit, bottomSideExit};
    }

    private Position computeApexCoordinatesFromHypotenuseAndAngle(double lengthHypotenuse, double angle) {
        double lengthAdjacent = Math.cos(angle) * lengthHypotenuse;
        double apexY = Math.sin(angle) * lengthAdjacent;
        double apexX = Math.sqrt(Math.pow(lengthAdjacent, 2) - Math.pow(apexY, 2));

        return new Position(apexX, apexY);
    }

    private Position[] calculatePlayerMovementLine() {
        Vector2d[] absolutePositionOfPlayerMovementLine = {
                new Vector2d(spawnLine[0]),
                new Vector2d(spawnLine[1])
        };
        Vector2d playerMovementLineOffset = new Vector2d(
                -GameSettings.PLAYER_MOVEMENT_LINE_INTERPOLATION_FACTOR * distanceToDespawnLine,
                GameSettings.MOVEMENT_ANGLE_IN_RADIANS
        );

        absolutePositionOfPlayerMovementLine[0].add(playerMovementLineOffset);
        absolutePositionOfPlayerMovementLine[1].add(playerMovementLineOffset);

        return absolutePositionOfPlayerMovementLine;
    }

    /**
     * Returns the Coordinates where the Travel Path enter the Screen, which therefore mark the boundaries for the GameObjects.
     *
     * @return a shallow copy of the OuterScreenEntry coordinates
     * @see TravelPathCalculator#getOuterScreenExit()
     */
    public Position[] getOuterScreenEntry() {
        return new Position[]{new Position(outerScreenEntry[0]), new Position(outerScreenEntry[1])};
    }

    /**
     * Returns the Coordinates where the Travel Path leave the Screen, which therefore mark the boundaries for the GameObjects.
     *
     * @return a copy of the OuterScreenEntry coordinates
     * @see TravelPathCalculator#getOuterScreenEntry()
     */
    public Position[] getOuterScreenExit() {
        return new Position[]{new Position(outerScreenExit[0]), new Position(outerScreenExit[1])};
    }

    /*
    /**
     * By default, the Spawn Line connects the {@link TravelPathCalculator#outerScreenEntry} coordinates.
     * The Spawn Line is then moved outside the visible GameView, so that any {@link GameObject} spawns outside the visible area.
     * The same offset is applied to the Despawn Line.
     *
     * @return offset from default position
     * @see TravelPathCalculator#outerScreenEntry
     * @see TravelPathCalculator#spawnLine
     * @see TravelPathCalculator#despawnLine
     * @see GameObject
     * @see GameView
     *
     * public double getSpawnLineOffset() {
     *     return spawnLineOffset;
     * }
     */


    /**
     * The Spawn Line is outside the visible {@link GameView}.
     * Any {@link GameObject} spawns here and moves towards the {@link TravelPathCalculator#despawnLine}.
     *
     * @return a copy of the {@link TravelPathCalculator#spawnLine} Position Array
     * @see TravelPathCalculator#getDespawnLine()
     */
    public Position[] getSpawnLine() {
        return new Position[]{new Position(spawnLine[0]), new Position(spawnLine[1])};
    }

    /**
     * The Despawn Line is outside the visible {@link GameView}.
     * Once any {@link GameObject} has reached this line, it will be despawned..
     *
     * @return a copy of the {@link TravelPathCalculator#despawnLine} Position Array
     * @see TravelPathCalculator#getSpawnLine()
     */
    public Position[] getDespawnLine() {
        return new Position[]{new Position(despawnLine[0]), new Position(despawnLine[1])};
    }

    /**
     * This is the distance from {@link TravelPathCalculator#spawnLine} to {@link TravelPathCalculator#despawnLine}.
     * This is equivalent to the length of the Travel Path for any {@link GameObject}.
     *
     * @return the length of the Travel Path
     */
    public double getDistanceToDespawnLine() {
        return distanceToDespawnLine;
    }

    /**
     * Returns the distance from one side of the Travel Path to the other.
     * This is equivalent to the maximum horizontal distance between any two GameObjects.
     *
     * @return the width of the Travel Path
     */
    public double getTravelPathWidth() {
        return travelPathWidth;
    }

    /**
     * When the Player moves horizontally, he will move on this line.
     *
     * @return the player movement line
     */
    public Position[] getPlayerMovementLine() {
        return new Position[]{new Position(playerMovementLine[0]), new Position(playerMovementLine[1])};
    }

    /**
     * Returns the distance from the Player to the Spawn Line. This may be used as the length of the path for the
     * {@link PlayerLaserShot}.
     *
     * @return the distance from the Player to the Spawn Line
     */
    public double getDistancePlayerMovementToSpawnLine() {
        return distancePlayerMovementToSpawnLine;
    }

    /**
     * The game employs isometric Movement, simulating a 3d environment.
     * This Matrix can project any point from 2d to isometric 3d.
     *
     * @return the Projection Matrix
     * @see Vector2d#matrixMultiplication(double[][])
     */
    public double[][] getIsometricProjectionMatrix() {
        return new double[][]{
                {isometricProjectionMatrix[0][0], isometricProjectionMatrix[0][1]},
                {isometricProjectionMatrix[1][0], isometricProjectionMatrix[1][1]}
        };
    }
}
