package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.unmovable.Footer;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.*;

import java.awt.event.KeyEvent;

class UserControlledGameObjectPool {
    protected final GameView gameView;

    protected ZaxxonFighter zaxxonFighter;
    protected GunImplacement gunImplacement;
    protected RadarTower radarTower;
    protected HeightStatusBar heightStatusBar;
    protected FuelTank fuelTank;
    protected EnemyShooter enemyShooter;
    protected FuelCellGauge fuelCellGauge;
    protected Footer footer;
    protected EnergyBarrierTower energyBarrier;

    UserControlledGameObjectPool(GameView gameView) {
        this.gameView = gameView;

    }

    protected void gameLoop() {
        Integer[] pressedKeys = gameView.keyCodesOfCurrentlyPressedKeys();
        KeyEvent[] typedKeys = gameView.typedKeys();

        for (int keyCode : pressedKeys) {
            processKeyCode(keyCode);
        }

        for (KeyEvent typedKey : typedKeys) {
            processTypedKey(typedKey);
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

    private void processTypedKey(KeyEvent typedKey) {
        if (typedKey.getKeyCode() == KeyEvent.VK_SPACE) {
            if (typedKey.getID() == KeyEvent.KEY_RELEASED) {
                zaxxonFighter.releaseShoot();
            }
        }
    }
}
