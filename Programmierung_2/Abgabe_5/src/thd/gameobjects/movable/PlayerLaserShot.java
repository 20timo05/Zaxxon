package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * The {@code LaserShot} is a {@code GameObject} that is emitted from the {@link ZaxxonFighter} and can destroy enemy objects.
 * It can also be used to determine the relative position to other objects like {@link EnergyBarrier}s.
 *
 * @see GameObject
 */
public class PlayerLaserShot extends GameObject {

    /**
     * Creates a new {@code PlayerLaserShot} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param startPosition     the current position of the {@link ZaxxonFighter}
     */
    public PlayerLaserShot(GameView gameView, GamePlayManager gamePlayManager, Position startPosition) {
        super(gameView, gamePlayManager);

        height = 8;
        width = 16;
        size = 3;
        speedInPixel = 6;

        position.updateCoordinates(startPosition);

        Vector2d positionOnSpawnLine = new Vector2d(startPosition);
        positionOnSpawnLine.add(new Vector2d(TRAVEL_PATH_CALCULATOR.getDistancePlayerMovementToSpawnLine(), GameSettings.MOVEMENT_ANGLE_IN_RADIANS));
        targetPosition.updateCoordinates(positionOnSpawnLine);
    }

    /**
     * Moves this {@code PlayerLaserShot} object forward.
     */
    @Override
    public void updatePosition() {
        position.moveToPosition(targetPosition, speedInPixel);
    }

    @Override
    public void updateStatus() {
        if (position.similarTo(targetPosition)) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    /**
     * Renders {@code PlayerLaserShot} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addBlockImageToCanvas(PlayerLaserShotBlockImages.LASER, position.getX(), position.getY(), size, rotation);
    }
}
