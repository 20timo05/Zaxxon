package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.MainCharacter;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * The {@code ZaxxonFighter} can be controlled by the Player.
 * Handles isometric movement (left, right, up, down). It can also shoot at oncoming GameObjects in order to get Points &
 * Fuel. It has to avoid obstacles and enemies.
 *
 * @see GameObject
 */
public class ZaxxonFighter extends GameObject implements MainCharacter {
    private final Vector2d movementVector;
    private final Position preProjectionPosition;
    private final Vector2d projectedPosition;

    private double altitudeInterpolation;
    private double movementInterpolation;

    private int shotDurationInMilliseconds;

    /**
     * Creates a new {@code ZaxxonFighter} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     */
    public ZaxxonFighter(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);

        speedInPixel = 3;
        size = 3;
        height = 13;
        width = 24;

        movementVector = new Vector2d();
        preProjectionPosition = new Position(0, 0);
        projectedPosition = new Vector2d();

        altitudeInterpolation = 0.5;
        movementInterpolation = 0.5;
        shotDurationInMilliseconds = 300;
    }

    @Override
    public void updatePosition() {
        clipMovementVector(movementVector);
        movementVector.normalize(); // normalize, so velocity is same in all directions

        preProjectionPosition.right(movementVector.getX() * speedInPixel);
        preProjectionPosition.up(movementVector.getY() * speedInPixel);

        // projection from 2d to isometric "3d" Position
        projectedPosition.updateCoordinates(preProjectionPosition);
        projectedPosition.matrixMultiplication(TRAVEL_PATH_CALCULATOR.getIsometricProjectionMatrix());
        projectedPosition.add(TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[0]);

        position.updateCoordinates(projectedPosition);
        movementVector.updateCoordinates(0, 0);

        altitudeInterpolation = preProjectionPosition.getY() / (GameSettings.MAX_PLAYER_ALTITUDE - height * size);
        movementInterpolation = preProjectionPosition.getX() / TRAVEL_PATH_CALCULATOR.getTravelPathWidth();
    }

    /**
     * Renders {@code ZaxxonFighter} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position shadowPosition = geometricUtils.interpolatePosition(
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[0],
                TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[1],
                movementInterpolation
        );

        gameView.addBlockImageToCanvas(ZaxxonFighterBlockImages.ZAXXON_FIGHTER_SHADOW, shadowPosition.getX(), shadowPosition.getY(), 3, 0);
        gameView.addBlockImageToCanvas(ZaxxonFighterBlockImages.ZAXXON_FIGHTER_STRAIGHT, position.getX(), position.getY(), size, 0);
    }

    /**
     * Moves the {@code ZaxxonFighter} left by {@link #speedInPixel}px.
     */
    public void left() {
        movementVector.left(speedInPixel);
    }

    /**
     * Moves the {@code ZaxxonFighter} right by {@link #speedInPixel}px.
     */
    public void right() {
        movementVector.right(speedInPixel);
    }

    /**
     * Moves the {@code ZaxxonFighter} up by {@link #speedInPixel}px.
     */
    public void up() {
        movementVector.up(speedInPixel);
    }

    /**
     * Moves the {@code ZaxxonFighter} down by {@link #speedInPixel}px.
     */
    public void down() {
        movementVector.down(speedInPixel);
    }

    /**
     * Shoots a {@link PlayerLaserShot} straight ahead.
     *
     * @see PlayerLaserShot
     */
    @Override
    public void shoot() {
        if (gameView.timer(300, Integer.MAX_VALUE, this)) {
            Vector2d laserShotPosition = new Vector2d(position);
            laserShotPosition.right(getWidth());
            laserShotPosition.up(getHeight() / 2);
            gamePlayManager.spawnGameObject(new PlayerLaserShot(gameView, gamePlayManager, laserShotPosition));
            gameView.resetTimers(this);
        }
    }

    @Override
    public void updateStatus() {

    }

    /**
     * Returns the linear interpolation factor of the height.
     * <ul>
     *     <li>0 corresponds to being in the lowest position</li>
     *     <li>1 corresponds to being in the highest position</li>
     * </ul>
     *
     * @return the linear interpolation factor
     */
    public double getAltitudeInterpolation() {
        return altitudeInterpolation;
    }

    private void clipMovementVector(Position movementVector) {
        // clip Movement, so that it Player stays inside valid Movement Range
        if (
                preProjectionPosition.getX() + movementVector.getX() <= 0
                        || preProjectionPosition.getX() + movementVector.getX() >= TRAVEL_PATH_CALCULATOR.getTravelPathWidth() - width * size
        ) {
            movementVector.updateCoordinates(0, movementVector.getY());
        }

        if (
                preProjectionPosition.getY() - movementVector.getY() <= 0
                        || preProjectionPosition.getY() - movementVector.getY() >= GameSettings.MAX_PLAYER_ALTITUDE - height * size
        ) {
            movementVector.updateCoordinates(movementVector.getX(), 0);
        }
    }
}
