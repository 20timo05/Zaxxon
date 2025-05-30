package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

import static thd.game.managers.GameSettings.MAX_PLAYER_ALTITUDE;
import thd.game.utilities.TravelPathCalculator;

/**
 * The {@code EnergyBarrierAnimation} is only the yellow Grid, indicating the
 * position.
 * The actual Barrier is the corresponding Parten {@link EnergyBarrier}, which
 * actually is a {@link CollidingGameObject}.
 *
 * @see GameObject
 * @see EnergyBarrier
 */
class EnergyBarrierAnimation extends GameObject {
    private final Vector2d offsetTower;
    private final Position[] startPath;
    private final double barrierSpeedInPixel;
    private double movementInterpolation;
    private final int altitudeLevel;

    /**
     * Creates a new {@code EnergyBarrier} GameObject.
     *
     * @param gameView        GameView to show the game object on.
     * @param gamePlayManager reference to the gamePlayManager
     * @param spawnDelayInMilis      measure for how long before GameObject enters the
     *                        Screen
     * @param altitudeLevel   the altitude Level to display the yellow Grid at
     * @param dimensions      an array with height, width and size of the
     *                        BlockGraphic
     */
    EnergyBarrierAnimation(GameView gameView, GamePlayManager gamePlayManager, int spawnDelayInMilis, int altitudeLevel,
            double[] dimensions) {
        super(gameView, gamePlayManager, spawnDelayInMilis, 0);

        this.altitudeLevel = altitudeLevel;

        height = dimensions[0];
        width = dimensions[1];
        size = dimensions[2];

        barrierSpeedInPixel = 50;
        movementInterpolation = 0.1;

        startPath = new Position[] {
                TravelPathCalculator.copySpawnLine()[0],
                TravelPathCalculator.copyDespawnLine()[0]
        };

        offsetTower = new Vector2d(TravelPathCalculator.TRAVEL_PATH_WIDTH * movementInterpolation,
                -GameSettings.MOVEMENT_ANGLE_IN_RADIANS);
    }

    /**
     * Moves {@code EnergyBarrier} object forward, i.e. relative to the
     * corresponding {@link EnergyBarrier}.
     *
     * @param towerPosition the current position of the corresponding
     *                      {@link EnergyBarrier}
     * @see EnergyBarrier
     */
    void updatePosition(Position towerPosition) {
        movementInterpolation = (movementInterpolation
                + barrierSpeedInPixel / TravelPathCalculator.TRAVEL_PATH_WIDTH) % 1;

        offsetTower.scaleToMagnitude(TravelPathCalculator.TRAVEL_PATH_WIDTH * movementInterpolation);

        Vector2d newPosition = new Vector2d(towerPosition);
        newPosition.add(offsetTower);
        newPosition.up((double) altitudeLevel / CollidingGameObject.MAX_ALTITUDE_LEVEL * MAX_PLAYER_ALTITUDE);

        position.updateCoordinates(newPosition);

        Vector2d newTargetPosition = new Vector2d(startPath[1]);
        newTargetPosition.add(offsetTower);
        newTargetPosition.up(altitudeLevel * GameSettings.MAX_PLAYER_ALTITUDE);
        targetPosition.updateCoordinates(newTargetPosition);

        super.updatePosition();
    }

    /**
     * Renders {@code EnergyBarrier} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        gameView.addBlockImageToCanvas(EnergyBarrierBlockImages.ENERGY_BARRIER, mid.getX(), mid.getY(), size, 0);
    }
}
