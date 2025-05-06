package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code EnergyBarrierTower} appear in the first levels of the game. They are hazardous obstacles that the player must
 * avoid.
 * It is recommended to shoot at the {@code EnergyBarrierTower} in order to determine your position in relation to the Barrier.
 *
 * @see GameObject
 */
public class EnergyBarrierTower extends GameObject {
    private final StationaryMovementPattern stationaryMovementPattern;
    private final EnergyBarrier energyBarrier;

    /**
     * Initializes a {@code EnergyBarrierTower} object.
     *
     * @param gameView to display the {@code EnergyBarrierTower} object on
     */
    public EnergyBarrierTower(GameView gameView) {
        super(gameView);

        height = 50;
        width = 50;

        size = 3;

        stationaryMovementPattern = new StationaryMovementPattern(-0.22);
        position.updateCoordinates(stationaryMovementPattern.startPosition());
        targetPosition.updateCoordinates(stationaryMovementPattern.nextPosition());

        energyBarrier = new EnergyBarrier(gameView);
    }

    /**
     * Moves {@code EnergyBarrierTower} object forward.
     */
    @Override
    public void updatePosition() {
        if (position.getX() > 0 && position.getY() < GameSettings.GAME_HEIGHT - height) {
            position.moveToPosition(targetPosition, speedInPixel);
            energyBarrier.updatePosition(position);
        }
    }

    /**
     * Renders {@code EnergyBarrierTower} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addBlockImageToCanvas(EnergyBarrierBlockImages.ENERGY_TOWER, position.getX(), position.getY(), size, 0);
        energyBarrier.addToCanvas();
    }

    /**
     * String representation of {@code EnergyBarrierTower} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "EnergyBarrierTower: " + position;
    }
}
