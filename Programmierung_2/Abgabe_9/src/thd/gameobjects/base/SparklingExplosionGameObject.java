package thd.gameobjects.base;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;

/**
 * A Wrapper for {@link CollidingGameObject} that can be destroyed by a {@link thd.gameobjects.movable.ZaxxonFighterLaserShot}.
 * This will provide an Explosion Animation that is more sparkly.
 *
 * @see ExplodingGameObject
 */
public abstract class SparklingExplosionGameObject extends CollidingGameObject {
    protected SparklingExplosionState currentExplosionState;

    /**
     * Creates a new game object that is able to collide and explode.
     *
     * @param gameView              Window to show the game object on.
     * @param gamePlayManager       Controls the game play.
     * @param altitudeLevel         the altitude of the GameObject
     * @param isRectangular         if true: use rectangular hitbox, else use polygonal
     * @param spawnDelayInMilis            measure for how long before GameObject enters the Screen
     * @param spawnLineInter        interpolation factor: where on the SpawnLine to spawn the object
     */
    public SparklingExplosionGameObject(GameView gameView, GamePlayManager gamePlayManager, int altitudeLevel, boolean isRectangular, int spawnDelayInMilis, double spawnLineInter) {
        super(gameView, gamePlayManager, altitudeLevel, isRectangular, spawnDelayInMilis, spawnLineInter);
    }

    /**
     * Creates a new game object that is able to collide and explode.
     *
     * @param gameView        Window to show the game object on.
     * @param gamePlayManager Controls the game play.
     * @param altitudeLevel   the altitude of the GameObject
     * @param isRectangular   if true: use rectangular hitbox, else use polygonal
     */
    public SparklingExplosionGameObject(GameView gameView, GamePlayManager gamePlayManager, int altitudeLevel, boolean isRectangular) {
        super(gameView, gamePlayManager, altitudeLevel, isRectangular);
    }

    private enum SparklingExplosionState {
        LASER_EXPLOSION_1(SparklingExplosionBlockImages.LASER_EXPLOSION_1, 11, 10, 5),
        LASER_EXPLOSION_2(SparklingExplosionBlockImages.LASER_EXPLOSION_2, 9, 12, 5),
        LASER_EXPLOSION_3(SparklingExplosionBlockImages.LASER_EXPLOSION_3, 10, 11, 5),
        LASER_EXPLOSION_4(SparklingExplosionBlockImages.LASER_EXPLOSION_4, 10, 11, 5);

        public final String display;
        public final int height;
        public final int width;
        public final double size;

        SparklingExplosionState(String display, int height, int width, double size) {
            this.display = display;
            this.height = height;
            this.width = width;
            this.size = size;
        }
    }

    /**
     * Starts the Explosion Animation after the GameObject has been hit.
     */
    @Override
    public void updateStatus() {
        super.updateStatus();

        if (hasDespawned) {

            switchToNextState();

            if (currentExplosionState != null) {
                height = currentExplosionState.height;
                width = currentExplosionState.width;
                size = currentExplosionState.size;

            } else {
                gamePlayManager.destroyGameObject(this);
            }
        }
    }

    /**
     * Renders the Explosion Animation as a sequence of BlockImages on {@code gameView}.
     * Note: Children classes SHOULD still be Override this method to render the actual GameObject.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();

        if (hasDespawned && currentExplosionState != null) {
            gameView.addBlockImageToCanvas(currentExplosionState.display, mid.getX(), mid.getY(), size, 0);
        }
    }

    private void switchToNextState() {
        if (currentExplosionState == null) {
            currentExplosionState = SparklingExplosionState.values()[0];
        }

        if (gameView.timer(30, 0, this)) {

            if (currentExplosionState.ordinal() + 1 < SparklingExplosionState.values().length) {
                currentExplosionState = SparklingExplosionState.values()[currentExplosionState.ordinal() + 1];

            } else {
                currentExplosionState = null;
            }
        }
    }
}
