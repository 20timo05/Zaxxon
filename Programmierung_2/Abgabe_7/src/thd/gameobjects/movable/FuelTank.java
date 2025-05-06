package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code FuelTank} is a stationary GameObject that yields 300 points upon destruction. It also replenishes the Fuel Gauge.
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 *
 * @see GameObject
 */
public class FuelTank extends CollidingGameObject {

    /**
     * Creates a new {@code FuelTank} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param distanceFromSpawnLine measure for how long before GameObject enters the Screen
     * @param spawnLineInter        interpolation factor: where on the SpawnLine to spawn the object
     */
    public FuelTank(GameView gameView, GamePlayManager gamePlayManager, double distanceFromSpawnLine, double spawnLineInter) {
        super(gameView, gamePlayManager, 0, true, distanceFromSpawnLine, spawnLineInter);

        height = 130;
        width = 155;
        size = 0.5;
        distanceToBackground = 1;

        hitBoxOffsets(-width*size/2, -height*size/2, 0, 0);
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

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (getAltitudeLevel() == other.getAltitudeLevel()) {
            if (other instanceof ZaxxonFighterLaserShot) {
                gamePlayManager.destroyGameObject(this);

                gamePlayManager.addPoints(300);
                gamePlayManager.replenishFuel();
            }
        }
    }
}
