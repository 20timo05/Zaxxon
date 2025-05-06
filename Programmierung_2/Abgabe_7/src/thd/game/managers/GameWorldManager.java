package thd.game.managers;

import thd.game.utilities.GameView;
import thd.game.utilities.WallBlockDimensionCalculator;
import thd.game.utilities.WallBlockGraphicUtils;
import thd.game.utilities.WallBlockImages;
import thd.gameobjects.base.Vector2d;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.DebuggingLines;
import thd.gameobjects.unmovable.Footer;
import thd.gameobjects.unmovable.FuelCellGauge;
import thd.gameobjects.unmovable.HeightStatusBar;

class GameWorldManager extends GamePlayManager {
    private final DebuggingLines debuggingLines;
    private final String world;

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
xxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxx
""",
"""
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
"""
    };

    private final WallBlockGraphicUtils.DynamicWall[] dynamicWalls;

    GameWorldManager(GameView gameView) {
        super(gameView);

        /* Dictionary:
        1 Row = 2x FULL_BLOCK_FRONT (on the left side)
        1 Column = 2x FULL_BLOCK_FRONT (in a WallRow)
        
        - w: Wall
            - Number: Index in WALL_DESCRIPTIONS
        - s: EnemyShooter
            - Number 0
        - e: EnergyBarrier
            - Number: altitudeLevel
        - f: FuelTank
            - Number 0
        - g: GunEmplacement
            - Number 0: straight
            - Number 1: across
        - r: RadarTower
            - Number 0
        - v: VerticalRocket
            - Number 0
         */
        world = """
   f0   \s
        \s
        \s
        \s
        \s
  f0  g1\s
        \s
     f0 \s
        \s
      g1\s
        \s
        \s
        \s
        \s
        \s
  w1    \s
        \s
        \s
        \s
        \s
  r0    \s
        \s
        \s
        \s
 r0   r0\s
        \s
        \s
        \s
        \s
        \s
        \s
        \s
 g0     \s
   v0   \s
        \s
      g1\s
        \s
        \s
      r0\s
        \s
        \s
        \s
        \s
        \s
        \s
        \s
        \s
        \s
        \s
w0      \s
""";

        debuggingLines = new DebuggingLines(gameView, this);
        zaxxonFighter = new ZaxxonFighter(gameView, this);
        heightStatusBar = new HeightStatusBar(gameView, this);
        footer = new Footer(gameView, this);
        fuelCellGauge = new FuelCellGauge(gameView, this);

        dynamicWalls = new WallBlockGraphicUtils.DynamicWall[WALL_DESCRIPTIONS.length];
        generateWalls();

        spawnGameObjects();
        spawnGameObjectsFromWorldString();
    }

    private void generateWalls() {
        WallBlockGraphicUtils utils = new WallBlockGraphicUtils();
        for (int i = 0; i < WALL_DESCRIPTIONS.length; i++) {
            dynamicWalls[i] = utils.generateDynamicWall(WALL_DESCRIPTIONS[i]);
        }
    }

    private void spawnGameObjects() {
        spawnGameObject(debuggingLines);
        spawnGameObject(zaxxonFighter);
        spawnGameObject(heightStatusBar);
        spawnGameObject(footer);
        spawnGameObject(fuelCellGauge);
    }

    private void spawnGameObjectsFromWorldString(){
        String[] lines = world.split("\\R");

        for (int row = 0; row < lines.length; row++) {
            double distanceFromSpawnLine = row * 2 * WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_X;

            for (int column = 0; column < lines[row].length()-1; column++) {
                double spawnLineInter = (double) column / lines[row].length();

                char c = lines[row].charAt(column);
                int status = Character.getNumericValue(lines[row].charAt(column+1));

                if (c == 'w') {
                    spawnGameObject(new Wall(gameView, this, dynamicWalls[status], distanceFromSpawnLine, spawnLineInter));
                } else if (c == 's') {
                    spawnGameObject(new EnemyShooter(gameView, this, distanceFromSpawnLine, spawnLineInter));
                } else if (c == 'e') {
                    spawnGameObject(new EnergyBarrier(gameView, this, distanceFromSpawnLine, status));
                } else if (c == 'f') {
                    spawnGameObject(new FuelTank(gameView, this, distanceFromSpawnLine, spawnLineInter));
                } else if (c == 'g') {
                    spawnGameObject(new GunEmplacement(gameView, this, distanceFromSpawnLine, spawnLineInter, status==0));
                } else if (c == 'r') {
                    spawnGameObject(new RadarTower(gameView, this, distanceFromSpawnLine, spawnLineInter));
                } else if (c == 'v') {
                    spawnGameObject(new VerticalRocketHole(gameView, this, distanceFromSpawnLine, spawnLineInter));
                }

            }
        }
    }
}
