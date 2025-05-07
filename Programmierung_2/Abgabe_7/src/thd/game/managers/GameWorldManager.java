package thd.game.managers;

import static thd.game.managers.GameSettings.SPEED_IN_PIXEL;
import static thd.game.managers.GameSettings.TRAVEL_PATH_CALCULATOR;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import thd.game.utilities.GameView;
import thd.game.utilities.WallBlockDimensionCalculator;
import thd.game.utilities.WallBlockGraphicUtils;
import thd.game.utilities.WallBlockImages;
import thd.gameobjects.base.ActivatableGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.base.Vector2d;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.DebuggingLines;
import thd.gameobjects.unmovable.Footer;
import thd.gameobjects.unmovable.FuelCellGauge;
import thd.gameobjects.unmovable.HeightStatusBar;

class GameWorldManager extends GamePlayManager {
    private final DebuggingLines debuggingLines;
    private final String world;

    private final int worldOffsetColumns;
    private final int worldOffsetLines;

    private final List<GameObject> activatableGameObjects;

    private static final String[] WALL_DESCRIPTIONS = new String[] {
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
    
    private String backgroundWallBlockImage;
    private Background[] backgrounds;

    GameWorldManager(GameView gameView) {
        super(gameView);

        /*
         * Dictionary:
         * 1 Row = 2x FULL_BLOCK_FRONT (on the left side)
         * 1 Column = 2x FULL_BLOCK_FRONT (in a WallRow)
         * 
         * - w: Wall
         * - Number: Index in WALL_DESCRIPTIONS
         * - s: EnemyShooter
         * - Number 0
         * - e: EnergyBarrier
         * - Number: altitudeLevel
         * - f: FuelTank
         * - Number 0
         * - g: GunEmplacement
         * - Number 0: straight
         * - Number 1: across
         * - r: RadarTower
         * - Number 0
         * - v: VerticalRocket
         * - Number 0
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
        
        

        // all gameobjects should be visible in the beginning
        worldOffsetColumns = world.split("\\R").length;
        worldOffsetLines = 0;

        activatableGameObjects = new LinkedList<>();

        debuggingLines = new DebuggingLines(gameView, this);
        zaxxonFighter = new ZaxxonFighter(gameView, this);
        heightStatusBar = new HeightStatusBar(gameView, this);
        footer = new Footer(gameView, this);
        fuelCellGauge = new FuelCellGauge(gameView, this);
        backgrounds = new Background[2];

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

        // generate background wall
        int numberOfBricks = (int) (TRAVEL_PATH_CALCULATOR.getDistanceToDespawnLine() / WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_X);
        int height = 9;
        StringBuilder bgWallDesc = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < numberOfBricks; x++) {
                bgWallDesc.append('x');
            }
            bgWallDesc.append("\n");
        }

        String bgWallMirrored = utils.generateWallBlockImage(bgWallDesc.toString());
        backgroundWallBlockImage = utils.mirrorBlockImage(bgWallMirrored);
    }

    private void spawnGameObjects() {
        spawnGameObject(debuggingLines);
        spawnGameObject(zaxxonFighter);
        spawnGameObject(heightStatusBar);
        spawnGameObject(footer);
        spawnGameObject(fuelCellGauge);
    }

    private void addActivatableGameObject(GameObject gameObject) {
        System.out.println(gameObject.spawnDelayInMilis + " - " + gameObject.getClass());
        activatableGameObjects.add(gameObject);
        addToShiftableGameObjectsIfShiftable(gameObject);
    }

    private void spawnGameObjectsFromWorldString() {
        String[] lines = world.split("\\R");

        for (int row = lines.length - 1; row > 0; row--) {
            double distanceFromSpawnLine = (lines.length - 1 - row) * 2 * WallBlockDimensionCalculator.FULL_BLOCK_INCREASE_OFFSET_X;
            int spawnDelayInMilis = (int) (distanceFromSpawnLine / SPEED_IN_PIXEL * 1000 / 60);

            for (int column = 0; column < lines[row].length() - 1; column++) {
                double spawnLineInter = (double) column / lines[row].length();

                char c = lines[row].charAt(column);
                int status = Character.getNumericValue(lines[row].charAt(column + 1));

                if (c == 'w') {
                    addActivatableGameObject(
                            new Wall(gameView, this, dynamicWalls[status], spawnDelayInMilis, spawnLineInter));
                } else if (c == 's') {
                    addActivatableGameObject(new EnemyShooter(gameView, this, spawnDelayInMilis, spawnLineInter));
                } else if (c == 'e') {
                    addActivatableGameObject(new EnergyBarrier(gameView, this, spawnDelayInMilis, status));
                } else if (c == 'f') {
                    addActivatableGameObject(new FuelTank(gameView, this, spawnDelayInMilis, spawnLineInter));
                } else if (c == 'g') {
                    addActivatableGameObject(
                            new GunEmplacement(gameView, this, spawnDelayInMilis, spawnLineInter, status == 0));
                } else if (c == 'r') {
                    addActivatableGameObject(new RadarTower(gameView, this, spawnDelayInMilis, spawnLineInter));
                } else if (c == 'v') {
                    addActivatableGameObject(new VerticalRocketHole(gameView, this, spawnDelayInMilis, spawnLineInter));
                }
            }
        }
        
        backgrounds[0] = new Background(gameView, this, backgroundWallBlockImage);
        backgrounds[1] = new Background(gameView, this, backgroundWallBlockImage);
        spawnGameObject(backgrounds[0]);
        addActivatableGameObject(backgrounds[1]);
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        activateGameObjects();

        if (backgrounds[0].hasDespawned()) {
            backgrounds[0] = backgrounds[1];
            backgrounds[1] = new Background(gameView, this, backgroundWallBlockImage);
            addActivatableGameObject(backgrounds[1]);
        }
    }

    private void activateGameObjects() {
        ListIterator<GameObject> iterator = activatableGameObjects.listIterator();

        while (iterator.hasNext()) {
            GameObject currentObject = iterator.next(); 

            if (currentObject instanceof EnemyShooter enemyShooter && enemyShooter.tryToActivate(null)) {
                spawnGameObject(enemyShooter);
                iterator.remove();
            } else if (currentObject instanceof EnergyBarrier energyBarrier && energyBarrier.tryToActivate(null)) {
                spawnGameObject(energyBarrier);
                iterator.remove();
            } else if (currentObject instanceof FuelTank fuelTank && fuelTank.tryToActivate(null)) {
                spawnGameObject(fuelTank);
                iterator.remove();
            } else if (currentObject instanceof GunEmplacement gunEmplacement && gunEmplacement.tryToActivate(null)) {
                spawnGameObject(gunEmplacement);
                iterator.remove();
            } else if (currentObject instanceof RadarTower radarTower && radarTower.tryToActivate(null)) {
                spawnGameObject(radarTower);
                iterator.remove();
            } else if (currentObject instanceof VerticalRocketHole verticalRocketHole
                    && verticalRocketHole.tryToActivate(null)) {
                spawnGameObject(verticalRocketHole);
                iterator.remove();
            } else if (currentObject instanceof WallRow wallRow && wallRow.tryToActivate(null)) {
                spawnGameObject(wallRow);
                iterator.remove();
            } else if (currentObject instanceof Background background && background.tryToActivate(backgrounds[0])) {
                spawnGameObject(currentObject);
                iterator.remove();
            }
        }
    }
}
