package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.game.utilities.GeometricUtils;
import thd.gameobjects.base.*;

/**
 * A hazardous Enemy Game Object that spawns in the Motherbase. The Rocket is launched from the corresponding {@link VerticalRocketHole}.
 * The Rocket should either be dodged or shot at. It yields 150 Points upon destruction.
 */
public class VerticalRocket extends ExplodingGameObject implements ShiftableGameObject {
    private final VerticalRocketHole verticalRocketHole;
    private double altitudeInterpolation;

    /**
     * Creates a new {@code VerticalRocket}.
     *
     * @param gameView           GameView to show the game object on.
     * @param gamePlayManager    reference to the gamePlayManager
     * @param verticalRocketHole reference to the corresponding {@link VerticalRocketHole}
     */
    VerticalRocket(GameView gameView, GamePlayManager gamePlayManager, VerticalRocketHole verticalRocketHole) {
        super(gameView, gamePlayManager, 0, true);

        this.verticalRocketHole = verticalRocketHole;

        height = 19;
        width = 10;
        size = 4;
        speedInPixel = 2;
        altitudeInterpolation = 0;

        updatePosition(); // initialize start and target position
        hitBoxOffsets(-width * size / 2, -height * size / 2, 0, 0);
    }

    @Override
    public void updatePosition() {
        Position holePos = verticalRocketHole.getPosition();

        Vector2d targetPos = new Vector2d(holePos);
        targetPos.up(GameSettings.MAX_PLAYER_ALTITUDE);
        this.targetPosition.updateCoordinates(targetPos);

        position.updateCoordinates(GeometricUtils.interpolatePosition(holePos, targetPos, altitudeInterpolation));

        position.moveToPosition(targetPosition, speedInPixel);
        altitudeInterpolation = 1 - position.distance(targetPosition) / GameSettings.MAX_PLAYER_ALTITUDE;
        altitudeLevel = (int) (altitudeInterpolation * MAX_ALTITUDE_LEVEL);
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (other instanceof ZaxxonFighterLaserShot || other instanceof ZaxxonFighter) {
            gamePlayManager.destroyGameObject(this);

            if (other instanceof ZaxxonFighterLaserShot) {
                gamePlayManager.addPoints(150);
            }
        }
    }

    @Override
    public void updateStatus() {
        super.updateStatus();

        if (position.similarTo(targetPosition)) {
            gamePlayManager.destroyGameObject(this);
        }
    }

    /**
     * Renders {@code LaserShot} object as a BlockImage on {@code gameView}.
     *
     * @see GameView#addBlockImageToCanvas
     */
    @Override
    public void addToCanvas() {
        super.addToCanvas();

        if (!hasDespawned) {
            Position mid = calcMiddlePoint();
            gameView.addBlockImageToCanvas(VerticalRocketBlockImages.ROCKET, mid.getX(), mid.getY(), size, rotation);
        }
    }
}
