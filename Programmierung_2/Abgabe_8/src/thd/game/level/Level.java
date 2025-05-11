package thd.game.level;

import thd.game.utilities.WallBlockGraphicUtils;

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
    public int worldOffsetColumns;
    /** The vertical offset for the visible area of the world String. */
    public int worldOffsetLines;
    /** The pregenerated BlockImages for the Walls used in the Levels. */
    public final WallBlockGraphicUtils.DynamicWall[] dynamicWalls;
    /** The Timestamp when the Level will end. If the Player is still alive, he has completed the Level. */
    public int levelDurationTimestamp;

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
                    """, // W0: High Hole (Alt 3, 4)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W1: Low Solid (Alt 0, 1)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W2: Mid Passage (Alt 1, 2)
            """
                            xxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxx        xxxxxxxxxxxxxxxx
                    xxxxxxxxxx        xxxxxxxxxxxxxxxx
                    xxxxxxxxxx              xxxxxxxxxx
                    xxxxxxxxxx              xxxxxxxxxx
                    xxxxxxxxxxxxxxx                   xx
                    xxxxxxxxxxxxxxxxx                 xx
                    xxxxxxxxxxxxxxxxxxx       xxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W3: Wider Diagonal Slit (Low-Right to High-Left)
            """
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W4: Dual Vertical Slits (Alt 0-4 access)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx  xxxxxx  xxxxxx  xxxxxx
                    xxxxxx  xxxxxx  xxxxxx  xxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W5: Mid Gaps (Holes at Alt 1)
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
                    """, // W6: Low Hole (Alt 0, 1)
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
                    """, // W7: Stairs Up (Left-to-Right, Alt 0 to 4)
            """
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    xxxxxx    xxxxxxxxxx    xxxxxx
                    """, // W8: Central Pillar (Openings L/R, Alt 0-4)
            """
                    xxxxxxxx      xxxxxxxx      xxxx
                    xxxxxxxx      xxxxxxxx      xxxx
                        xxxxxxxxxxxx      xxxxxxxx
                        xxxxxxxxxxxx      xxxxxxxx
                    xxxxxxxx      xxxxxxxx      xxxx
                    xxxxxxxx      xxxxxxxx      xxxx
                        xxxxxxxxxxxx      xxxxxxxx
                        xxxxxxxxxxxx      xxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W9: Offset Vertical Slits / Weave (Holes Alt 4 Mid, Alt 3 Left, Alt 2 Mid, Alt 1 Left)
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx                  xxxxxxxxxxxx
                    xxxxxx                  xxxxxxxxxxxx
                    xxxxxxxx      xxxxxxxxxxxxxxxxxxxx
                    xxxxxxxx      xxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxx  xxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxx  xxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """, // W10: Funnel Down (Hole Alt 3, narrows to Alt 2, then Alt 1)
            """
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxx  xxxxxxxxxxxxxxxx  xxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """ // W11: Layered Side Holes (Holes L/R at Alt 4, 2, 0)
    };

    /**
     * Pregenerates the Wall BlocKImage Strings.
     */
    public Level() {
        dynamicWalls = new WallBlockGraphicUtils.DynamicWall[WALL_DESCRIPTIONS.length];
        generateWalls();
    }

    private void generateWalls() {
        WallBlockGraphicUtils utils = new WallBlockGraphicUtils();
        for (int i = 0; i < WALL_DESCRIPTIONS.length; i++) {
            dynamicWalls[i] = utils.generateDynamicWall(WALL_DESCRIPTIONS[i]);
        }
    }

}
