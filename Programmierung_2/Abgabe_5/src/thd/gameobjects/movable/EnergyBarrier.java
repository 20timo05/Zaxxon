package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.StationaryGameObject;
import thd.gameobjects.base.Vector2d;

import java.awt.*;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * The {@code EnergyBarrier} appear in the first levels of the game. They are hazardous obstacles that the player must
 * avoid.
 * It is recommended to shoot at the {@code EnergyBarrier} in order to determine your position in relation to the Barrier.
 * This is only the actual Barrier, not including the {@link EnergyBarrierTower}.
 *
 * @see GameObject
 * @see EnergyBarrierTower
 */
public class EnergyBarrier extends StationaryGameObject {
    private final Vector2d offsetTower;
    private final Position[] startPath;

    private final double altitudeInterpolation;
    private double movementInterpolation;

    private final double barrierSpeedInPixel;

    /**
     * Creates a new {@code EnergyBarrier} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     */
    public EnergyBarrier(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager, 0.1);

        height = 21;
        width = 16;
        size = 5;
        barrierSpeedInPixel = 50;

        movementInterpolation = inter;
        altitudeInterpolation = 1;

        startPath = new Position[] {
                TRAVEL_PATH_CALCULATOR.getSpawnLine()[0],
                TRAVEL_PATH_CALCULATOR.getDespawnLine()[0]
        };

        offsetTower = new Vector2d(TRAVEL_PATH_CALCULATOR.getTravelPathWidth() * movementInterpolation, Math.toRadians(GameSettings.MOVEMENT_ANGLE_IN_DEGREE - 90));
    }

    @Override
    public void updateStatus() {
        super.updateStatus();
        movementInterpolation = (movementInterpolation + barrierSpeedInPixel / TRAVEL_PATH_CALCULATOR.getTravelPathWidth()) % 1;
    }

    /**
     * Moves {@code EnergyBarrier} object forward.
     *
     * @see EnergyBarrierTower
     */
    @Override
    public void updatePosition() {
        offsetTower.scaleToMagnitude(TRAVEL_PATH_CALCULATOR.getTravelPathWidth() * movementInterpolation);

        Vector2d newPosition = new Vector2d(
                geometricUtils.interpolatePosition(startPath[0], startPath[1], calcInterpolation())
        );
        newPosition.add(offsetTower);
        newPosition.up(altitudeInterpolation*GameSettings.MAX_PLAYER_ALTITUDE);

        position.updateCoordinates(newPosition);

        Vector2d newTargetPosition = new Vector2d(startPath[1]);
        newTargetPosition.add(offsetTower);
        newTargetPosition.up(altitudeInterpolation*GameSettings.MAX_PLAYER_ALTITUDE);
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
        gameView.addBlockImageToCanvas(EnergyBarrierBlockImages.ENERGY_BARRIER, position.getX(), position.getY()-getHeight(), size, 0);
    }

    /*
    @Override
    public void drawHitbox() {
        gameView.addRectangleToCanvas(position.getX(), position.getY()-getHeight(), getWidth(), getHeight(), 1, false, Color.blue);
    }
     */
}
