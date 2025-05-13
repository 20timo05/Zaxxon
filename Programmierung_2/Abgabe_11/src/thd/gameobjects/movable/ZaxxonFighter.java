package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.managers.GamePlayManager;
import thd.game.utilities.DynamicZIndexGameObject;
import thd.game.utilities.GameView;
import thd.game.utilities.GeometricUtils;
import thd.game.utilities.TravelPathCalculator;
import thd.gameobjects.base.*;

/**
 * The {@code ZaxxonFighter} can be controlled by the Player.
 * Handles isometric movement (left, right, up, down). It can also shoot at oncoming GameObjects in order to get Points &
 * Fuel. It has to avoid obstacles and enemies.
 *
 * @see GameObject
 */
public class ZaxxonFighter extends CollidingGameObject implements MainCharacter, DynamicZIndexGameObject {
    private final Vector2d movementVector;
    private final Position preProjectionPosition;
    private final Vector2d projectedPosition;
    private final Position shadowPosition;

    private double altitudeInterpolation;
    private double movementInterpolation;

    private static final int SHOT_DURATION_IN_MILLISECONDS = 300;

    private final int shadowWidth;
    private final int shadowHeight;

    private enum State {UP, STRAIGHT, DOWN};
    private State currentState;
    private String blockImage;

    /**
     * Creates a new {@code ZaxxonFighter} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     */
    public ZaxxonFighter(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager, 0, true);

        speedInPixel = 3;
        size = 4;
        height = 13;
        width = 24;

        movementVector = new Vector2d();
        preProjectionPosition = new Position(TravelPathCalculator.TRAVEL_PATH_WIDTH/2.0, GameSettings.MAX_PLAYER_ALTITUDE/2.0);
        projectedPosition = new Vector2d();
        shadowPosition = new Position();

        shadowWidth = 22;
        shadowHeight = 14;

        currentState = State.STRAIGHT;
        blockImage = ZaxxonFighterBlockImages.ZAXXON_FIGHTER_STRAIGHT;

        hitBoxOffsets(-width*size/4, -height*size/4, -width*size/2, -width*size/4);
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (getAltitudeLevel() == other.getAltitudeLevel()) {
            if (
                    other instanceof EnemyShooter
                            || other instanceof EnergyBarrier
                            || other instanceof FuelTank
                            || other instanceof GunEmplacement
                            || other instanceof GunEmplacementLaserShot
                            || other instanceof RadarTower
                            || other instanceof WallRow
                            || other instanceof VerticalRocket
            ) {
                gamePlayManager.destroyGameObject(this);
                gamePlayManager.lifeLost();
            }
        }
    }

    @Override
    public void updatePosition() {
        clipMovementVector(movementVector);
        movementVector.normalize(); // normalize, so velocity is same in all directions

        currentState = (movementVector.getY() < 0)
                ? State.UP
                : (movementVector.getY() == 0)
                ? State.STRAIGHT
                : State.DOWN;

        preProjectionPosition.right(movementVector.getX() * speedInPixel);
        preProjectionPosition.up(movementVector.getY() * speedInPixel);

        // projection from 2d to isometric "3d" Position
        projectedPosition.updateCoordinates(preProjectionPosition);
        projectedPosition.matrixMultiplication(TravelPathCalculator.copyIsometricProjectionMatrix());
        projectedPosition.add(TravelPathCalculator.copyPlayerMovementLine()[0]);

        position.updateCoordinates(projectedPosition);
        movementVector.updateCoordinates(0, 0);

        movementInterpolation = preProjectionPosition.getX() / TravelPathCalculator.TRAVEL_PATH_WIDTH;
        altitudeInterpolation = preProjectionPosition.getY() / (GameSettings.MAX_PLAYER_ALTITUDE);
        altitudeLevel = (int) (altitudeInterpolation * MAX_ALTITUDE_LEVEL);
        distanceToBackground = (char) (altitudeLevel + 5);

        // calculate position of the shadow below the player
        shadowPosition.updateCoordinates(GeometricUtils.interpolatePosition(
                TravelPathCalculator.copyPlayerMovementLine()[0],
                TravelPathCalculator.copyPlayerMovementLine()[1],
                movementInterpolation
        ));
        shadowPosition.left(shadowWidth*size/2);
        shadowPosition.up(shadowHeight*size/2);
    }

    /**
     * Renders {@code ZaxxonFighter} object as a BlockImage on {@code gameView}.
     * It also renders the Shadow of the {@code ZaxxonFighter} on the floor.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();

        gameView.addBlockImageToCanvas(ZaxxonFighterBlockImages.ZAXXON_FIGHTER_SHADOW, shadowPosition.getX(), shadowPosition.getY(), size, 0);
        gameView.addBlockImageToCanvas(blockImage, mid.getX(), mid.getY(), size, 0);
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
     * Shoots a {@link ZaxxonFighterLaserShot} straight ahead.
     *
     * @see ZaxxonFighterLaserShot
     */
    @Override
    public void shoot() {
        if (gameView.timer(SHOT_DURATION_IN_MILLISECONDS, Integer.MAX_VALUE, this)) {
            Vector2d laserShotPosition = new Vector2d(position);
            laserShotPosition.right(getWidth());
            laserShotPosition.up(getHeight() / 2);
            gamePlayManager.spawnGameObject(new ZaxxonFighterLaserShot(gameView, gamePlayManager, laserShotPosition, getAltitudeLevel()));
            gameView.resetTimers(this);

            gameView.playSound("playershoot.wav", false);

        }
    }

    @Override
    public void updateStatus() {
        if (hasDespawned) {
            gamePlayManager.lifeLost();
        }

        switch (currentState) {
            case UP:
                blockImage = ZaxxonFighterBlockImages.ZAXXON_FIGHTER_UP;
                break;
            case STRAIGHT:
                blockImage = ZaxxonFighterBlockImages.ZAXXON_FIGHTER_STRAIGHT;
                break;
            case DOWN:
                blockImage = ZaxxonFighterBlockImages.ZAXXON_FIGHTER_DOWN;
                break;
        }
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
                        || preProjectionPosition.getX() + movementVector.getX() >= TravelPathCalculator.TRAVEL_PATH_WIDTH
        ) {
            movementVector.updateCoordinates(0, movementVector.getY());
        }

        if (
                preProjectionPosition.getY() - movementVector.getY() <= 0
                        || preProjectionPosition.getY() - movementVector.getY() >= GameSettings.MAX_PLAYER_ALTITUDE
        ) {
            movementVector.updateCoordinates(movementVector.getX(), 0);
        }
    }
}
