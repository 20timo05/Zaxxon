package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.StationaryGameObject;

/**
 * The {@code FuelTank} is a stationary GameObject that yields 300 points upon destruction. It also replenishes the Fuel Gauge.
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 *
 * @see GameObject
 */
public class FuelTank extends StationaryGameObject {

    /**
     * Creates a new {@code FuelTank} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param inter             interpolation factor: where to spawn the object
     */
    public FuelTank(GameView gameView, GamePlayManager gamePlayManager, double inter) {
        super(gameView, gamePlayManager, inter);

        height = 130;
        width = 155;
        size = 0.5;
    }

    /**
     * Renders {@code FuelTank} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        gameView.addImageToCanvas("fuel.png", mid.getX(), mid.getY(), size, rotation);
    }
}
