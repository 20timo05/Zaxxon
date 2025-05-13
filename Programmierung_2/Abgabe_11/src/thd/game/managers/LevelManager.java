package thd.game.managers;

import thd.game.level.*;
import thd.game.utilities.GameView;

import java.util.List;

class LevelManager extends GameWorldManager {
    private List<Level> levels;
    private static final int LIVES_STANDARD = 2;
    private static final int LIVES_EASY = 3;

    protected LevelManager(GameView gameView) {
        super(gameView);
    }

    @Override
    protected void initializeLevel() {
        super.initializeLevel();
        initializeGameObjects();
    }

    protected void initializeGame() {
        levels = List.of(new Level1(), new Level2(), new Level3());
        level = levels.get(0);
        lives = Level.difficulty == Difficulty.EASY ? LIVES_EASY : LIVES_STANDARD;
        points = 0;
    }

    protected boolean hasNextLevel() {
        return level.number < levels.size();
    }

    protected void switchToNextLevel() {
        if (!hasNextLevel()) {
            throw new NoMoreLevelsAvailableException("No more levels available!");
        }

        level = levels.get(level.number);
    }

    private void initializeGameObjects() {
        /*
        Die Methode initializeGameObjects() soll in Zukunft dazu genutzt werden, um Spielelemente an ein
        neues Level anzupassen, z.B.
            o Anpassungen für das Level am Hintergrund machen.
            o Die Lebensanzeige aktualisieren.
            o Den Punktestand aus dem vorherigen Level übernehmen.
            o Einen Countdown neu starten.
         */
        fuelInterpolation = 1;
        numberOfEnemyPlanes = 20;
    }
}
