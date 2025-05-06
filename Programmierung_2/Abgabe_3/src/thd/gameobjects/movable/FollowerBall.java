package thd.gameobjects.movable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import java.awt.*;

/**
 * The {@code FollowerBall} is a gameObject, not from Zaxxon.
 * It is just used to demonstrate movement in random Directions.
 */
public class FollowerBall extends GameObject {
    private final RandomBall followMe;

    /**
     * Initializes a {@code FollowerBall} object.
     *
     * @param gameView to display the {@code FollowerBall} object on
     * @param followMe other {@code RandomBall} that this {@code FollowerBall} object should follow
     */
    public FollowerBall(GameView gameView, RandomBall followMe) {
        super(gameView);
        this.followMe = followMe;

        speedInPixel = 3;
        rotation = 0;
        height = 50;
        width = 50;

        position.updateCoordinates(new Position(0, 0));
        targetPosition.updateCoordinates(followMe.getPosition());
    }

    /**
     * Moves {@code FollowerBall} object forward.
     */
    @Override
    public void updatePosition() {
        targetPosition.updateCoordinates(followMe.getPosition());
        position.moveToPosition(targetPosition, speedInPixel);
    }

    /**
     * Renders {@code FollowerBall} object as shape on {@code gameView}.
     *
     * @see GameView#addOvalToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addOvalToCanvas(position.getX(), position.getY(), width, height, 1, true, Color.GREEN);
        gameView.addOvalToCanvas(targetPosition.getX(), targetPosition.getY(), width, height, 1, false, Color.WHITE);
    }

    /**
     * String representation of {@code FollowerBall} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "SatelliteDish: " + position;
    }
}
