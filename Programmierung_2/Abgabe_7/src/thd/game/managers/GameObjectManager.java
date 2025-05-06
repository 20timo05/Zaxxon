package thd.game.managers;

import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.EnergyBarrier;
import thd.gameobjects.movable.ZaxxonFighterLaserShot;

import java.util.LinkedList;
import java.util.List;

class GameObjectManager extends CollisionManager {
    private final List<GameObject> gameObjects;
    private final List<GameObject> gameObjectsToBeAdded;
    private final List<GameObject> gameObjectsToBeRemoved;

    private static final int MAXIMUM_NUMBER_OF_GAME_OBJECTS = 500;

    GameObjectManager() {
        gameObjects = new LinkedList<>();
        gameObjectsToBeAdded = new LinkedList<>();
        gameObjectsToBeRemoved = new LinkedList<>();
    }

    void add(GameObject gameObject) {
        gameObjectsToBeAdded.add(gameObject);

        if (gameObject instanceof ZaxxonFighterLaserShot) {
            for (GameObject obj : gameObjects) {
                if (obj instanceof EnergyBarrier) {
                    ((EnergyBarrier) obj).addCollidingGameObjectForPathDecision((ZaxxonFighterLaserShot) gameObject);
                }
            }
        }
    }

    void remove(GameObject gameObject) {
        gameObjectsToBeRemoved.add(gameObject);
    }

    void removeAll() {
        gameObjectsToBeAdded.clear();
        gameObjectsToBeRemoved.addAll(gameObjects);
    }

    void gameLoop() {
        updateLists();
        for (GameObject gameObject : gameObjects) {
            gameObject.updateStatus();
            gameObject.updatePosition();
            gameObject.addToCanvas();
        }

        manageCollisions(true);
    }

    private void updateLists() {
        removeFromGameObjects();
        addToGameObjects();

        if (gameObjects.size() > MAXIMUM_NUMBER_OF_GAME_OBJECTS) {
            throw new TooManyGameObjectsException("Maximum Number of GameObjects (" + MAXIMUM_NUMBER_OF_GAME_OBJECTS + ") exceeded!");
        }
    }

    private void removeFromGameObjects() {
        for (GameObject o : gameObjectsToBeRemoved) {
            gameObjects.remove(o);
            removeFromCollisionManagement(o);
        }
        gameObjectsToBeRemoved.clear();
    }

    private void addToGameObjects() {
        for (GameObject o : gameObjectsToBeAdded) {
            sortIntoGameObjects(o);
            addToCollisionManagement(o);
        }
        gameObjectsToBeAdded.clear();
    }

    private void sortIntoGameObjects(GameObject toAdd) {
        int indexToSortIn = 0;
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getDistanceToBackground() >= toAdd.getDistanceToBackground()) {
                break;
            }
            indexToSortIn++;
        }
        gameObjects.add(indexToSortIn, toAdd);
    }
}
