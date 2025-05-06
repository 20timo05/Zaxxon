package thd.gameobjects.movable;

import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

class StationaryMovementPattern extends MovementPattern {

    private final Position[] pattern;
    private int currentIndex;

    StationaryMovementPattern() {
        this(Math.random());
    }

    StationaryMovementPattern(double inter) {
        super();


        Vector2d startPosition = new Vector2d(TRAVEL_PATH_CALCULATOR.getSpawnLine()[0]);
        startPosition.interpolatePosition(TRAVEL_PATH_CALCULATOR.getSpawnLine()[1], inter);

        Vector2d endPosition = new Vector2d(TRAVEL_PATH_CALCULATOR.getDespawnLine()[0]);
        endPosition.interpolatePosition(TRAVEL_PATH_CALCULATOR.getDespawnLine()[1], inter);

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
