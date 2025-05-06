package thd.gameobjects.unmovable;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;

import static thd.gameobjects.unmovable.HeightStatusBarBlockImages.HEIGHT_BAR_FLAG;


/**
 * A status bar, that displays the current height of the Player.
 */
public class HeightStatusBar extends GameObject {
    /**
     * Initializes a {@code HeightStatusBar} object.
     *
     * @param gameView to display the {@code HeightStatusBar} object on
     */
    public HeightStatusBar(GameView gameView) {
        super(gameView);

        size = 10;
        rotation = 0;
        width = HEIGHT_BAR_FLAG.split("\n")[0].length() * size;
        height = HEIGHT_BAR_FLAG.split("\n").length * size;
        position.updateCoordinates(new Position(0, GameView.HEIGHT / 2.0 - height / 2.0));
    }

    /**
     * Renders {@code HeightStatusBar} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        gameView.addBlockImageToCanvas(HEIGHT_BAR_FLAG, position.getX(), position.getY(), size, rotation);
    }

    /**
     * String representation of {@code HeightStatusBar} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "HeightStatusBar: " + position;
    }
}
