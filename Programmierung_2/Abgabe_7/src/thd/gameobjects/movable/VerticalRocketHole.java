package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

/**
 * A passive {@code GameObject} that spawns in the Motherbase of the Game.
 * At some point in its life cycle it will launch a {@link VerticalRocket}.
 */
public class VerticalRocketHole extends GameObject {
    private double rocketSpawnInterpolationTreshold;
    private boolean rocketAlreadySpawned;
    /**
     * Creates a new {@code EnemyShooter} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param distanceFromSpawnLine measure for how long before GameObject enters the Screen
     * @param spawnLineInter        interpolation factor: where on the SpawnLine to spawn the object
     */
    public VerticalRocketHole(GameView gameView, GamePlayManager gamePlayManager, double distanceFromSpawnLine, double spawnLineInter) {
        super(gameView, gamePlayManager, distanceFromSpawnLine, spawnLineInter);

        height = 5;
        width = 9;
        size = 5;
        distanceToBackground = 1;

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
     * Renders {@code EnergyBarrierTower} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Position mid = calcMiddlePoint();
        gameView.addBlockImageToCanvas(VerticalRocketBlockImages.HOLE, mid.getX(), mid.getY(), size, 0);
    }
}
