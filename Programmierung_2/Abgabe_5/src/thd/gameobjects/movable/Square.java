package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;

import java.awt.*;

public class Square extends GameObject {
    public Square(GameView gameView, GamePlayManager gamePlayManager) {
        super(gameView, gamePlayManager);

        height = 30;
        width = 30;
        size = 1;

        position.updateCoordinates(100, 100);
        speedInPixel = 5;
    }

    @Override
    public void updateStatus() {
        if (position.getX() > GameView.WIDTH) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    @Override
    public void updatePosition() {
        position.right(speedInPixel);
    }

    @Override
    public void addToCanvas() {
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width, height, 3, false, Color.RED);
    }

    @Override
    public String toString() {
        return "Square: " + position;
    }
}
