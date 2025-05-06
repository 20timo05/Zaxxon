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
class GameManager extends UserControlledGameObjectPool {
    private final GameObjectManager gameObjectManager;

    private final DebuggingLines debuggingLines;

    /**
     * Initializes GameManager instance and creates all necessary GameObjects
     *
     * @param gameView to display the GameObjects on
     */
    GameManager(GameView gameView) {
        super(gameView);

        debuggingLines = new DebuggingLines(gameView);
        zaxxonFighter = new ZaxxonFighter(gameView);
        energyBarrier = new EnergyBarrierTower(gameView);
        gunImplacement = new GunImplacement(gameView);
        radarTower = new RadarTower(gameView);
        heightStatusBar = new HeightStatusBar(gameView);
        fuelTank = new FuelTank(gameView);
        enemyShooter = new EnemyShooter(gameView);
        footer = new Footer(gameView);
        fuelCellGauge = new FuelCellGauge(gameView);

        this.gameObjectManager = new GameObjectManager();
        gameObjectManager.add(zaxxonFighter);
        gameObjectManager.add(energyBarrier);
        gameObjectManager.add(gunImplacement);
        gameObjectManager.add(radarTower);
        gameObjectManager.add(heightStatusBar);
        gameObjectManager.add(fuelTank);
        gameObjectManager.add(enemyShooter);
        gameObjectManager.add(footer);
        gameObjectManager.add(fuelCellGauge);
    }

    /**
     * Renders all available GameObjects on the {@code gameView}
     */
    @Override
    protected void gameLoop() {
        super.gameLoop();

        heightStatusBar.updateStatus(zaxxonFighter.getPlayerAltitudeInterpolation());

        gameObjectManager.gameLoop();
    }
}
