package thd.gameobjects.movable;

import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

import java.awt.*;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * The {@code ZaxxonFighter} can be controlled by the Player.
 * Handles isometric movement (left, right, up, down). It can also shoot at oncoming GameObjects in order to get Points &
 * Fuel. It has to avoid obstacles and enemies.
 *
 * @see GameObject
 */
public class ZaxxonFighter extends GameObject {
    private final Vector2d movementVector;
    private final Position preProjectionPosition;
    private final Vector2d projectedPosition;

    private double playerAltitudeInterpolation;
    private double playerMovementInterpolation;
    private boolean shotInProgress;

    /**
     * Initializes a {@code ZaxxonFighter} object.
     *
     * @param gameView to display the {@code RadarTower} object on
     */
    public ZaxxonFighter(GameView gameView) {
        super(gameView);

        speedInPixel = 3;
        size = 4;
        height = 13;
        width = 24;
        shotInProgress = false;

        movementVector = new Vector2d();
        preProjectionPosition = new Position(0, 0);
        projectedPosition = new Vector2d();

        playerAltitudeInterpolation = 0;
        playerMovementInterpolation = 0;
    }

    @Override
    public void updatePosition() {
        clipMovementVector(movementVector);
        movementVector.normalize(); // normalize, so velocity is same in all directions

        preProjectionPosition.right(movementVector.getX() * speedInPixel);
        preProjectionPosition.up(movementVector.getY() * speedInPixel);

        // projection from 2d to isometric "3d" Position
        projectedPosition.updateCoordinates(preProjectionPosition);
        projectedPosition.matrixMultiplication(TRAVEL_PATH_CALCULATOR.getIsometricProjectionMatrix());
        projectedPosition.add(TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[0]);

        position.updateCoordinates(projectedPosition);
        movementVector.updateCoordinates(0, 0);

        playerAltitudeInterpolation = preProjectionPosition.getY() / (GameSettings.MAX_PLAYER_ALTITUDE - height * size);
        playerMovementInterpolation = preProjectionPosition.getX() / (TRAVEL_PATH_CALCULATOR.getTravelPathWidth() - width * size);

        // @TODO delete - Increasing the size of the player every 5s causes problems. Delete once possible!
        playerAltitudeInterpolation = Math.min(Math.max(playerAltitudeInterpolation, 0), 1);
        playerMovementInterpolation = Math.min(Math.max(playerMovementInterpolation, 0), 1);
    }

    /**
     * Renders {@code ZaxxonFighter} object as .png Image on {@code gameView}.
     *
     * @see GameView#addImageToCanvas
     */
    @Override
    public void addToCanvas() {
        Vector2d shadowPosition = new Vector2d(TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[0]);
        shadowPosition.interpolatePosition(TRAVEL_PATH_CALCULATOR.getPlayerMovementLine()[1], playerMovementInterpolation);

        gameView.addBlockImageToCanvas(ZaxxonFighterBlockImages.ZAXXON_FIGHTER_SHADOW, shadowPosition.getX(), shadowPosition.getY(), 3, 0);

        if (shotInProgress) {
            Position relativeXPos = new Position(width * size / 2 - height * size / 2, 0);
            gameView.addTextToCanvas("X", position.getX() + relativeXPos.getX(), position.getY() + relativeXPos.getY(), height * size, false, Color.WHITE, 0, GameSettings.FONT_FAMILY);
        } else {
            gameView.addBlockImageToCanvas(ZaxxonFighterBlockImages.ZAXXON_FIGHTER_STRAIGHT, position.getX(), position.getY(), size, 0);
        }
    }

    /**
     * Moves the {@code ZaxxonFighter} left by {@link #speedInPixel}px.
     */
    public void left() {
        movementVector.left(speedInPixel);
    }

    /**
     * Moves the {@code ZaxxonFighter} right by {@link #speedInPixel}px.
     */
    public void right() {
        movementVector.right(speedInPixel);
    }

    /**
     * Moves the {@code ZaxxonFighter} up by {@link #speedInPixel}px.
     */
    public void up() {
        movementVector.up(speedInPixel);
    }

    /**
     * Moves the {@code ZaxxonFighter} down by {@link #speedInPixel}px.
     */
    public void down() {
        movementVector.down(speedInPixel);
    }

    /**
     * Enables Shoot Mode, so that an X is displayed instead of the Player.
     */
    public void shoot() {
        shotInProgress = true;
    }

    /**
     * Disables Shoot Mode, so that the Player is display again (not an X).
     */
    public void releaseShoot() {
        shotInProgress = false;
    }

    @Override
    public void updateStatus() {
        if (gameView.timer(5000, 0, this)) {
            size++;
        }
    }

    /**
     * Returns the linear interpolation factor of the height.
     * <ul>
     *     <li>0 corresponds to being in the lowest position</li>
     *     <li>1 corresponds to being in the highest position</li>
     * </ul>
     *
     * @return the linear interpolation factor
     */
    public double getPlayerAltitudeInterpolation() {
        return playerAltitudeInterpolation;
    }

    /**
     * String representation of {@code ZaxxonFighter} object.
     *
     * @return String representation
     * @see Position#toString()
     */
    @Override
    public String toString() {
        return "ZaxxonFighter: " + position;
    }

    private void clipMovementVector(Position movementVector) {
        // clip Movement, so that it Player stays inside valid Movement Range
        if (
                preProjectionPosition.getX() + movementVector.getX() <= 0
                        || preProjectionPosition.getX() + movementVector.getX() >= TRAVEL_PATH_CALCULATOR.getTravelPathWidth() - width * size
        ) {
            movementVector.updateCoordinates(0, movementVector.getY());
        }

        if (
                preProjectionPosition.getY() - movementVector.getY() <= 0
                        || preProjectionPosition.getY() - movementVector.getY() >= GameSettings.MAX_PLAYER_ALTITUDE - height * size
        ) {
            movementVector.updateCoordinates(movementVector.getX(), 0);
        }
    }
}
