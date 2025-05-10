package thd.gameobjects.base;

import thd.game.managers.GamePlayManager;
import thd.game.utilities.GameView;
import thd.gameobjects.movable.*;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Arrays;
import java.util.Objects;

/**
 * Game objects that are able to collide with other game objects.
 */
public abstract class CollidingGameObject extends GameObject {
    private Rectangle hitBoxRectangle;
    private Polygon[] relativeHitboxPolygons;
    private Polygon[] absoluteHitboxPolygons;

    private double hitBoxOffsetX;
    private double hitBoxOffsetY;
    private double hitBoxOffsetWidth;
    private double hitBoxOffsetHeight;

    protected int altitudeLevel;

    /**
     * The maximum altitude level any GameObject can be.
     */
    public static final int MAX_ALTITUDE_LEVEL = 5;

    /**
     * Creates a new game object that is able to collide.
     *
     * @param gameView              Window to show the game object on.
     * @param gamePlayManager       Controls the game play.
     * @param altitudeLevel         the altitude of the GameObject
     * @param isRectangular         if true: use rectangular hitbox, else use polygonal
     * @param spawnDelayInMilis            measure for how long before GameObject enters the Screen
     * @param spawnLineInter        interpolation factor: where on the SpawnLine to spawn the object
     */
    public CollidingGameObject(GameView gameView, GamePlayManager gamePlayManager, int altitudeLevel, boolean isRectangular, int spawnDelayInMilis, double spawnLineInter) {
        super(gameView, gamePlayManager, spawnDelayInMilis, spawnLineInter);
        initializer(altitudeLevel, isRectangular);
    }

    /**
     * Crates a new game object that is able to collide.
     *
     * @param gameView        Window to show the game object on.
     * @param gamePlayManager Controls the game play.
     * @param altitudeLevel   the altitude of the GameObject
     * @param isRectangular   if true: use rectangular hitbox, else use polygonal
     */
    public CollidingGameObject(GameView gameView, GamePlayManager gamePlayManager, int altitudeLevel, boolean isRectangular) {
        super(gameView, gamePlayManager);
        initializer(altitudeLevel, isRectangular);
    }

    private void initializer(int altitudeLevel, boolean isRectangular) {
        if (isRectangular) {
            hitBoxRectangle = new Rectangle(0, 0, 0, 0);
            this.relativeHitboxPolygons = null;

        } else {
            hitBoxRectangle = null;
            relativeHitboxPolygons = new Polygon[0];
            absoluteHitboxPolygons = new Polygon[0];
        }
        this.altitudeLevel = altitudeLevel;
        distanceToBackground = (char) (altitudeLevel + 1);
    }

    private boolean isEnemyGameObject(CollidingGameObject obj) {
        return
                obj instanceof EnemyShooter
                || obj instanceof EnergyBarrier
                || obj instanceof FuelTank
                || obj instanceof GunEmplacement
                || obj instanceof RadarTower
                || obj instanceof WallRow
                || obj instanceof VerticalRocket;
    }

    /**
     * Determines if this game object currently collides with the other game object. Both hitboxes are updated before
     * detection.
     *
     * @param other The other game object.
     * @return <code>true</code> if the there was a collision.
     */
    public final boolean collidesWith(CollidingGameObject other) {
        updateHitBox();
        other.updateHitBox();

        if (getAltitudeLevel() != other.getAltitudeLevel()) {
            return false;
        }

        // optimization
        if (isEnemyGameObject(this) && isEnemyGameObject(other)) {
            return false;
        }

        // Case 1: Compare two rectangular hitboxes
        if (hitBoxRectangle != null && other.hitBoxRectangle != null) {
            return hitBoxRectangle.intersects(other.hitBoxRectangle);
        }

        // Case 2: one or both hitboxes are polygonal
        // Polygon class has a predefined method for this (intersects(Rectangle2D r)), but it is not precise enough
        // use more general Area-based intersection instead
        // @OPTIMIZE only perform heavy multi-polygon check when the player is on the same level then wall
        Area area1 = hitBoxRectangle != null ? hitboxToArea(hitBoxRectangle) : hitboxToArea(absoluteHitboxPolygons);
        Area area2 = other.hitBoxRectangle != null ? hitboxToArea(other.hitBoxRectangle) : hitboxToArea(other.absoluteHitboxPolygons);
        area1.intersect(area2);

        return !area1.isEmpty();
    }

