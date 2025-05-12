package thd.gameobjects.base;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;

/**
 * A Wrapper for {@link CollidingGameObject} that can be destroyed by a {@link thd.gameobjects.movable.ZaxxonFighterLaserShot}.
 * This will provide an Explosion Animation.
 *
 * @see SparklingExplosionGameObject
 */
public abstract class ExplodingGameObject extends CollidingGameObject {
    protected ExplosionState currentExplosionState;

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
    public ExplodingGameObject(GameView gameView, GamePlayManager gamePlayManager, int altitudeLevel, boolean isRectangular, int spawnDelayInMilis, double spawnLineInter) {
        super(gameView, gamePlayManager, altitudeLevel, isRectangular, spawnDelayInMilis, spawnLineInter);
    }

    /**
     * Crates a new game object that is able to collide and explode.
     *
     * @param gameView        Window to show the game object on.
     * @param gamePlayManager Controls the game play.
     * @param altitudeLevel   the altitude of the GameObject
     * @param isRectangular   if true: use rectangular hitbox, else use polygonal
     */
    public ExplodingGameObject(GameView gameView, GamePlayManager gamePlayManager, int altitudeLevel, boolean isRectangular) {
        super(gameView, gamePlayManager, altitudeLevel, isRectangular);
    }

    private enum ExplosionState {
        EXPLOSION_1("explosion1.png", 251, 214, 0.3),
        EXPLOSION_2("explosion2.png", 251, 217, 0.3),
        EXPLOSION_3("explosion3.png", 251, 254, 0.3),
        EXPLOSION_4("explosion4.png", 260, 232, 0.3),
        EXPLOSION_5("explosion5.png", 251, 286, 0.3),
        EXPLOSION_6("explosion6.png", 251, 252, 0.3),
        EXPLOSION_7("explosion7.png", 251, 220, 0.3),
        EXPLOSION_8("explosion8.png", 251, 214, 0.3),
        EXPLOSION_9("explosion9.png", 251, 270, 0.3);

        public final String display;
        public final int height;
        public final int width;
        public final double imageScaleFactor;

        ExplosionState(String display, int height, int width, double imageScaleFactor) {
            this.display = display;
            this.height = height;
            this.width = width;
            this.imageScaleFactor = imageScaleFactor;
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

            } else {
                gamePlayManager.destroyGameObject(this);
            }
        }
    }

    /**
     * Renders the Explosion Animation as a sequence of .png Images on {@code gameView}.
     * Note: Children classes SHOULD still be Override this method to render the actual GameObject.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        if (hasDespawned && currentExplosionState != null) {
            Position mid = calcMiddlePoint();
            gameView.addImageToCanvas(currentExplosionState.display, mid.getX()+10, mid.getY()+15, currentExplosionState.imageScaleFactor, 0);
        }
    }

    private void switchToNextState() {
        if (currentExplosionState == null) {
            currentExplosionState = ExplosionState.values()[0];
        }

        if (gameView.timer(30, 0, this)) {

            if (currentExplosionState.ordinal() + 1 < ExplosionState.values().length) {
                currentExplosionState = ExplosionState.values()[currentExplosionState.ordinal() + 1];

            } else {
                currentExplosionState = null;
            }
        }
    }
}
