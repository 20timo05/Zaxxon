package thd.gameobjects.base;

import thd.game.managers.GameSettings;
import thd.game.utilities.GeometricUtils;

import thd.game.utilities.TravelPathCalculator;

class StationaryMovementPattern extends MovementPattern {

    private final GeometricUtils geometricUtils;
    private final Position[] pattern;
    private int currentIndex;

    StationaryMovementPattern() {
        this(0, Math.random());
    }

    StationaryMovementPattern(double spwanLineInter) {
        this(0, spwanLineInter);
    }

    StationaryMovementPattern(double distanceFromSpawnLine, double spawnLineInter) {
        super();
        geometricUtils = new GeometricUtils();

        Position spawnLinePosition = geometricUtils.interpolatePosition(
                TravelPathCalculator.getSpawnLine()[0],
                TravelPathCalculator.getSpawnLine()[1],
                spawnLineInter
        );

        Vector2d startPosition = new Vector2d(spawnLinePosition);
        Vector2d spawnLineOffset = new Vector2d(distanceFromSpawnLine, GameSettings.MOVEMENT_ANGLE_IN_RADIANS);
        startPosition.add(spawnLineOffset);


        Position endPosition = geometricUtils.interpolatePosition(
                TravelPathCalculator.getDespawnLine()[0],
                TravelPathCalculator.getDespawnLine()[1],
                spawnLineInter
        );

        pattern = new Position[]{startPosition, endPosition};
    }


    @Override
    protected Position nextPosition() {
        if (currentIndex >= pattern.length) {
            currentIndex = 0;
        }
        return pattern[currentIndex++];
    }

    @Override
    protected Position startPosition() {
        currentIndex = 0;
        return nextPosition();
    }
}
