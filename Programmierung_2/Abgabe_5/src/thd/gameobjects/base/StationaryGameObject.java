package thd.gameobjects.base;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * Represents a stationary Enemy Game Object in the Game.
 * The Player can either shoot it or has to dodge it.
 */
public abstract class StationaryGameObject extends GameObject{
    protected final double inter;
    /**
     * Creates a new GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param inter             interpolation factor: where to spawn the object
     */
    public StationaryGameObject(GameView gameView, GamePlayManager gamePlayManager, double inter) {
        super(gameView, gamePlayManager);
        this.inter = inter;

        speedInPixel = GameSettings.SPEED_IN_PIXEL;

        StationaryMovementPattern stationaryMovementPattern = new StationaryMovementPattern(inter);
        position.updateCoordinates(stationaryMovementPattern.startPosition());
        targetPosition.updateCoordinates(stationaryMovementPattern.nextPosition());
    }

    /**
     * Moves {@code StationaryGameObject} object forward.
     */
    @Override
    public void updatePosition() {
        position.moveToPosition(targetPosition, speedInPixel);
    }

    /**
     * Automatically despawns the GameObjects.
     */
    @Override
    public void updateStatus() {
        if (position.similarTo(targetPosition)) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    /*
    @Override
    public void drawHitbox() {
        Position mid = calcMiddlePoint();
        gameView.addRectangleToCanvas(mid.getX(), mid.getY(), getWidth(), getHeight(), 1, false, Color.blue);
    }
     */

    /**
     * calculates how much percent of the way the GameObject is.
     *
     * @return the interpolation factor
     */
    public double calcInterpolation() {
        return 1 - position.distance(targetPosition) / TRAVEL_PATH_CALCULATOR.getDistanceToDespawnLine();
    }
}
