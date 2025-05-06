package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code EnemyShooter} is a stationary GameObject that yields 100 points upon destruction.
 * This is increases by 50 points each round of the game (motherbase, space combat, ...).
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 *
 * @see GameObject
 */
public class EnemyShooter extends CollidingGameObject {

    /**
     * Creates a new {@code EnemyShooter} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param distanceFromSpawnLine measure for how long before GameObject enters the Screen
     * @param spawnLineInter        interpolation factor: where on the SpawnLine to spawn the object
     */
    public EnemyShooter(GameView gameView, GamePlayManager gamePlayManager, double distanceFromSpawnLine, double spawnLineInter) {
        super(gameView, gamePlayManager, 0, true, distanceFromSpawnLine, spawnLineInter);

        height = 80;
        width = 127;
        size = 0.5;
        distanceToBackground = 1;

        hitBoxOffsets(-width*size/2, -height*size/2, 0, 0);
    }

    /**
     * Renders {@code EnemyShooter} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        gameView.addImageToCanvas("enemyshooter.png", mid.getX(), mid.getY(), size, rotation);
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (getAltitudeLevel() == other.getAltitudeLevel()) {
            if (other instanceof ZaxxonFighterLaserShot) {
                gamePlayManager.destroyGameObject(this);

                gamePlayManager.addPoints(100 + 50 * (gamePlayManager.getLevel() + 1));
                gamePlayManager.decrementNumberOfEnemyPlanes();
            }
        }
    }


}
