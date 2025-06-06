package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.game.utilities.GeometricUtils;
import thd.gameobjects.base.*;

import thd.game.utilities.TravelPathCalculator;

/**
 * The {@code GunEmplacementLaserShot} is a {@code GameObject} that is emitted from the {@link EnemyShooter} and can destroy the {@code ZaxxonFighter}.
 * They can't change direction as they are shot either straight or across.
 *
 * @see GameObject
 */
class GunEmplacementLaserShot extends CollidingGameObject implements ShiftableGameObject {
    private static final int MAX_LASER_SHOT_LENGTH = 500;

    private final GunEmplacement gunEmplacement;
    private final boolean orientation;

    /**
     * Creates a new {@code GunEmplacementLaserShot} GameObject.
     *
     * @param gameView        GameView to show the game object on.
     * @param gamePlayManager reference to the gamePlayManager
     * @param gunEmplacement  the corresponding {@link GunEmplacement}
     * @param orientation     true -> straight shot, false -> shot to the left
     */
    GunEmplacementLaserShot(
            GameView gameView,
            GamePlayManager gamePlayManager,
            GunEmplacement gunEmplacement,
            boolean orientation
    ) {
        super(gameView, gamePlayManager, 0, true);

        this.orientation = orientation;
        this.gunEmplacement = gunEmplacement;

        height = 8;
        width = 16;
        size = 4;
        speedInPixel = 7;

        calculatePosition();
        hitBoxOffsets(-width * size / 2, -height * size / 2, 0, 0);
    }


    /**
     * Moves this {@code LaserShot} object forward.
     */
    @Override
    public void updatePosition() {
        position.moveToPosition(targetPosition, speedInPixel);
    }

    @Override
    public void updateStatus() {
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
        Position mid = calcMiddlePoint();
        if (orientation) {
            gameView.addBlockImageToCanvas(GunEmplacementBlockImages.LASER_STRAIGHT, mid.getX(), mid.getY(), size, rotation);
        } else {
            gameView.addBlockImageToCanvas(GunEmplacementBlockImages.LASER_ACROSS, mid.getX(), mid.getY(), size, rotation);
        }

    }


    private void calculatePosition() {
        position.updateCoordinates(gunEmplacement.calcMiddlePoint());

        // translate, so that Laser comes out of Firing Shaft
        if (orientation) {
            position.left(getWidth());
            position.down(gunEmplacement.getHeight());
        } else {
            position.left(getWidth());
            position.up(getHeight());
        }


        // calculate the furthest point away (e.g. a Wall or Despawn Line)
        double laserPathAngle = orientation ? Math.toRadians(-180 + GameSettings.MOVEMENT_ANGLE_IN_DEGREE) : Math.toRadians(180 - GameSettings.MOVEMENT_ANGLE_IN_DEGREE);
        Vector2d[] laserPathParametric = new Vector2d[]{
                new Vector2d(position),
                new Vector2d(1, laserPathAngle)
        };

        Vector2d newTargetPosition;
        // straight
        if (orientation) {
            newTargetPosition = new Vector2d(
                    GeometricUtils.calculateIntersection(TravelPathCalculator.copyDespawnLine(), laserPathParametric)
            );

        } else {
            // left
            Position[] leftTravelPathBorder = new Position[]{
                    TravelPathCalculator.copySpawnLine()[0],
                    TravelPathCalculator.copyDespawnLine()[0]
            };
            newTargetPosition = new Vector2d(
                    GeometricUtils.calculateIntersection(leftTravelPathBorder, laserPathParametric)
            );

        }

        // limit distance to target Position by MAX_LASER_SHOT_LENGTH
        if (position.distance(newTargetPosition) > MAX_LASER_SHOT_LENGTH) {
            newTargetPosition.subtract(position);
            newTargetPosition.scaleToMagnitude(MAX_LASER_SHOT_LENGTH);
            newTargetPosition.add(position);
        }

        targetPosition.updateCoordinates(newTargetPosition);
    }

    @Override
    public void reactToCollisionWith(CollidingGameObject other) {
        if (getAltitudeLevel() == other.getAltitudeLevel()) {
            gamePlayManager.destroyGameObject(this);
        }
    }
}
