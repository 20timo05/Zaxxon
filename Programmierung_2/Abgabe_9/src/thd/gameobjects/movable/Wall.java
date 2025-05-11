package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.game.utilities.WallBlockGraphicUtils;
import thd.gameobjects.base.GameObject;

import java.util.ArrayList;

/**
 * The {@code Wall}s are stationary GameObjects that appear in the Motherbase.
 * They have holes that the {@link ZaxxonFighter} has to fly through.
 * Sometimes the holes are protected by an {@link EnergyBarrierAnimation},
 * making it even more difficult to fly through.
 * It is advised to shoot at the wall to determine your relative position to it.
 *
 * @see GameObject
 */
public class Wall extends GameObject {
    /**
     * An array of all the actual {@link WallRow} GameObjects associated with this Wall.
     * These will be added as separate {@link thd.gameobjects.base.CollidingGameObject} so that Collision Detection can happen at different altitudes.
     */
    public final WallRow[] wallRows;
    /**
     * Creates a new {@code Wall} GameObject.
     * This class serves as a Wrapper for several {@code WallRow} GameObjects that
     * are staked on top of each other.
     *
     * @param gameView        GameView to show the game object on.
     * @param gamePlayManager reference to the gamePlayManager
     * @param dynamicWall     a BlockGraphic String for each row of the Wall &
     *                        the corresponding hitbox (created by
     *                        {@link WallBlockGraphicUtils#generateDynamicWall(String)})
     * @param spawnDelayInMilis      measure for how long before GameObject enters
     *                        the Screen
     * @param spawnLineInter  interpolation factor: where on the SpawnLine to
     *                        spawn the object
     *
     * @see WallBlockGraphicUtils
     */
    public Wall(GameView gameView, GamePlayManager gamePlayManager, WallBlockGraphicUtils.DynamicWall dynamicWall,
            int spawnDelayInMilis, double spawnLineInter) {
        super(gameView, gamePlayManager);

        wallRows = new WallRow[dynamicWall.wallBlockImageInRows.length];

        String[] wallBlockImageInRows = dynamicWall.wallBlockImageInRows;
        ArrayList<ArrayList<int[]>> hitboxIndicesInRows = dynamicWall.hitboxIndicesInRows;

        for (int y = 0; y < wallBlockImageInRows.length; y++) {
            String blockGraphic = wallBlockImageInRows[wallBlockImageInRows.length - y - 1];
            ArrayList<int[]> hitboxIndices = hitboxIndicesInRows.get(hitboxIndicesInRows.size() - y - 1);

            wallRows[y] = new WallRow(
                    gameView,
                    gamePlayManager,
                    spawnDelayInMilis,
                    spawnLineInter,
                    y,
                    blockGraphic,
                    hitboxIndices);
        }
    }

    @Override
    public void addToCanvas() {
    }
}
