package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.DebuggingLines;
import thd.gameobjects.unmovable.Footer;
import thd.gameobjects.unmovable.FuelCellGauge;
import thd.gameobjects.unmovable.HeightStatusBar;

/**
 * Manages all GameObjects and renders them on the {@code gameView}.
 */
class GameManager extends GamePlayManager {
    private final DebuggingLines debuggingLines;

    /**
     * Initializes GameManager instance and creates all necessary GameObjects
     *
     * @param gameView to display the GameObjects on
     */
    GameManager(GameView gameView) {
        super(gameView);

        debuggingLines = new DebuggingLines(gameView, this);
        zaxxonFighter = new ZaxxonFighter(gameView, this);
        heightStatusBar = new HeightStatusBar(gameView, this);
        footer = new Footer(gameView, this);
        fuelCellGauge = new FuelCellGauge(gameView, this);

        spawnGameObject(zaxxonFighter);
        spawnGameObject(heightStatusBar);
        spawnGameObject(footer);
        spawnGameObject(fuelCellGauge);

    }

    private void gameManagement() {

    }

    /**
     * Renders all available GameObjects on the {@code gameView}
     */
    @Override
    protected void gameLoop() {
        super.gameLoop();

        heightStatusBar.updateStatus(zaxxonFighter.getAltitudeInterpolation());
        // debuggingLines.addToCanvas();

        gameManagement();
    }
}
