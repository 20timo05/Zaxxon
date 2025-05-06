package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.GunImplacement;
import thd.gameobjects.movable.RadarTower;
import thd.gameobjects.unmovable.HeightStatusBar;

/**
 * Manages all GameObjects and renders them on the {@code gameView}.
 */
class GameManager {
    private final GameView gameView;
    private final GunImplacement gunImplacement;
    private final RadarTower radarTower;
    private final HeightStatusBar heightStatusBar;

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
    }
}
