package game;
public class GameViewManager {
    private final GameView gameView;
    private Tank tank;
    private SatelliteDish satelliteDish;
    private HeightStatusBar heightStatusBar;

    public GameViewManager() {
        gameView = new GameView();
        tank = new Tank(gameView);
        satelliteDish = new SatelliteDish(gameView);
        heightStatusBar = new HeightStatusBar(gameView);

        startGameLoop();
    }
    private void startGameLoop() {
        // Der Game-Loop
        while (gameView.isVisible()) {
            tank.updatePosition();
            tank.addToCanvas();

            satelliteDish.updatePosition();
            satelliteDish.addToCanvas();

            heightStatusBar.addToCanvas();

            gameView.plotCanvas();
        }
    }
}