package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

/**
 * The {@code EnemyShooter} is a stationary GameObject that yields 100 points
 * upon destruction.
 * This is increases by 50 points each round of the game (motherbase, space
 * combat, ...).
 * They will appear in the Motherbase as passive objects that does not shoot or
 * move.
 *
 * @see GameObject
 */
public class EnemyShooter extends ExplodingGameObject implements ShiftableGameObject, ActivatableGameObject<Void> {
    private enum State{};
    private State currentState; // Wichtel, Explosion Animation was outsourced for cleaner code.
    /**
     * Creates a new {@code EnemyShooter} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param spawnDelayInMilis measure for how long before GameObject enters the
     *                          Screen
     * @param spawnLineInter    interpolation factor: where on the SpawnLine to spawn
     *                          the object
     */
    public EnemyShooter(GameView gameView, GamePlayManager gamePlayManager, int spawnDelayInMilis, double spawnLineInter) {
        super(gameView, gamePlayManager, 0, true, spawnDelayInMilis, spawnLineInter);

        height = 80;
        width = 127;
        size = 0.7;

        hitBoxOffsets(-width * size / 2, -height * size / 2, 0, 0);
    }

    /**
     * Renders {@code EnemyShooter} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        super.addToCanvas();

        if (!hasDespawned) {
            Position mid = calcMiddlePoint();
            gameView.addImageToCanvas("enemyshooter.png", mid.getX(), mid.getY(), size, rotation);
        }
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

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (getAltitudeLevel() == other.getAltitudeLevel()) {
            if (other instanceof ZaxxonFighterLaserShot) {
                hasDespawned = true;

                gamePlayManager.addPoints(100 + 50 * (gamePlayManager.retrieveLevel().number + 1));
                gamePlayManager.decrementNumberOfEnemyPlanes();
            }
        }
    }

}
