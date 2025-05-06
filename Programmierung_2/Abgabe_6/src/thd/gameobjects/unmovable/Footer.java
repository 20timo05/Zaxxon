package thd.gameobjects.unmovable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;

import java.awt.*;

/**
 * Displays the footer with various status bars.
 */
public class Footer extends GameObject {

    private static final int FLAG_DISPLAY_SIZE = 4;
    private static final int LIVE_DISPLAY_SIZE = 9;
    private final int flagDisplayWidth;
    private final int liveDisplayWidth;
    private static final int FONT_SIZE = 30;

    /**
     * Crates a new Footer.
     *
     * @param gameView GameView to show the Footer on.
     * @param gamePlayManager   reference to the gamePlayManager
     */
    public Footer(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);

        flagDisplayWidth = LevelDisplayBlockImages.FLAG.split("\n")[0].length() * FLAG_DISPLAY_SIZE;
        liveDisplayWidth = LiveDisplayBlockImages.LIVE_DISPLAY.split("\n")[LiveDisplayBlockImages.LIVE_DISPLAY.split("\n").length - 1].length() * LIVE_DISPLAY_SIZE;
    }

    /**
     * Draws the Footer to the canvas.
     */
    @Override
    public void addToCanvas() {
        gameView.addRectangleToCanvas(0, GameSettings.GAME_HEIGHT, GameView.WIDTH, GameSettings.FOOTER_HEIGHT, 1, true, Color.BLACK);

        // add lettering to FuelCellGauge
        gameView.addTextToCanvas("FUEL E                           F", 70, GameSettings.GAME_HEIGHT + GameSettings.FOOTER_HEIGHT * 0.5, GameSettings.FONT_SIZE, false, Color.WHITE, 0, GameSettings.FONT_FAMILY);

        // add live display symbols to the bottom left of the footer
        for (int i = 0; i < gamePlayManager.getLives(); i++) {
            gameView.addBlockImageToCanvas(LiveDisplayBlockImages.LIVE_DISPLAY, 50 + liveDisplayWidth*1.5*i, GameSettings.GAME_HEIGHT + GameSettings.FOOTER_HEIGHT * 0.2, LIVE_DISPLAY_SIZE, 0);
        }

        // add flags to the bottom right of the footer
        for (int i = 0; i < gamePlayManager.getLevel(); i++) {
            gameView.addBlockImageToCanvas(LevelDisplayBlockImages.FLAG, GameView.WIDTH - 200 + flagDisplayWidth * i, GameSettings.GAME_HEIGHT + GameSettings.FOOTER_HEIGHT * 0.5, FLAG_DISPLAY_SIZE, 0);
        }

        gameView.addTextToCanvas("SCORE  " + gamePlayManager.getPoints(), GameView.WIDTH * 0.35, GameSettings.GAME_HEIGHT + 20, FONT_SIZE, false, Color.WHITE, 0, GameSettings.FONT_FAMILY);

        // add Enemy Planes counter
        String epText = String.format("EP = %02d", gamePlayManager.getNumberOfEnemyPlanes());
        gameView.addTextToCanvas(epText, GameView.WIDTH - epText.length()*FONT_SIZE - 75, GameSettings.GAME_HEIGHT + 20, FONT_SIZE, false, Color.WHITE, 0, GameSettings.FONT_FAMILY);
    }
}