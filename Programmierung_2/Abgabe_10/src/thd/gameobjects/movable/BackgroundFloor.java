package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.ShiftableGameObject;
import thd.gameobjects.base.Vector2d;

import thd.game.utilities.TravelPathCalculator;

class BackgroundFloor extends GameObject implements ShiftableGameObject {
    private static final int GROUND_OFFSET_LEFT = 200;
    private static final int GROUND_OFFSET_FORWARD = 500;
    private final String groundBlockImage;
    private final boolean isFirst;

    /**
     * Creates a new {@code BackgroundFloor} GameObject.
     *
     * @param gameView        GameView to show the game object on.
     * @param gamePlayManager reference to the gamePlayManager
     * @param height          length of the floor
     * @param isFirst         boolean whether it should show Floor EdgeF
     */
    BackgroundFloor(GameView gameView, GamePlayManager gamePlayManager, double height, boolean isFirst) {
        super(gameView, gamePlayManager, 0, 0);
        this.isFirst = isFirst;

        Vector2d newTargetPosition = new Vector2d(targetPosition);
        newTargetPosition.add(new Vector2d(
                -TravelPathCalculator.DISTANCE_TO_DESPAWN_LINE,
                GameSettings.MOVEMENT_ANGLE_IN_RADIANS
        ));
        targetPosition.updateCoordinates(newTargetPosition);

        this.height = height;
        size = 6;
        distanceToBackground = 0;
        groundBlockImage = generateGroundBlockImage(height);
    }

    private String generateGroundBlockImage(double height) {
        int width = (int) (2 * GROUND_OFFSET_LEFT + Math.abs(TravelPathCalculator.copySpawnLine()[0].getX() - TravelPathCalculator.copySpawnLine()[1].getX()) / size);

        StringBuilder blockImage = new StringBuilder();
        for (int y = (int) height; y > 0; y--) {
            blockImage.append(" ".repeat(y * 2));
            String color = y % 2 == 0 ? "g" : "b";
            blockImage.append(color.repeat(width));
            blockImage.append("\n");
        }
        for (int x = 0; x < width; x += 4) {
            String color = (x / 4) % 2 == 0 ? "g" : "b";
            blockImage.append(" ".repeat(x));
            blockImage.append(color.repeat(width - x));
            blockImage.append("\n");
        }
        return blockImage.toString();
    }

    /**
     * Renders {@code BackgroundFloor} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Vector2d backgroundPosition = new Vector2d(position);
        backgroundPosition.add(new Vector2d(-GROUND_OFFSET_FORWARD, GameSettings.MOVEMENT_ANGLE_IN_RADIANS));

        gameView.addBlockImageToCanvas(groundBlockImage, backgroundPosition.getX() - GROUND_OFFSET_LEFT, backgroundPosition.getY() - height * size, size, 0);
        if (isFirst && calcInterpolation() < 0.5) {
            gameView.addBlockImageToCanvas(BackgroundBlockImages.EDGE, backgroundPosition.getX() - GROUND_OFFSET_LEFT, backgroundPosition.getY(), 4, 0);
        }
    }
}