package game;

import java.awt.*;

class HeightStatusBar {
    private GameView gameView;
    private Position position;
    private double size;
    private double rotation;
    private double width;
    private double height;

    HeightStatusBar(GameView gameView) {
        this.gameView = gameView;
        size = 30;
        rotation = 0;
        width = 150;
        height = 33;
        position = new Position(GameView.WIDTH-width, 0);
    }

    public void addToCanvas() {
        gameView.addTextToCanvas("Objekt 3", position.getX(), position.getY(),
                size, true, Color.YELLOW, rotation);
    }

    @Override
    public String toString() {
        return "HeightStatusBar: " + position;
    }
}
