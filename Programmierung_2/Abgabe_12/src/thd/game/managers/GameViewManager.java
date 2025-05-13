package thd.game.managers;

import thd.game.utilities.GameView;

import java.awt.*;

/**
 * Creates the Window and starts the GameLoop.
 */
public final class GameViewManager {
    private final GameView gameView;
    private final GameManager gameManager;

    /**
     * Initializes {@code GameView} and decorates with Information about the game.
     */
    public GameViewManager() {
        gameView = new GameView();
        gameManager = new GameManager(gameView);

        gameView.updateStatusText("Timo Rolf - Java Programmierung SS 2025");
        gameView.updateWindowTitle("Zaxxon (Synapse) C64");
        gameView.updateWindowIcon("playerstraight.png");

        gameView.updateColorForBlockImage('G', new Color(50, 50, 50, 127));
        gameView.updateColorForBlockImage('g', new Color(118, 118, 118));

        gameView.showStatistic(true);
        startGameLoop();
    }

    private void startGameLoop() {
        // Der Game-Loop
        while (gameView.isVisible()) {
            gameManager.gameLoop();
            gameView.plotCanvas();
        }
    }
}