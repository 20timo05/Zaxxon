package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

/**
 * The {@code FuelTank} is a stationary GameObject that yields 300 points upon
 * destruction. It also replenishes the Fuel Gauge.
 * They will appear in the Motherbase as passive objects that does not shoot or
 * move.
 *
 * @see GameObject
 */
public class FuelTank extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<Void> {

    /**
     * Creates a new {@code FuelTank} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param spawnDelayInMilis measure for how long before GameObject enters the
     *                          Screen
     * @param spawnLineInter    interpolation factor: where on the SpawnLine to spawn
     *                          the object
     */
    public FuelTank(GameView gameView, GamePlayManager gamePlayManager, int spawnDelayInMilis, double spawnLineInter) {
        super(gameView, gamePlayManager, 0, true, spawnDelayInMilis, spawnLineInter);

        height = 130;
        width = 155;
        size = 0.5;

        hitBoxOffsets(-width * size / 2, -height * size / 2, 0, 0);
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
