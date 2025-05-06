package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.StationaryGameObject;

/**
 * The {@code GunImplacement} is a stationary GameObject that yields 200 or 500 points (random) upon destruction.
 * They will appear in the Motherbase and shoot straight bullets at the player, but only when he is at ground level.
 *
 * @see GameObject
 */
public class GunImplacement extends StationaryGameObject {
    private static final int MAX_SHOOT_INTERVAL_IN_MILLISECONDS = 4000;
    private static final int MIN_SHOOT_INTERVAL_IN_MILLISECONDS = 1500;

    private final boolean orientation;
    private int nextShotTime;

    /**
     * Creates a new {@code GunImplacement} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param inter             interpolation factor: where to spawn the object
     * @param orientation 0-> shoots to the left, 1->shoots straight, 2->shoots to the right
     */
    public GunImplacement(GameView gameView, GamePlayManager gamePlayManager, double inter, boolean orientation) {
        super(gameView, gamePlayManager, inter);

        this.orientation = orientation;
        height = 19;
        width = 30;
        size = 2;

        calcNextShotTime();
    }

    @Override
    public void updateStatus() {
        super.updateStatus();

        if (gameView.gameTimeInMilliseconds() >= nextShotTime) {
            gamePlayManager.spawnGameObject(new GunImplacementLaserShot(gameView, gamePlayManager, this, orientation));
            calcNextShotTime();
        }
    }

    /**
     * Renders {@code GunImplacement} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        if (orientation) {
            gameView.addBlockImageToCanvas(GunImplacementBlockImages.STRAIGHT, mid.getX(), mid.getY(), size, rotation);
        } else {
            gameView.addBlockImageToCanvas(GunImplacementBlockImages.LEFT, mid.getX(), mid.getY(), size, rotation);
        }
    }

    private void calcNextShotTime() {
        int randomInterval = (int) (Math.random() * (MAX_SHOOT_INTERVAL_IN_MILLISECONDS - MIN_SHOOT_INTERVAL_IN_MILLISECONDS) + MIN_SHOOT_INTERVAL_IN_MILLISECONDS);
        nextShotTime = gameView.gameTimeInMilliseconds() + randomInterval;
    }

}
