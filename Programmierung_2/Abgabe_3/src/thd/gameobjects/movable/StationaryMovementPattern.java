package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.MovementPattern;
import thd.gameobjects.base.Position;

class StationaryMovementPattern extends MovementPattern {

    private final Position[] pattern;
    private int currentIndex;

    StationaryMovementPattern() {
        super();

        // generate random number between 0 and 0.66*GameView.HEIGHT
        double movementAngle = Math.toRadians(15);
        int startYPosition = (int) (Math.random() * (0.66 * GameView.HEIGHT));
        int targetYPosition = (int) (startYPosition + Math.tan(movementAngle) * GameView.WIDTH);

        pattern = new Position[]{
                new Position(GameView.WIDTH, startYPosition),
                new Position(0, targetYPosition)
        };
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
