package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import java.awt.*;

/**
 * The {@code RandomBall} is a gameObject, not from Zaxxon.
 * It is just used to demonstrate movement in random Directions.
 */
public class RandomBall extends GameObject {
    private final RandomMovementPattern randomMovementPattern;
    private final QuadraticMovementPattern quadraticMovementPattern;

    /**
     * Initializes a {@code RandomBall} object.
     *
     * @param gameView to display the {@code RandomBall} object on
     */
    public RandomBall(GameView gameView) {
        super(gameView);

        speedInPixel = 4;
        rotation = 0;
        height = 50;
        width = 50;

        randomMovementPattern = new RandomMovementPattern();
        quadraticMovementPattern = new QuadraticMovementPattern();
        position.updateCoordinates(randomMovementPattern.startPosition());
        targetPosition.updateCoordinates(quadraticMovementPattern.nextPosition());
    }

    /**
     * Moves {@code RandomBall} object forward.
     */
    @Override
    public void updatePosition() {
        if (gameView.timer(1000, 4000, this)) {
            position.moveToPosition(targetPosition, speedInPixel);
        }

        if (position.similarTo(targetPosition)) {
            targetPosition.updateCoordinates(quadraticMovementPattern.nextPosition());
        }

        if (gameView.timer(3000, 0, this)) {
            speedInPixel++;
        }
    }

    /**
     * Renders {@code RandomBall} object as shape on {@code gameView}.
     *
     * @see GameView#addOvalToCanvas
     */
    @Override
    public void addToCanvas() {
        if (gameView.gameTimeInMilliseconds() <= 5000) {
            gameView.addOvalToCanvas(position.getX(), position.getY(), width, height, 1, true, Color.YELLOW);
        } else {
            gameView.addOvalToCanvas(position.getX(), position.getY(), width, height, 1, true, Color.RED);
        }

        gameView.addOvalToCanvas(targetPosition.getX(), targetPosition.getY(), width, height, 1, false, Color.WHITE);
    }

    /**
     * String representation of {@code RandomBall} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "SatelliteDish: " + position;
    }
}
