package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.*;

/**
 * This class is the central contact point for all GameObjects that need to interact with
 * dynamic spawning/ despawning behaviour.
 */
public class GamePlayManager extends UserControlledGameObjectPool{
    private final GameObjectManager gameObjectManager;
    private int currentNumberOfVisibleSquares;
    private int spawnCounter;

    protected GamePlayManager(GameView gameView) {
        super(gameView);

        gameObjectManager = new GameObjectManager();
        currentNumberOfVisibleSquares = 0;
        spawnCounter = 0;
    }

    /**
     * Adds a {@code GameObject} to the Spawning Queue.
     *
     * @param gameObject the object to spawn
     */
    public void spawnGameObject(GameObject gameObject) {
        gameObjectManager.add(gameObject);
    }

    /**
     * Adds a {@code GameObject} to the Depawning Queue.
     *
     * @param gameObject the object to despawn
     */
    public void destroyGameObject(GameObject gameObject) {
        gameObjectManager.remove(gameObject);
    }


    protected void destroyAllGameObjects() {
        gameObjectManager.removeAll();
    }

    private void gamePlayManagement() {
        if (!gameView.timer(0, 1000, this) && currentNumberOfVisibleSquares < 5) {
            spawnGameObject(new Square(gameView, this));
            currentNumberOfVisibleSquares++;
        }

        if (gameView.gameTimeInMilliseconds() >= 0 && spawnCounter == 0) {
            spawnGameObject(new GunImplacement(gameView, this, 0.8, false));
            spawnGameObject(new RadarTower(gameView, this, 0.2));
            spawnCounter++;
        }

        if (gameView.gameTimeInMilliseconds() >= 3000 && spawnCounter == 1) {
            spawnGameObject(new GunImplacement(gameView, this, 0.3, true));
            spawnGameObject(new EnemyShooter(gameView, this, 0.5));
            spawnGameObject(new FuelTank(gameView, this, 0.5));
            spawnCounter++;
        }

        if (gameView.gameTimeInMilliseconds() >= 7000 && spawnCounter == 2) {
            spawnGameObject(new EnergyBarrierTower(gameView, this));
            spawnGameObject(new EnergyBarrier(gameView, this));
            spawnCounter++;
        }
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        gameObjectManager.gameLoop();
        gamePlayManagement();
    }
}
