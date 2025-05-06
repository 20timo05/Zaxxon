package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.StationaryGameObject;

/**
 * The {@code EnemyShooter} is a stationary GameObject that yields 100 points upon destruction.
 * This is increases by 50 points each round of the game (motherbase, space combat, ...).
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 *
 * @see GameObject
 */
public class EnemyShooter extends StationaryGameObject {

    /**
     * Creates a new {@code EnemyShooter} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param inter             interpolation factor: where to spawn the object
     */
    public EnemyShooter(GameView gameView, GamePlayManager gamePlayManager, double inter) {
        super(gameView, gamePlayManager, inter);

        height = 80;
        width = 127;
        size = 0.5;
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
}
