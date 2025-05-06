package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code EnemyShooter} is a stationary GameObject that yields 100 points upon destruction.
 * This is increases by 50 points each round of the game (motherbase, space combat, ...).
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 *
 * @see GameObject
 */
public class EnemyShooter extends GameObject {
    private final StationaryMovementPattern stationaryMovementPattern;
    /**
     * Initializes a {@code EnemyShooter} object.
     *
     * @param gameView to display the {@code EnemyShooter} object on
     */
    public EnemyShooter(GameView gameView) {
        super(gameView);

        height = 80;
        width = 127;
        size = 0.5;

        stationaryMovementPattern = new StationaryMovementPattern();
        position.updateCoordinates(stationaryMovementPattern.startPosition());
        targetPosition.updateCoordinates(stationaryMovementPattern.nextPosition());
    }

    /**
     * Moves {@code EnemyShooter} object forward.
     */
    @Override
    public void updatePosition() {
        if (gameView.gameTimeInMilliseconds() > 2000 && position.getX() > 0 && position.getY() < GameSettings.GAME_HEIGHT - height) {
            position.moveToPosition(targetPosition, speedInPixel);
        }
    }

    /**
     * Renders {@code EnemyShooter} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addImageToCanvas("enemyshooter.png", position.getX(), position.getY(), size, rotation);
    }

    /**
     * String representation of {@code EnemyShooter} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "EnemyShooter: " + position;
    }
}
