package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

import java.awt.*;
import java.util.ArrayList;

import static thd.game.managers.GameSettings.MAX_PLAYER_ALTITUDE;
import thd.game.utilities.TravelPathCalculator;

/**
 * The {@code EnergyBarrier} appear in the first levels of the game. They are
 * hazardous obstacles that the player must
 * avoid.
 * It is recommended to shoot at the {@code EnergyBarrier} in order to determine
 * your position in relation to the Barrier.
 *
 * @see GameObject
 */
public class EnergyBarrier extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<Void> {
    private static final double ENERGY_BARRIER_HEIGHT = 16;
    private static final double ENERGY_BARRIER_WIDTH = 21;
    private static final double ENERGY_BARRIER_SIZE = 7;
    private final EnergyBarrierAnimation energyBarrierAnimation;
    private final ArrayList<CollidingGameObject> collidingGameObjectsForPathDecision;
    private final Position startPosition;

    private int stopCounter;
    private int soundId;

    /**
     * Creates a new {@code EnergyBarrier} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param spawnDelayInMilis measure for how long before GameObject enters the
     *                          Screen
     * @param altitudeLevel     the altitude that this {@code EnergyBarrier} was
     *                          spawned at
     */
    public EnergyBarrier(GameView gameView, GamePlayManager gamePlayManager, int spawnDelayInMilis, int altitudeLevel) {
        super(gameView, gamePlayManager, altitudeLevel, false, spawnDelayInMilis, -0.1);

        height = 41;
        width = 14;
        size = 4;

        setRelativeHitboxPolygons(calculateHitbox());
        hitBoxOffsets(-20, -30 - (double) altitudeLevel / MAX_ALTITUDE_LEVEL * MAX_PLAYER_ALTITUDE, 0, 20);

        energyBarrierAnimation = new EnergyBarrierAnimation(gameView, gamePlayManager, spawnDelayInMilis,
                altitudeLevel, new double[]{ENERGY_BARRIER_HEIGHT, ENERGY_BARRIER_WIDTH, ENERGY_BARRIER_SIZE});

        collidingGameObjectsForPathDecision = new ArrayList<>();
        startPosition = new Position(position);

        stopCounter = 0;
        distanceToBackground = 10;

        soundId = -1;

    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        // cannot be destroyed
    }

    @Override
    public void updateStatus() {
        if (soundId == -1) {
            soundId = gameView.playSound("energybarrier.wav", false);
        } else if (position.similarTo(targetPosition)) {
            gameView.stopSound(soundId);
        }

        super.updateStatus();
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
        energyBarrierAnimation.updatePosition(position);

        ArrayList<CollidingGameObject> objToRemove = new ArrayList<>();

        for (CollidingGameObject collidingGameObject : collidingGameObjectsForPathDecision) {
            if (collidingGameObject.hasDespawned) {
                objToRemove.add(collidingGameObject);

            } else if (collidesWith(collidingGameObject)) {
                stopCounter += 30;
                break;
            }
        }

        if (stopCounter > 0) {
            position.moveToPosition(startPosition, speedInPixel);
            energyBarrierAnimation.getPosition().moveToPosition(startPosition, speedInPixel);
            stopCounter--;
        }

        collidingGameObjectsForPathDecision.removeAll(objToRemove);
    }

    /**
     * Adds a {@link CollidingGameObject} to the Walls
     * collidingGameObjectsForPathDecision, where I can't go through.
     *
     * @param obj the {@link CollidingGameObject} that the {@code EnergyBarrier}
     *            can't go through
     */
    public void addCollidingGameObjectForPathDecision(CollidingGameObject obj) {
        collidingGameObjectsForPathDecision.add(obj);
    }

    /**
     * Renders {@code EnergyBarrierTower} object as a BlockImage on
     * {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        double verticalOffset = (double) altitudeLevel / MAX_ALTITUDE_LEVEL * MAX_PLAYER_ALTITUDE;
        gameView.addBlockImageToCanvas(EnergyBarrierBlockImages.ENERGY_TOWER, mid.getX(), mid.getY() - verticalOffset, size, 0);

        energyBarrierAnimation.addToCanvas();
    }

    /**
     * Activates the GameObject when it is ready to spawn.
     *
     * @param info no external information required, pass <code>null</code>
     * @return boolean whether object is ready
     */
    @Override
    public boolean tryToActivate(Void info) {
        return gameView.gameTimeInMilliseconds() > spawnDelayInMilis;
    }

    private Polygon[] calculateHitbox() {
        Position[] preProjectionRelativeHitbox = new Position[]{
                new Position(0, 0),
                new Position(TravelPathCalculator.TRAVEL_PATH_WIDTH, 0),
                new Position(TravelPathCalculator.TRAVEL_PATH_WIDTH, -ENERGY_BARRIER_HEIGHT * ENERGY_BARRIER_SIZE),
                new Position(0, -ENERGY_BARRIER_HEIGHT * ENERGY_BARRIER_SIZE)
        };

        Polygon postProjectionHitbox = calculateRelativeProjectedHitbox(preProjectionRelativeHitbox,
                TravelPathCalculator.copyStretchedIsometricProjectionMatrix());
        return new Polygon[]{postProjectionHitbox};
    }
}
