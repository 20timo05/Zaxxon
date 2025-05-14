package thd.game.level;

import thd.game.utilities.WallBlockGraphicUtils;

import java.util.Arrays;

/**
 * This is a representation of a level.
 *
 * Dictionary:
 * 1 Row = 2x FULL_BLOCK_FRONT (on the left side)
 * 1 Column = 2x FULL_BLOCK_FRONT (in a WallRow)
 *
 * - w: Wall
 *      - Number: Index in WALL_DESCRIPTIONS
 * - s: EnemyShooter
 *      - Number 0
 * - e: EnergyBarrier
 *      - Number: altitudeLevel
 * - f: FuelTank
 *      - Number 0
 * - g: GunEmplacement
 *      - Number 0: straight
 *      - Number 1: across
 * - r: RadarTower
 *      - Number 0
 * - v: VerticalRocket
 *      - Number 0
 */
public class Level {
    /** The Name of the Level. */
    public String name;
    /** The number of the Level. */
    public int number;
    /** The world String representation of the Level. */
    public String world;
    /** The horizontal offset for the visible area of the world String. */
    int worldOffsetColumns;
    /** The vertical offset for the visible area of the world String. */
    int worldOffsetLines;
    /** The Timestamp when the Level will end. If the Player is still alive, he has completed the Level. */
    public int levelDurationTimestamp;

    /** Current Difficulty Level that is either EASY or STANDARD. */
    public static Difficulty difficulty = Difficulty.STANDARD;

    // W2, W4, W5 have been adjusted to ensure an odd number of solid rows below gaps.
    private static final String[] WALL_DESCRIPTIONS = new String[]{
            """
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W0: High Hole (Alt 3, 4) - OK
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W1: Low Solid (Alt 0, 1) - OK (No gaps)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W2: Mid Passage (Alt 1, 2) - CORRECTED (6 rows total, 1-row base)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxx              xxxxxxxxxxxxxxxxx
                    xxxx              xxxxxxxxxxxxxxxxx
                    xxxxxxxxxx            xxxxxxxxxxxxx
                    xxxxxxxxxx            xxxxxxxxxxxxx
                    xxxxxxxxxxxxxxx            xxxxxxxx
                    xxxxxxxxxxxxxxxxxxx        xxxxxxxx
                    xxxxxxxxxxxxxxxxxxx        xxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W3: Wider Diagonal Slit (Low-Right to High-Left) - User's version, OK
            """
                    xxxx    xxxxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxxxx    xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W4: Dual Vertical Slits (Alt 0-4 access) - CORRECTED (8 rows total, 1-row base)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxx     xxxxxxxxxx    xxxxxx
                    xxxxx     xxxxxxxxxx    xxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W5: Mid Gaps (Holes at Alt 1) - CORRECTED (4 rows total, 1-row base)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    """, // W6: Low Hole (Alt 0, 1) - OK (Gap at bottom)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxx    xxxx
                    xxxxxxxxxxxxxxxxxxxxxx      xxxxxx
                    xxxxxxxxxxxxxxxxxx        xxxxxxxx
                    xxxxxxxxxxxxxx          xxxxxxxxxx
                    xxxxxxxxxxxx            xxxxxxxxxxxx
                    xxxxxxxxxx              xxxxxxxxxxxxxx
                    xxxxxxxx                xxxxxxxxxxxxxxxx
                    xxxxxx                  xxxxxxxxxxxxxxxxxx
                    xxxx                    xxxxxxxxxxxxxxxxxxxx
                    """, // W7: Stairs Up (Left-to-Right, Alt 0 to 4) - OK (Gaps can go to bottom)
            """
                    xxxxxx      xxxxxxxx      xxxxxx
                    xxxxxx      xxxxxxxx      xxxxxx
                    xxxxxx      xxxxxxxx      xxxxxx
                    xxxxxx      xxxxxxxx      xxxxxx
                    xxxxxx      xxxxxxxx      xxxxxx
                    xxxxxx      xxxxxxxx      xxxxxx
                    xxxxxx      xxxxxxxx      xxxxxx
                    xxxxxx      xxxxxxxx      xxxxxx
                    xxxxxx      xxxxxxxx      xxxxxx
                    """, // W8: Central Pillar (Openings L/R, Alt 0-4) - OK (Gaps can go to bottom)
            """
                    xxxxxx        xxxxxxxx        xx
                    xxxxxx        xxxxxxxx        xx
                          xxxxxxxx        xxxxxxxx
                          xxxxxxxx        xxxxxxxx
                    xxxxxx        xxxxxxxx        xx
                    xxxxxx        xxxxxxxx        xx
                        xxxxxxxxxx        xxxxxxxx
                        xxxxxxxxxx        xxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W9: Offset Vertical Slits / Weave - OK
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx                  xxxxxxxxxxxx
                    xxxxxx                  xxxxxxxxxxxx
                    xxxxxxxx      xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxx      xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxx  xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxx  xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W10: Funnel Down - OK
            """
                    xxxx    xxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxx    xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxx    xxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxx    xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxx    xxxxxxxxxxxx    xxxx
                    xxxx    xxxxxxxxxxxx    xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """ // W11: Layered Side Holes - OK (Gaps can go to bottom)
    };

    /** The pregenerated BlockImages for the Walls used in the Levels. */
    public static final WallBlockGraphicUtils.DynamicWall[] DYNAMIC_WALLS = Arrays.stream(WALL_DESCRIPTIONS).map(WallBlockGraphicUtils::generateDynamicWall).toArray(WallBlockGraphicUtils.DynamicWall[]::new);

}
