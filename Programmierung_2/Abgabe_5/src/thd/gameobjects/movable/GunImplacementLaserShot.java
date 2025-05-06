package thd.gameobjects.movable;

import thd.game.managers.GamePlayManager;
import thd.game.managers.GameSettings;
import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Position;
import thd.gameobjects.base.Vector2d;

import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

/**
 * The {@code GunImplacementLaserShot} is a {@code GameObject} that is emitted from the {@link EnemyShooter} and can destroy the {@code ZaxxonFighter}.
 * They can't change direction as they are shot either straight or across.
 *
 * @see GameObject
 */
class GunImplacementLaserShot extends GameObject {
    private static final int MAX_LASER_SHOT_LENGTH = 500;

    private final GunImplacement gunImplacement;
    private final boolean orientation;

    /**
     * Creates a new {@code GunImplacementLaserShot} GameObject.
     *
     * @param gameView          GameView to show the game object on.
     * @param gamePlayManager   reference to the gamePlayManager
     * @param gunImplacement the corresponding {@link GunImplacement}
     * @param orientation    true -> straight shot, false -> shot to the left
     */
    GunImplacementLaserShot(
            GameView gameView,
            GamePlayManager gamePlayManager,
            GunImplacement gunImplacement,
            boolean orientation
    ) {
        super(gameView, gamePlayManager);

        this.orientation = orientation;
        this.gunImplacement = gunImplacement;

        height = 8;
        width = 16;
        size = 3;
        speedInPixel = 7;

        calculatePosition();
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
        if (orientation) {
            gameView.addBlockImageToCanvas(GunImplacementBlockImages.LASER_STRAIGHT, position.getX(), position.getY(), size, rotation);
        } else {
            gameView.addBlockImageToCanvas(GunImplacementBlockImages.LASER_ACROSS, position.getX(), position.getY(), size, rotation);
        }

    }


    private void calculatePosition() {
        position.updateCoordinates(gunImplacement.calcMiddlePoint());

        // translate, so that Laser comes out of Firing Shaft
        if (orientation) {
            position.left(getWidth());
            position.down(gunImplacement.getHeight());
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
                    geometricUtils.calculateIntersection(TRAVEL_PATH_CALCULATOR.getDespawnLine(), laserPathParametric)
            );

        } else {
            // left
            Position[] leftTravelPathBorder = new Position[]{
                    TRAVEL_PATH_CALCULATOR.getSpawnLine()[0],
                    TRAVEL_PATH_CALCULATOR.getDespawnLine()[0]
            };
            newTargetPosition = new Vector2d(
                    geometricUtils.calculateIntersection(leftTravelPathBorder, laserPathParametric)
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
}
