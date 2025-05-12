package thd.game.managers;

import thd.game.utilities.SortedGameObjectsList;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.EnergyBarrier;
import thd.gameobjects.movable.ZaxxonFighter;
import thd.gameobjects.movable.ZaxxonFighterLaserShot;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class GameObjectManager extends CollisionManager {
    private final SortedGameObjectsList gameObjects;
    private final List<GameObject> gameObjectsToBeAdded;
    private final List<GameObject> gameObjectsToBeRemoved;

    private static final int MAXIMUM_NUMBER_OF_GAME_OBJECTS = 500;

    GameObjectManager() {
        gameObjects = new SortedGameObjectsList();
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

        gameObjects.resortForDynamicGameObjects();

        for (GameObject gameObject : gameObjects) {
            gameObject.updateStatus();
            gameObject.updatePosition();
            gameObject.addToCanvas();
        }

        manageCollisions(false);
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
        for (GameObject toAdd : gameObjectsToBeAdded) {
            gameObjects.add(toAdd);
            addToCollisionManagement(toAdd);
        }
        gameObjectsToBeAdded.clear();
    }
}