    private Area hitboxToArea(Rectangle hitbox) {
        return new Area(hitbox);
    }

    private Area hitboxToArea(Polygon[] hitboxes) {
        Area combinedArea = new Area();
        for (Polygon hitbox: hitboxes) {
            combinedArea.add(new Area(hitbox));
        }
        return combinedArea;
    }

    private void updateHitBox() {
        if (hitBoxRectangle != null) {
            // update rectangular hitbox
            hitBoxRectangle.x = (int) (position.getX() + hitBoxOffsetX);
            hitBoxRectangle.y = (int) (position.getY() + hitBoxOffsetY);
            hitBoxRectangle.width = (int) (width*size + hitBoxOffsetWidth);
            hitBoxRectangle.height = (int) (height*size + hitBoxOffsetHeight);

        } else {
            // @OPTIMIZE move absolute hitbox simlar to Position - one step in direction of targetPosition
            // update polygonal hitbox
            absoluteHitboxPolygons = new Polygon[relativeHitboxPolygons.length];

            for (int polyIdx = 0; polyIdx < relativeHitboxPolygons.length; polyIdx++) {

                Polygon absoluteHitbox = offsetToAbsoluteHitbox(relativeHitboxPolygons[polyIdx]);
                absoluteHitboxPolygons[polyIdx] = absoluteHitbox;
            }
        }
    }

    private Polygon offsetToAbsoluteHitbox(Polygon relativeHitbox) {
        Polygon absoluteHitbox = new Polygon();

        for (int cornerIdx = 0; cornerIdx < relativeHitbox.npoints; cornerIdx++) {
            absoluteHitbox.addPoint(
                    (int) (getPosition().getX() + hitBoxOffsetX + relativeHitbox.xpoints[cornerIdx]),
                    (int) (getPosition().getY() + hitBoxOffsetY + relativeHitbox.ypoints[cornerIdx])
            );
        }
        return absoluteHitbox;
    }

    /**
     * Determines position and size of the hitbox relatively to the position and size of the game object.
     *
     * @param offsetX      x-coordinate, relative to the game objects' x-coordinate.
     * @param offsetY      y-coordinate, relative to the game objects' y-coordinate.
     * @param offsetWidth  Width, relative to the game objects' width.
     * @param offsetHeight Height, relative to the game objects' height.
     */
    protected void hitBoxOffsets(double offsetX, double offsetY, double offsetWidth, double offsetHeight) {
        this.hitBoxOffsetX = offsetX;
        this.hitBoxOffsetY = offsetY;
        this.hitBoxOffsetWidth = offsetWidth;
        this.hitBoxOffsetHeight = offsetHeight;
    }

    /**
     * If a game object is collided with another game object, it reacts to the collision. This method needs to be
     * overridden by game objects and implemented with appropriate reactions.
     *
     * @param other The other game object that is involved in the collision.
     */
    public abstract void reactToCollisionWith(CollidingGameObject other);

    /**
     * Shows hitbox of this game object as a red rectangle.
     */
    public void showHitBox() {
        if (hitBoxRectangle != null) {
            if (hitBoxRectangle.width > 0 && hitBoxRectangle.height > 0) {
                gameView.addRectangleToCanvas(hitBoxRectangle.x, hitBoxRectangle.y, hitBoxRectangle.width, hitBoxRectangle.height, 2, false, Color.RED);
            }
        } else {
            for (Polygon hitbox : absoluteHitboxPolygons) {
                gameView.addPolygonToCanvas(castToDoubleArray(hitbox.xpoints), castToDoubleArray(hitbox.ypoints), 2, false, Color.RED);
            }
        }
    }

