package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.unmovable.Footer;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.*;
import thd.game.level.*;

import java.awt.event.KeyEvent;

class UserControlledGameObjectPool {
    protected final GameView gameView;

    protected ZaxxonFighter zaxxonFighter;
    protected HeightStatusBar heightStatusBar;
    protected FuelCellGauge fuelCellGauge;
    protected Footer footer;

    protected Level level;

    UserControlledGameObjectPool(GameView gameView) {
        this.gameView = gameView;
    }

    protected void gameLoop() {
        Integer[] pressedKeys = gameView.keyCodesOfCurrentlyPressedKeys();

        for (int keyCode : pressedKeys) {
            processKeyCode(keyCode);
        }
    }

    private void processKeyCode(int keyCode) {
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            zaxxonFighter.left();
        } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            zaxxonFighter.right();
        } else if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            zaxxonFighter.up();
        } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            zaxxonFighter.down();
        } else if (keyCode == KeyEvent.VK_SPACE) {
            zaxxonFighter.shoot();
        }
    }
}
