package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.*;

/**
 * The {@code GunEmplacement} is a stationary GameObject that yields 200 or 500
 * points (random) upon destruction.
 * They will appear in the Motherbase and shoot straight bullets at the player,
 * but only when he is at ground level.
 *
 * @see GameObject
 */
public class GunEmplacement extends ExplodingGameObject implements ShiftableGameObject, ActivatableGameObject<Void> {
    private static final int MAX_SHOOT_INTERVAL_IN_MILLISECONDS = 4000;
    private static final int MIN_SHOOT_INTERVAL_IN_MILLISECONDS = 1500;

    private final boolean orientation;
    private int nextShotTime;

    /**
     * Creates a new {@code GunEmplacement} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param spawnDelayInMilis measure for how long before GameObject enters the
     *                          Screen
     * @param spawnLineInter    interpolation factor: where on the SpawnLine to spawn
     *                          the object
     * @param orientation       true: shoots straight, false: shoots to the left
     */
    public GunEmplacement(GameView gameView, GamePlayManager gamePlayManager, int spawnDelayInMilis, double spawnLineInter,
                          boolean orientation) {
        super(gameView, gamePlayManager, 0, true, spawnDelayInMilis, spawnLineInter);

        this.orientation = orientation;
        height = 19;
        width = 30;
        size = 2.8;

        calcNextShotTime();
        hitBoxOffsets(-width * size / 2, -height * size / 2, 0, 0);
    }

    @Override
    public void updateStatus() {
        super.updateStatus();

        if (!hasDespawned && gameView.gameTimeInMilliseconds() >= nextShotTime) {
            gamePlayManager.spawnGameObject(new GunEmplacementLaserShot(gameView, gamePlayManager, this, orientation));
            calcNextShotTime();

            gameView.playSound("gunemplacementshoot.wav", false);
        }
    }

    /**
     * Renders {@code GunEmplacement} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        super.addToCanvas();

        if (!hasDespawned) {
            Position mid = calcMiddlePoint();
            String blockImage = orientation ? GunEmplacementBlockImages.STRAIGHT : GunEmplacementBlockImages.LEFT;
            gameView.addBlockImageToCanvas(blockImage, mid.getX(), mid.getY(), size, rotation);
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

    private void calcNextShotTime() {
        int randomInterval = (int) (Math.random()
                * (MAX_SHOOT_INTERVAL_IN_MILLISECONDS - MIN_SHOOT_INTERVAL_IN_MILLISECONDS)
                + MIN_SHOOT_INTERVAL_IN_MILLISECONDS);
        nextShotTime = gameView.gameTimeInMilliseconds() + randomInterval;
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (getAltitudeLevel() == other.getAltitudeLevel()) {
            if (other instanceof ZaxxonFighterLaserShot) {
                hasDespawned = true;

                gamePlayManager.addPoints(Math.random() < 0.5 ? 200 : 500);
            }
        }
    }
}
