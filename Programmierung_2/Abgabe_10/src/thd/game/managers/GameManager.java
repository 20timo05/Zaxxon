package thd.game.managers;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.utilities.GameView;

/**
 * Manages all GameObjects and renders them on the {@code gameView}.
 */
class GameManager extends LevelManager {
    private int currentLives;
    /**
     * Initializes GameManager instance and creates all necessary GameObjects
     *
     * @param gameView to display the GameObjects on
     */
    GameManager(GameView gameView) {
        super(gameView);

        startNewGame();
    }

    public void startNewGame() {
        Level.difficulty = Difficulty.EASY;
        initializeGame();
    }

    private void gameManagement() {
        if (endOfGame()) {
            if (lives == 0) {
                overlay.showMessage("Game Over", 2);
            } else {
                overlay.showMessage("Great Job!", 2);
            }

            startNewGame();

        } else if (endOfLevel()) {
            overlay.showMessage("Great Job!", 2);
            switchToNextLevel();
            initializeLevel();

        } else if (lives < currentLives) { // player has crashed
            initializeLevel();
            currentLives = lives;
        }
    }


    private boolean endOfLevel() {
        return gameView.gameTimeInMilliseconds() >= level.levelDurationTimestamp;
    }

    private boolean endOfGame() {
        return lives == 0 || (!hasNextLevel() && endOfLevel());
    }

    @Override
    protected void initializeLevel() {
        super.initializeLevel();
        overlay.showMessage(level.name, 2);
    }

    @Override
    protected void initializeGame() {
        super.initializeGame();
        initializeLevel();
        currentLives = lives;
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
