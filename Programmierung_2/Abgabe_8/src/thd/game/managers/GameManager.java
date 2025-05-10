package thd.game.managers;

import thd.game.utilities.GameView;

/**
 * Manages all GameObjects and renders them on the {@code gameView}.
 */
class GameManager extends GameWorldManager {
    /**
     * Initializes GameManager instance and creates all necessary GameObjects
     *
     * @param gameView to display the GameObjects on
     */
    GameManager(GameView gameView) {
        super(gameView);
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
        gameManagement();
    }
}
