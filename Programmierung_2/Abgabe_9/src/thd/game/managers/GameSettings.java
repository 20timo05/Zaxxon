package thd.game.managers;

import thd.game.utilities.GameView;
import thd.game.utilities.TravelPathCalculator;
import thd.gameobjects.base.GameObject;

/**
 * A class that holds all constant values needed for the game in one place.
 */
public final class GameSettings {
    /**
     * The Font Family that should be used everywhere. It resembles the old C64 Font and is monospaced.
     */
    public static final String FONT_FAMILY = "c64pro.ttf";
    /**
     * The Font Size to display text in the Footer of the Game.
     *
     * @see thd.gameobjects.unmovable.Footer
     */
    public static final int FONT_SIZE = 25;
    /**
     * Percentage value of the top Screen Side, that defines where the top side of the Travel Path should go through
     * when entering the Screen.
     */
    public static final double SCREEN_ENTRY_FACTOR_TOP = 0.75;
    /**
     * Percentage value of the right Screen Side, that defines where the lower side of the Travel Path should go through
     * when entering the Screen.
     */
    public static final double SCREEN_ENTRY_FACTOR_RIGHT = 0.5;
    /**
     * Any {@code GameObject} moves in this angle (in degree).
     *
     * @see GameObject
     */
    public static final double MOVEMENT_ANGLE_IN_DEGREE = 26.5;

    /**
     * Any {@code GameObject} moves in this angle (in radians).
     *
     * @see GameObject
     */
    public static final double MOVEMENT_ANGLE_IN_RADIANS = Math.toRadians(MOVEMENT_ANGLE_IN_DEGREE);


    // DEBUG ONLY - SPAWN_LINE_OFFSET, so that Spawn Line is fully visible on GameView
    /**
     * If set to {@code true}, the spawnLine will be fully visible on the Screen.
     * This will overwrite {@link #DEFAULT_SPAWN_LINE_OFFSET}.
     */
    public static final boolean USE_DEBUG_SPAWN_OFFSET = false;
    /**
     * This is the default value. If any {@link GameObject}s are visible during the (de)spawn process, increase this value!
     */
    public static final double DEFAULT_SPAWN_LINE_OFFSET = 250;

    /**
     * This is the height of the Footer that contains displays like LiveDisplay, FuelCellGauge, Level, ...
     */
    public static final int FOOTER_HEIGHT = GameView.HEIGHT / 5;
    /**
     * This is the actual height of the Game, since the Footer takes up the bottom Part of the {@link GameView}.
     */
    public static final int GAME_HEIGHT = GameView.HEIGHT - FOOTER_HEIGHT;
    /**
     * This is the maximum altitude that the Player can fly up to.
     */
    public static final double MAX_PLAYER_ALTITUDE = 250;
    /**
     * This is the default speed for all {@link GameObject}s.
     */
    public static final double SPEED_IN_PIXEL = 4;

    /**
     * An object that calculates many important values needed based on
     * {@link GameView#HEIGHT}, {@link GameView#WIDTH} and {@link #MOVEMENT_ANGLE_IN_RADIANS}.
     *
     * @see TravelPathCalculator
     */
    public static final TravelPathCalculator TRAVEL_PATH_CALCULATOR = new TravelPathCalculator();


    static {
        // check for valid constant values
        if (GameSettings.MOVEMENT_ANGLE_IN_DEGREE < 10 || GameSettings.MOVEMENT_ANGLE_IN_DEGREE > 31) {
            throw new IllegalArgumentException("Movement Angle should be in [10, 31]!");
        }
    }
}
