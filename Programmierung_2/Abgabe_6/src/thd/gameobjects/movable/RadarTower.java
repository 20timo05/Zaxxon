package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * The {@code RadarTower} is a stationary GameObject that yields 1000 points upon destruction.
 * They will appear in the Motherbase as passive objects that does not shoot or move.
 *
 * @see GameObject
 */
public class RadarTower extends CollidingGameObject {

    /**
     * Creates a new {@code RadarTower} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param inter             interpolation factor: where to spawn the object
     */
    public RadarTower(GameView gameView, GamePlayManager gamePlayManager, double inter) {
        super(gameView, gamePlayManager, 0, true, inter);

        height = 129;
        width = 109;
        size = 0.5;

        hitBoxOffsets(-width*size/2, -height*size/2, 0, 0);
    }

    /**
     * Renders {@code RadarTower} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        gameView.addImageToCanvas("radartower.png", mid.getX(), mid.getY(), size, rotation);
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (getAltitudeLevel() == other.getAltitudeLevel()) {
            if (other instanceof ZaxxonFighterLaserShot) {
                gamePlayManager.destroyGameObject(this);

                gamePlayManager.addPoints(1000);
            }
        }
    }
}
