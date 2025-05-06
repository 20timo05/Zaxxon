package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

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
class EnergyBarrier extends GameObject {
    private double barrierInter;
    private final Vector2d offsetTower;
    /**
     * Initializes a {@code EnergyBarrier} object.
     *
     * @param gameView to display the {@code EnergyBarrier} object on
     */
    EnergyBarrier(GameView gameView) {
        super(gameView);

        height = 50;
        width = 50;
        size = 5;

        barrierInter = 0.3;
        offsetTower = new Vector2d(TRAVEL_PATH_CALCULATOR.getTravelPathWidth() * barrierInter, Math.toRadians(GameSettings.MOVEMENT_ANGLE_IN_DEGREE - 90));
    }

    /**
     * Moves {@code EnergyBarrier} object forward.
     *
     * @param energyBarrierTowerPosition the position of the corresponding {@see EnergyBarrierTower}
     *
     * @see EnergyBarrierTower
     */
    void updatePosition(Position energyBarrierTowerPosition) {
        if (position.getX() >= 0 && position.getY() <= GameSettings.GAME_HEIGHT - height) {
            offsetTower.scaleToMagnitude(TRAVEL_PATH_CALCULATOR.getTravelPathWidth() * barrierInter);
            position.updateCoordinates(
                    energyBarrierTowerPosition.getX() + offsetTower.getX(),
                    energyBarrierTowerPosition.getY() + offsetTower.getY()
            );
        }
    }

    /**
     * Renders {@code EnergyBarrier} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addBlockImageToCanvas(EnergyBarrierBlockImages.ENERGY_BARRIER, position.getX(), position.getY(), size, 0);
    }

    /**
     * String representation of {@code EnergyBarrier} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "EnergyBarrier: " + position;
    }
}
