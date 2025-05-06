package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.FollowerBall;
import thd.gameobjects.movable.GunImplacement;
import thd.gameobjects.movable.RadarTower;
import thd.gameobjects.movable.RandomBall;
import thd.gameobjects.unmovable.HeightStatusBar;

/**
 * Manages all GameObjects and renders them on the {@code gameView}.
 */
class GameManager {
    // private static final String[] IMAGE_PATHS = {"enemyshooter.png", "flyingfuel.png", "fuel.png", "playerstraight.png"};

    private final GameView gameView;

    private final GunImplacement gunImplacement;
    private final RadarTower radarTower;
    private final HeightStatusBar heightStatusBar;
    private final RandomBall randomBall;
    private final FollowerBall followerBall;
    /**
     * Initializes GameManager instance and creates all necessary GameObjects
     *
     * @param gameView to display the GameObjects on
     */
    GameManager(GameView gameView) {
        this.gameView = gameView;

        gunImplacement = new GunImplacement(gameView);
        radarTower = new RadarTower(gameView);
        heightStatusBar = new HeightStatusBar(gameView);
        randomBall = new RandomBall(gameView);
        followerBall = new FollowerBall(gameView, randomBall);
    }

    /**
     * Renders all available GameObjects on the {@code gameView}
     */
    void gameLoop() {
        gunImplacement.updatePosition();
        gunImplacement.addToCanvas();

        radarTower.updatePosition();
        radarTower.addToCanvas();

        heightStatusBar.addToCanvas();

        randomBall.updatePosition();
        randomBall.addToCanvas();

        followerBall.updatePosition();
        followerBall.addToCanvas();

        /*
        for (int i = 0; i < GameManager.IMAGE_PATHS.length; i++) {
            gameView.addImageToCanvas(GameManager.IMAGE_PATHS[i], i * 150, 100, 0.5, 0);
        }

        // render Fuel Display
        int pixelSize = 3;
        int fuelCellHeight = FuelCellBlockImages.FUEL_CELL.split("\n").length;
        int fuelCellWidth = FuelCellBlockImages.FUEL_CELL.split("\n")[fuelCellHeight / 2].length();
        for (int i = 0; i < 20; i++) {
            gameView.addBlockImageToCanvas(FuelCellBlockImages.FUEL_CELL, 150 + i * pixelSize * fuelCellWidth, GameView.HEIGHT - fuelCellHeight - 100, pixelSize, 0);
        }

        // render Live Display
        pixelSize = 8;
        gameView.addBlockImageToCanvas(LiveDisplayBlockImages.LIVE_DISPLAY, 100, GameView.HEIGHT - 150, pixelSize, 0);
        gameView.addBlockImageToCanvas(LiveDisplayBlockImages.LIVE_DISPLAY, 150, GameView.HEIGHT - 150, pixelSize, 0);

        // render Flag Display
        pixelSize = 5;
        gameView.addBlockImageToCanvas(FlagDisplayBlockImages.FLAG_DISPLAY, GameView.WIDTH - 200, GameView.HEIGHT - 150, pixelSize, 0);
        */
    }
}
