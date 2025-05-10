package thd.game.bin;

import thd.game.managers.GameViewManager;

/**
 * Contains main function, that starts the game.
 */
public class StartGame {
    /**
     * Starts the game.
     *
     * @param args cmd arguements
     */
    public static void main(String[] args) {
        new GameViewManager();
    }
}