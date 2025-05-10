package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.*;

import static thd.game.managers.GameSettings.SPEED_IN_PIXEL;

/**
 * This class is the central contact point for all GameObjects that need to interact with
 * dynamic spawning/ despawning behaviour.
 */
public class GamePlayManager extends WorldShiftManager {
    private final GameObjectManager gameObjectManager;

    protected int points;
    protected int lives;
    protected int numberOfEnemyPlanes;

    private static final double FUEL_DRAIN = 0.0001;
    private static final double FUEL_TANK_REPLENISH = 0.25;
    protected double fuelInterpolation;

    protected GamePlayManager(GameView gameView) {
        super(gameView);

        gameObjectManager = new GameObjectManager();
    }

    /**
     * Adds a {@code GameObject} to the Spawning Queue.
     *
     * @param gameObject the object to spawn
     */
    @Override
    public void spawnGameObject(GameObject gameObject) {
        super.spawnGameObject(gameObject);
        gameObjectManager.add(gameObject);
    }

    /**
     * Adds a {@code GameObject} to the Depawning Queue.
     *
     * @param gameObject the object to despawn
     */
    @Override
    public void destroyGameObject(GameObject gameObject) {
        super.destroyGameObject(gameObject);
        gameObject.hasDespawned = true;
        gameObjectManager.remove(gameObject);
    }

    @Override
    protected void destroyAllGameObjects() {
        super.destroyAllGameObjects();
        gameObjectManager.removeAll();
    }

    private void gamePlayManagement() {
        moveWorldToLeft(-SPEED_IN_PIXEL);
    }

    /**
     * Decrements the number of Lives by 1.
     */
    public void lifeLost() {
        if (lives > 0) {
            lives--;
        }
    }

    /**
     * Replenishes the {@link thd.gameobjects.unmovable.FuelCellGauge} upon destruction of a {@link FuelTank}.
     */
    public void replenishFuel() {
        fuelInterpolation += FUEL_TANK_REPLENISH;
    }

    /**
     * Drains a little Fuel every game tick.
     */
    public void looseFuel() {
        fuelInterpolation -= FUEL_DRAIN;
    }

    /**
     * Returns how much percent of the Fuel is left.
     *
     * @return the fuel interpolation factor
     */
    public double getFuelInterpolation() {
        return fuelInterpolation;
    }

    /**
     * Returns the number of Lives that are left.
     *
     * @return number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns the Level that the Player is in at the moment.
     *
     * @return the level
     */
    public int getLevel() {
        return level.index;
    }

    /**
     * Returns the current score of the player.
     *
     * @return the score
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds points to the score upon destruction of a {@link thd.gameobjects.base.CollidingGameObject}.
     *
     * @param numPoints the number of points to add
     */
    public void addPoints(int numPoints) {
        points += numPoints;
    }

    /**
     * A counter of {@link EnemyShooter}s that still have to be shot.
     *
     * @return the counter
     */
    public int getNumberOfEnemyPlanes() {
        return numberOfEnemyPlanes;
    }

    /**
     * Decrements the counter of {@link EnemyShooter}s that still have to be shot.
     */
    public void decrementNumberOfEnemyPlanes() {
        if (numberOfEnemyPlanes > 0) {
            numberOfEnemyPlanes--;
        }
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        gameObjectManager.gameLoop();
        gamePlayManagement();
    }
}
