package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.StationaryGameObject;

import java.awt.*;

/**
 * The {@code EnergyBarrierTower} appear in the first levels of the game. They are hazardous obstacles that the player must
 * avoid.
 * It is recommended to shoot at the {@code EnergyBarrierTower} in order to determine your position in relation to the Barrier.
 *
 * @see GameObject
 */
public class EnergyBarrierTower extends StationaryGameObject {

    /**
     * Creates a new {@EnergyBarrierTower} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     */
    public EnergyBarrierTower(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager, 0);

        height = 41;
        width = 14;
        size = 3;
    }

    /**
     * Renders {@code EnergyBarrierTower} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addBlockImageToCanvas(EnergyBarrierBlockImages.ENERGY_TOWER, position.getX(), position.getY()-getHeight(), size, 0);
    }

    /*
    @Override
    public void drawHitbox() {
        gameView.addRectangleToCanvas(position.getX(), position.getY()-getHeight(), getWidth(), getHeight(), 1, false, Color.blue);
    }
     */
}
