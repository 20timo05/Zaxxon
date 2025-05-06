package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import java.awt.*;
import java.util.ArrayList;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * The {@code EnergyBarrier} appear in the first levels of the game. They are hazardous obstacles that the player must
 * avoid.
 * It is recommended to shoot at the {@code EnergyBarrier} in order to determine your position in relation to the Barrier.
 *
 * @see GameObject
 */
public class EnergyBarrier extends CollidingGameObject {
    private final EnergyBarrierAnimation energyBarrierAnimation;
    private static final double ENERGY_BARRIER_HEIGHT = 16;
    private static final double ENERGY_BARRIER_WIDTH = 21;
    private static final double ENERGY_BARRIER_SIZE = 5;

    private final ArrayList<CollidingGameObject> collidingGameObjectsForPathDecision;
    private final Position startPosition;

    private int stopCounter;

    /**
     * Creates a new {@code EnergyBarrier} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param altitudeLevel     the altitude that this {@code EnergyBarrier} was spawned at
     */
    public EnergyBarrier(GameView gameView, GamePlayManager gamePlayManager, int altitudeLevel) {
        super(gameView, gamePlayManager, 0, false, -0.1);

        height = 41;
        width = 14;
        size = 3;

        setRelativeHitboxPolygons(calculateHitbox());
        hitBoxOffsets(-20, -30, 0, 20);

        energyBarrierAnimation = new EnergyBarrierAnimation(gameView, gamePlayManager, altitudeLevel, new double[]{ENERGY_BARRIER_HEIGHT, ENERGY_BARRIER_WIDTH, ENERGY_BARRIER_SIZE});

        collidingGameObjectsForPathDecision = new ArrayList<>();
        startPosition = new Position(position);

        stopCounter = 0;
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        // cannot be destroyed
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
        energyBarrierAnimation.updatePosition(position);

        ArrayList<CollidingGameObject> objToRemove = new ArrayList<>();

        for (CollidingGameObject collidingGameObject : collidingGameObjectsForPathDecision) {
            if (collidingGameObject.hasDespawned()) {
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
     * Adds a {@link CollidingGameObject} to the Walls collidingGameObjectsForPathDecision, where I can't go through.
     *
     * @param obj the {@link CollidingGameObject} that the {@code EnergyBarrier} can't go through
     */
    public void addCollidingGameObjectForPathDecision(CollidingGameObject obj) {
        collidingGameObjectsForPathDecision.add(obj);
    }

    /**
     * Renders {@code EnergyBarrierTower} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        gameView.addBlockImageToCanvas(EnergyBarrierBlockImages.ENERGY_TOWER, mid.getX(), mid.getY(), size, 0);

        energyBarrierAnimation.addToCanvas();
    }

    private Polygon[] calculateHitbox() {
        Position[] preProjectionRelativeHitbox = new Position[]{
                new Position(0, 0),
                new Position(TRAVEL_PATH_CALCULATOR.getTravelPathWidth(), 0),
                new Position(TRAVEL_PATH_CALCULATOR.getTravelPathWidth(), -ENERGY_BARRIER_HEIGHT * ENERGY_BARRIER_SIZE),
                new Position(0, -ENERGY_BARRIER_HEIGHT * ENERGY_BARRIER_SIZE)
        };

        Polygon postProjectionHitbox = calculateRelativeProjectedHitbox(preProjectionRelativeHitbox, TRAVEL_PATH_CALCULATOR.getIsometricProjectionMatrix());
        return new Polygon[]{postProjectionHitbox};
    }
}
