package thd.game.managers;

import thd.game.level.Difficulty;
import thd.game.level.Level;
import thd.game.utilities.FileAccess;
import thd.game.utilities.GameView;
import thd.screens.GameInfo;
import thd.screens.Screens;

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

    private void startNewGame() {
        Level.difficulty = FileAccess.readDifficultyFromDisc(); // Lesen der gespeicherten Auswahl.
        String selection = Screens.showStartScreen(gameView, GameInfo.TITLE, GameInfo.DESCRIPTION, Level.difficulty.name);
        Level.difficulty = Difficulty.fromName(selection);
        FileAccess.writeDifficultyToDisc(Level.difficulty); // Abspeichern der neuen Auswahl.
        initializeGame();
    }

    private void gameManagement() {
        if (endOfGame()) {
            gameView.stopAllSounds();
            if (lives == 0) {
                overlay.showMessage("Game Over");

            } else {
                overlay.showMessage("Game Completed!");
            }

            if (overlay.isMessageShown() && gameView.timer(2000, 0, this)) {
                gameView.stopAllSounds();
                overlay.stopShowing();
                Screens.showEndScreen(gameView, "Sie haben " + points + " Punkte erreicht!");
                startNewGame();
            }

        } else if (endOfLevel()) {
            gameView.stopAllSounds();
            if (!overlay.isMessageShown()) {
                overlay.showMessage("Great Job!");
            }

            if (overlay.isMessageShown() && gameView.timer(2000, 0, this)) {
                overlay.stopShowing();
                switchToNextLevel();
                initializeLevel();
            }

        } else if (lives < currentLives) { // player has crashed
            initializeLevel();
            currentLives = lives;
        }
    }


    private boolean endOfLevel() {
        return gameView.gameTimeInMilliseconds() >= levelStartTimestamp + level.levelDurationTimestamp;
    }

    private boolean endOfGame() {
        return lives == 0 || (!hasNextLevel() && endOfLevel());
    }

    @Override
    protected void initializeLevel() {
        super.initializeLevel();
        overlay.showMessage(level.name, 2);
        gameView.stopAllSounds();

        levelStartTimestamp = gameView.gameTimeInMilliseconds();
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
