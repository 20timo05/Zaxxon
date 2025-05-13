package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

/**
 * A passive {@code GameObject} that spawns in the Motherbase of the Game.
 * At some point in its life cycle it will launch a {@link VerticalRocket}.
 */
public class VerticalRocketHole extends GameObject implements ShiftableGameObject, ActivatableGameObject<Void> {
    private double rocketSpawnInterpolationTreshold;
    private boolean rocketAlreadySpawned;

    /**
     * Creates a new {@code EnemyShooter} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param spawnDelayInMilis measure for how long before GameObject enters the
     *                          Screen
     * @param spawnLineInter    interpolation factor: where on the SpawnLine to spawn
     *                          the object
     */
    public VerticalRocketHole(GameView gameView, GamePlayManager gamePlayManager, int spawnDelayInMilis,
                              double spawnLineInter) {
        super(gameView, gamePlayManager, spawnDelayInMilis, spawnLineInter);

        height = 5;
        width = 9;
        size = 5;

        rocketSpawnInterpolationTreshold = Math.random() * (0.65 - 0.45) + 0.45;
        rocketAlreadySpawned = false;
    }

    @Override
    public void updateStatus() {
        super.updateStatus();
        if (!rocketAlreadySpawned && calcInterpolation() > rocketSpawnInterpolationTreshold) {
            gamePlayManager.spawnGameObject(new VerticalRocket(gameView, gamePlayManager, this));
            rocketAlreadySpawned = true;
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

    /**
     * Renders {@code EnergyBarrierTower} object as a BlockImage on
     * {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        gameView.addBlockImageToCanvas(VerticalRocketBlockImages.HOLE, mid.getX(), mid.getY(), size, 0);
    }
}
