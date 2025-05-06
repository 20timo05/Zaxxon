package thd.gameobjects.base;

import thd.game.utilities.GeometricUtils;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

class StationaryMovementPattern extends MovementPattern {

    private final GeometricUtils geometricUtils;
    private final Position[] pattern;
    private int currentIndex;

    StationaryMovementPattern() {
        this(Math.random());
    }

    StationaryMovementPattern(double inter) {
        super();
        geometricUtils = new GeometricUtils();

        Position startPosition = geometricUtils.interpolatePosition(
                TRAVEL_PATH_CALCULATOR.getSpawnLine()[0],
                TRAVEL_PATH_CALCULATOR.getSpawnLine()[1],
                inter
        );

        Position endPosition = geometricUtils.interpolatePosition(
                TRAVEL_PATH_CALCULATOR.getDespawnLine()[0],
                TRAVEL_PATH_CALCULATOR.getDespawnLine()[1],
                inter
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
