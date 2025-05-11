package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.game.utilities.WallBlockDimensionCalculator;
import thd.game.utilities.WallBlockGraphicUtils;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.ShiftableGameObject;

import java.awt.*;
import java.util.ArrayList;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * One Brick-Row of a {@link Wall}.
 * These are seperated so that Collision Detection can be differentiated between different altitude Levels.
 *
 * @see Wall
 * @see WallBlockGraphicUtils
 */
public class WallRow extends CollidingGameObject implements ShiftableGameObject, ActivatableGameObject<Void> {
    private final String blockGraphic;

    /**
     * Creates a new {@code WallRow} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param spawnDelayInMilis measure for how long before GameObject enters the Screen
     * @param spawnLineInter    interpolation factor: where on the SpawnLine to spawn the object
     * @param altitudeIndex     the vertical index of the row
     * @param blockGraphic      the block Graphic String for this row
     * @param hitboxIndices     the positions of the hitboxes in this row (can have holes)
     */
    public WallRow(
            GameView gameView,
            GamePlayManager gamePlayManager,
            int spawnDelayInMilis,
            double spawnLineInter,
            int altitudeIndex,
            String blockGraphic,
            ArrayList<int[]> hitboxIndices
    ) {
        super(gameView, gamePlayManager, altitudeIndex / 2, false, spawnDelayInMilis, spawnLineInter - 0.1);
        this.blockGraphic = blockGraphic;

        int[] wallBlockDimensions = new WallBlockGraphicUtils().calcBlockImageDimension(blockGraphic);
        height = WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_Y;
        width = wallBlockDimensions[1];

        // dynamically calculate size, so that when player flies over wall, the wall actually looks lower than the player
        size = Math.floor(GameSettings.MAX_PLAYER_ALTITUDE / (9 * height));

        double offsetY = altitudeIndex * WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_Y * size;
        position.up(offsetY);
        targetPosition.up(offsetY);

        setRelativeHitboxPolygons(calculateHitbox(hitboxIndices));
        hitBoxOffsets(0, 0, 0, 0);
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
    }

    /**
     * Renders this {@code WallRow} of the corresponding {@code Wall} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addBlockImageToCanvas(blockGraphic, position.getX(), position.getY(), size, rotation);
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

    private Polygon[] calculateHitbox(ArrayList<int[]> hitboxIndices) {
        Polygon[] hitboxes = new Polygon[hitboxIndices.size()];

        for (int i = 0; i < hitboxIndices.size(); i++) {
            int[] idx = hitboxIndices.get(i);

            // define hitbox in 2d
            Position[] preProjectionRelativeHitbox = new Position[]{
                    new Position(idx[0] * size * WallBlockDimensionCalculator.HALF_BLOCK_INCREASE_OFFSET_X, 0),
                    new Position((idx[1] + 1) * size * WallBlockDimensionCalculator.HALF_BLOCK_INCREASE_OFFSET_X, 0),
                    new Position((idx[1] + 1) * size * WallBlockDimensionCalculator.HALF_BLOCK_INCREASE_OFFSET_X, -1 * size * WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_Y),
                    new Position(idx[0] * size * WallBlockDimensionCalculator.HALF_BLOCK_INCREASE_OFFSET_X, -1 * size * WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_Y)
            };

            Polygon postProjectionHitbox = calculateRelativeProjectedHitbox(preProjectionRelativeHitbox, TRAVEL_PATH_CALCULATOR.getStretchedIsometricProjectionMatrix());
            hitboxes[i] = postProjectionHitbox;
        }

        return hitboxes;
    }
}
