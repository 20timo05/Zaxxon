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
    public String name;
    public int index;
    public String worldString;
    public int worldOffsetColumns;
    public int worldOffsetLines;
    public final WallBlockGraphicUtils.DynamicWall[] dynamicWalls;

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
                    """,
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """,
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxx                  xxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """,
            """
                    xxxxxxxxxxxxxx            xxxx
                    xxxxxxxxxxxx        xxxxxxxxxx
                    xxxxxxxxxx      xxxxxxxxxxxxxx
                    xxxxxxxx    xxxxxxxxxxxxxxxxxx
                    xxxxxx    xxxxxxxxxxxxxxxxxxxx
                    xxxx  xxxxxxxxxxxxxxxxxxxxxxxx
                    xx  xxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """,
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
                    """,
            """
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx  xxxxxx  xxxxxx  xxxxxx
                    xxxxxx  xxxxxx  xxxxxx  xxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    """,
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
                    """
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
