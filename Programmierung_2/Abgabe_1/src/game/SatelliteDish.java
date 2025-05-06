package game;

import java.awt.*;

public class SatelliteDish {
    private GameView gameView;
    private Position position;
    private double speedInPixel;
    private double size;
    private double rotation;
    private double width;
    private double height;

    public SatelliteDish(GameView gameView) {
        this.gameView = gameView;
        position = new Position(1100, 650);
        speedInPixel = 2;
        size = 30;
        rotation = 0;
        width = 150;
        height = 33;
    }

    public void updatePosition() {
        position.left(speedInPixel);
    }

    public void addToCanvas() {
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width, height,
                5, false, Color.WHITE);
        gameView.addRectangleToCanvas(position.getX(), position.getY(), width, height,
                0, true, Color.GREEN);
        gameView.addTextToCanvas("Objekt 2", position.getX(), position.getY(),
                size, true, Color.BLUE, rotation);
    }

    @Override
    public String toString() {
        return "SatelliteDish: " + position;
    }
}
