package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * The {@code LaserShot} is a {@code GameObject} that is emitted from the {@link ZaxxonFighter} and can destroy enemy objects.
 * It can also be used to determine the relative position to other objects like {@link EnergyBarrierAnimation}s.
 *
 * @see GameObject
 */
public class ZaxxonFighterLaserShot extends SparklingExplosionGameObject {

    /**
     * Creates a new {@code PlayerLaserShot} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param startPosition     the current position of the {@link ZaxxonFighter}
     * @param altitudeLevel     the altitude level of the {@link ZaxxonFighter} when shooting
     */
    public ZaxxonFighterLaserShot(GameView gameView, GamePlayManager gamePlayManager, Position startPosition, int altitudeLevel) {
        super(gameView, gamePlayManager, altitudeLevel, true);

        height = 8;
        width = 16;
        size = 3;
        speedInPixel = 6;

        position.updateCoordinates(startPosition);

        Vector2d positionOnSpawnLine = new Vector2d(startPosition);
        positionOnSpawnLine.add(new Vector2d(TRAVEL_PATH_CALCULATOR.getDistancePlayerMovementToSpawnLine(), GameSettings.MOVEMENT_ANGLE_IN_RADIANS));
        targetPosition.updateCoordinates(positionOnSpawnLine);

        hitBoxOffsets(-width*size/2, -height*size/2, 0, 0);
    }

    /**
     * Moves this {@code PlayerLaserShot} object forward.
     */
    @Override
    public void updatePosition() {
        if (!hasDespawned) {
            position.moveToPosition(targetPosition, speedInPixel);

        } else {
            Vector2d newPosition = new Vector2d(position);
            newPosition.add(new Vector2d(-GameSettings.SPEED_IN_PIXEL, GameSettings.MOVEMENT_ANGLE_IN_RADIANS));
            position.updateCoordinates(newPosition);
        }
    }

    @Override
    public void updateStatus() {
        super.updateStatus();

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
        super.addToCanvas();

        if (!hasDespawned) {
            Position mid = calcMiddlePoint();
            gameView.addBlockImageToCanvas(ZaxxonFighterBlockImages.LASER, mid.getX(), mid.getY(), size, rotation);
        }
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (getAltitudeLevel() == other.getAltitudeLevel()) {
            if (
                    other instanceof EnemyShooter
                            || other instanceof FuelTank
                            || other instanceof GunEmplacement
                            || other instanceof RadarTower
            ) {
                gamePlayManager.destroyGameObject(this);
                hasDespawned = true;

            } else if (other instanceof WallRow || other instanceof EnergyBarrier) {
                hasDespawned = true;
            }
        }
    }
}
