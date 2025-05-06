package thd.game.managers;

import thd.game.utilities.GameView;
import thd.game.utilities.WallBlockGraphicUtils;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.*;

/**
 * This class is the central contact point for all GameObjects that need to interact with
 * dynamic spawning/ despawning behaviour.
 */
public class GamePlayManager extends UserControlledGameObjectPool{
    private final GameObjectManager gameObjectManager;
    private int spawnCounter;

    private static final int LIVES = 2;
    protected int points;
    protected int lives;
    private int level;
    private int numberOfEnemyPlanes;

    private static final double FUEL_DRAIN = 0.0001;
    private static final double FUEL_TANK_REPLENISH = 0.25;
    private double fuelInterpolation;

    private int startGameTimestamp;

    private final WallBlockGraphicUtils.DynamicWall dynamicWall;

    protected GamePlayManager(GameView gameView) {
        super(gameView);

        gameObjectManager = new GameObjectManager();
        spawnCounter = 0;

        WallBlockGraphicUtils utils = new WallBlockGraphicUtils();
        dynamicWall = utils.generateDynamicWall(WallBlockGraphicUtils.SMALL_SAMPLE_WALL);

        points = 0;
        lives = LIVES;
        level = 1;
        fuelInterpolation = 1;
        numberOfEnemyPlanes = 20;

        startGameTimestamp = gameView.gameTimeInMilliseconds();
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
        if (gameView.gameTimeInMilliseconds() >= startGameTimestamp && spawnCounter == 0) {
            spawnGameObject(new Wall(gameView, this, dynamicWall));
            spawnCounter++;
        }

        if (gameView.gameTimeInMilliseconds() >= startGameTimestamp + 3000 && spawnCounter == 1) {
            spawnGameObject(new GunEmplacement(gameView, this, 0.3, true));
            spawnGameObject(new EnemyShooter(gameView, this, 0.5));
            spawnGameObject(new FuelTank(gameView, this, 0.8));
            spawnCounter++;
        }

        if (gameView.gameTimeInMilliseconds() >= startGameTimestamp + 7000 && spawnCounter == 2) {
            spawnGameObject(new EnergyBarrier(gameView, this, 0));
            spawnCounter++;
        }

        if (gameView.gameTimeInMilliseconds() >= startGameTimestamp + 10000 && spawnCounter == 3) {
            spawnGameObject(new VerticalRocketHole(gameView, this, 0.3));
            spawnGameObject(new RadarTower(gameView, this, 0.7));
            spawnCounter++;
        }
    }

    /**
     * Decrements the number of Lives by 1.
     */
    public void lifeLost() {
        if (lives > 0) {
            lives--;
            restartGame();
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
        if (fuelInterpolation - FUEL_DRAIN >= 0) {
            fuelInterpolation -= FUEL_DRAIN;
        }
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
        return level;
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

    /**
     * Restarts the Game when the {@link ZaxxonFighter} crashed with another {@link thd.gameobjects.base.CollidingGameObject}.
     */
    private void restartGame() {
        gameObjectManager.removeAll();
        spawnCounter = 0;
        startGameTimestamp = gameView.gameTimeInMilliseconds();

        spawnGameObject(zaxxonFighter);
        spawnGameObject(heightStatusBar);
        spawnGameObject(footer);
        spawnGameObject(fuelCellGauge);
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        gameObjectManager.gameLoop();
        gamePlayManagement();
    }
}
