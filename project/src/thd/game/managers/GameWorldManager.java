package thd.game.managers;

import thd.game.utilities.GameView;
import thd.game.utilities.TravelPathCalculator;
import thd.game.utilities.WallBlockGraphicUtils;
import thd.game.utilities.WallBuildingService;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.Footer;
import thd.gameobjects.unmovable.FuelCellGauge;
import thd.gameobjects.unmovable.HeightStatusBar;
import thd.gameobjects.unmovable.Overlay;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static thd.game.managers.GameSettings.SPEED_IN_PIXEL;

class GameWorldManager extends GamePlayManager {
    private final List<GameObject> activatableGameObjects;

    private String backgroundWallBlockImage;
    private WallBackground[] wallBackgrounds;

    protected GameWorldManager(GameView gameView) {
        super(gameView);

        activatableGameObjects = new LinkedList<>();
        overlay = new Overlay(gameView, this);
    }

    private void spawnGameObjects() {
        zaxxonFighter = new ZaxxonFighter(gameView, this);
        heightStatusBar = new HeightStatusBar(gameView, this);
        footer = new Footer(gameView, this);
        fuelCellGauge = new FuelCellGauge(gameView, this);
        wallBackgrounds = new WallBackground[2];

        generateBackgroundWall();
        spawnGameObject(zaxxonFighter);
        spawnGameObject(heightStatusBar);
        spawnGameObject(footer);
        spawnGameObject(fuelCellGauge);
        spawnGameObject(overlay);
    }

    private void addActivatableGameObject(GameObject gameObject) {
        activatableGameObjects.add(gameObject);
        addToShiftableGameObjectsIfShiftable(gameObject);
    }


    private void spawnGameObjectsFromWorldString() {
        String[] lines = level.world.split("\\R");

        for (int row = lines.length - 1; row >= 0; row--) {
            double distanceFromSpawnLine = (lines.length - 1 - row) * 2 * WallBuildingService.FULL_BLOCK_INCREASE_OFFSET_X;
            int spawnDelayInMilis = distanceToDuration(distanceFromSpawnLine);

            for (int column = 0; column < lines[row].length() - 1; column++) {
                double spawnLineInter = (double) column / lines[row].length();

                // double x = (level.worldOffsetColumns - column) * 100; // wichtel
                // double y = (level.worldOffsetLines - row) * 100; // wichtel

                char character = lines[row].charAt(column);
                int status = Character.getNumericValue(lines[row].charAt(column + 1));

                if (character == 'w') {
                    Wall wall = new Wall(gameView, this, level.DYNAMIC_WALLS[status], spawnDelayInMilis, spawnLineInter);
                    for (WallRow wallRow : wall.wallRows) {
                        addActivatableGameObject(wallRow);
                    }
                } else if (character == 's') {
                    addActivatableGameObject(new EnemyShooter(gameView, this, spawnDelayInMilis, spawnLineInter));
                } else if (character == 'e') {
                    addActivatableGameObject(new EnergyBarrier(gameView, this, spawnDelayInMilis, status));
                } else if (character == 'f') {
                    addActivatableGameObject(new FuelTank(gameView, this, spawnDelayInMilis, spawnLineInter));
                } else if (character == 'g') {
                    addActivatableGameObject(
                            new GunEmplacement(gameView, this, spawnDelayInMilis, spawnLineInter, status == 0));
                } else if (character == 'r') {
                    addActivatableGameObject(new RadarTower(gameView, this, spawnDelayInMilis, spawnLineInter));
                } else if (character == 'v') {
                    addActivatableGameObject(new VerticalRocketHole(gameView, this, spawnDelayInMilis, spawnLineInter));
                }
            }

            if (row == 0) {
                // game should end, after last obstacle has passed
                double distanceToPlayerMovementLine = TravelPathCalculator.copySpawnLine()[0].distance(TravelPathCalculator.copyPlayerMovementLine()[0]) + 100;
                level.levelDurationTimestamp = distanceToDuration(distanceFromSpawnLine + distanceToPlayerMovementLine);
            }

        }

        wallBackgrounds[0] = new WallBackground(gameView, this, backgroundWallBlockImage, true);
        wallBackgrounds[1] = new WallBackground(gameView, this, backgroundWallBlockImage, false);
        spawnGameObject(wallBackgrounds[0]);
        addActivatableGameObject(wallBackgrounds[1]);
    }

    private int distanceToDuration(double distance) {
        return gameView.gameTimeInMilliseconds() + (int) (distance / SPEED_IN_PIXEL * 1000 / 60 * 1.3); // 1.3 is the streching out factor
    }

    protected void initializeLevel() {
        activatableGameObjects.clear();
        destroyAllGameObjects();
        spawnGameObjects();
        spawnGameObjectsFromWorldString();
        clearListsForPathDecisionsInGameObjects();
    }

    private void clearListsForPathDecisionsInGameObjects() {
        // @TODO
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        activateGameObjects();

        if (wallBackgrounds[0].hasDespawned) {
            wallBackgrounds[0] = wallBackgrounds[1];
            wallBackgrounds[1] = new WallBackground(gameView, this, backgroundWallBlockImage, false);
            addActivatableGameObject(wallBackgrounds[1]);
        }
    }

    private void activateGameObjects() {
        ListIterator<GameObject> iterator = activatableGameObjects.listIterator();

        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();

            if (gameObject instanceof EnemyShooter enemyShooter && enemyShooter.tryToActivate(null)) {
                spawnGameObject(enemyShooter);
                iterator.remove();
            } else if (gameObject instanceof EnergyBarrier energyBarrier && energyBarrier.tryToActivate(null)) {
                spawnGameObject(energyBarrier);
                iterator.remove();
            } else if (gameObject instanceof FuelTank fuelTank && fuelTank.tryToActivate(null)) {
                spawnGameObject(fuelTank);
                iterator.remove();
            } else if (gameObject instanceof GunEmplacement gunEmplacement && gunEmplacement.tryToActivate(null)) {
                spawnGameObject(gunEmplacement);
                iterator.remove();
            } else if (gameObject instanceof RadarTower radarTower && radarTower.tryToActivate(null)) {
                spawnGameObject(radarTower);
                iterator.remove();
            } else if (gameObject instanceof VerticalRocketHole verticalRocketHole
                    && verticalRocketHole.tryToActivate(null)) {
                spawnGameObject(verticalRocketHole);
                iterator.remove();
            } else if (gameObject instanceof WallRow wallRow && wallRow.tryToActivate(null)) {
                spawnGameObject(wallRow);
                iterator.remove();
            } else if (gameObject instanceof WallBackground wallBackground) {
                if (wallBackground.tryToActivate(wallBackgrounds[0])) {
                    spawnGameObject(gameObject);
                    iterator.remove();
                }
            }
        }
    }

    private void generateBackgroundWall() {
        double horizontalDistanceSpawnLineDespawnLine = Math.abs(TravelPathCalculator.copySpawnLine()[0].getX() - TravelPathCalculator.copyDespawnLine()[0].getX());
        int numberOfBricks = (int) (horizontalDistanceSpawnLineDespawnLine / WallBuildingService.FULL_BLOCK_INCREASE_OFFSET_X);
        int height = 9;
        StringBuilder bgWallDesc = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < numberOfBricks; x++) {
                bgWallDesc.append('x');
            }
            bgWallDesc.append("\n");
        }

        String bgWallMirrored = WallBlockGraphicUtils.generateWallBlockImage(bgWallDesc.toString());
        backgroundWallBlockImage = WallBlockGraphicUtils.mirrorBlockImage(bgWallMirrored);
    }
}