    private double[] castToDoubleArray(int[] intArr) {
        double[] doubleArr = new double[intArr.length];
        for (int i = 0; i < intArr.length; i++) {
            doubleArr[i] = intArr[i];
        }
        return doubleArr;
    }

    /*
     * Uses the 2d hitbox calculted by height*size and width*size and projects it into "3d" isometric perspective.
     *
     * @param projectionMatrix the matrix to use for projection
     * @return the projected Hitbox as a {@link Polygon}
     *
     * protected Polygon calculateDefaultRelativeProjectedHitbox(double[][] projectionMatrix) {
     *         // define default hitbox in 2d
     *         Position[] defaultPreProjectionRelativeHitbox = new Position[] {
     *                 new Position(0, 0),
     *                 new Position(width*size, 0),
     *                 new Position(width*size, -height*size),
     *                 new Position(0, -height*size)
     *         };
     *
     *         return calculateRelativeProjectedHitbox(defaultPreProjectionRelativeHitbox, projectionMatrix);
     *     }
     */

    /**
     * Projects any 2d hitbox and projects it into "3d" isometric perspective.
     *
     * @param preProjectionRelativeHitbox the hitbox in 2d before projection
     * @param projectionMatrix the matrix to use for projection
     * @return the projected Hitbox as a {@link Polygon}
     */
    protected Polygon calculateRelativeProjectedHitbox(Position[] preProjectionRelativeHitbox, double[][] projectionMatrix) {
        // project hitbox into isometric "3d"
        Polygon postProjectionHitbox = new Polygon();
        for (Position corner : preProjectionRelativeHitbox) {
            Vector2d projected = new Vector2d(corner);
            projected.matrixMultiplication(projectionMatrix);

            postProjectionHitbox.addPoint((int) projected.getX(), (int) projected.getY());
        }

        return postProjectionHitbox;
    }

    /**
     * Returns the altitude level.
     *
     * @return the altitude level
     */
    public int getAltitudeLevel() {
        return altitudeLevel;
    }

    /**
     * Setter for the relative Polygonal Hitboxes which is an Array of Polygons that all together mark one Hitbox.
     *
     * @param relativeHitboxPolygons the array of polygons
     */
    public void setRelativeHitboxPolygons(Polygon[] relativeHitboxPolygons) {
        this.relativeHitboxPolygons = relativeHitboxPolygons;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        CollidingGameObject other = (CollidingGameObject) o;

        return
                hitBoxRectangle == null ? other.hitBoxRectangle == null : hitBoxRectangle.equals(other.hitBoxRectangle)
                && comparePolygonArray(relativeHitboxPolygons, other.relativeHitboxPolygons)
                && comparePolygonArray(absoluteHitboxPolygons, other.absoluteHitboxPolygons)
                && Double.compare(hitBoxOffsetX, other.hitBoxOffsetX) == 0
                && Double.compare(hitBoxOffsetY, other.hitBoxOffsetY) == 0
                && Double.compare(hitBoxOffsetWidth, other.hitBoxOffsetWidth) == 0
                && Double.compare(hitBoxOffsetHeight, other.hitBoxOffsetHeight) == 0
                && altitudeLevel == other.altitudeLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                hitBoxRectangle,
                Arrays.hashCode(relativeHitboxPolygons),
                Arrays.hashCode(absoluteHitboxPolygons),
                hitBoxOffsetX,
                hitBoxOffsetY,
                hitBoxOffsetWidth,
                hitBoxOffsetHeight,
                altitudeLevel
        );
    }

    private boolean comparePolygonArray(Polygon[] arr1, Polygon[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        } else if (arr1 == null || arr2 == null) {
            return false;
        }

        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (!arr1[i].equals(arr2[i])) {
                return false;
            }
        }

        return true;
    }
}