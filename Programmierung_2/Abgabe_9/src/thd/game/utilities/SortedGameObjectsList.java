package thd.game.utilities;

import thd.gameobjects.base.GameObject;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A wrapper around a LinkedList, that keeps the GameObjects sorted by their distanceToBackground.
 */
public class SortedGameObjectsList extends LinkedList<GameObject> {
    @Override
    public boolean add(GameObject toAdd) {
        // Edge Cases, toAdd is not in the middle of the List
        if (isEmpty() || toAdd.getDistanceToBackground() < getFirst().getDistanceToBackground()) {
            addFirst(toAdd);
            return true;
        }

        if (toAdd.getDistanceToBackground() >= getLast().getDistanceToBackground()) {
            addLast(toAdd);
            return true;
        }

        ListIterator<GameObject> it = listIterator();

        while (it.hasNext()) {
            GameObject gameObject = it.next();

            if (gameObject.getDistanceToBackground() >= toAdd.getDistanceToBackground()) {
                it.previous();
                it.add(toAdd);
                return true;
            }
        }

        return false; // should not happen
    }

    /**
     * Some GameObjects may change their DistanceToBackground during the Game, e.g. the {@link thd.gameobjects.movable.ZaxxonFighter}.
     */
    public void resortForDynamicGameObjects() {
        ListIterator<GameObject> iterator = listIterator();
        LinkedList<GameObject> dynamicObjectsToReinsert = new LinkedList<>();

        // remove dynamic GameObjects
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();

            if (gameObject instanceof DynamicZIndexGameObject) {
                iterator.remove();
                dynamicObjectsToReinsert.add(gameObject);
            }
        }

        // insert dynamic GameObjects back into sorted GameObjectsList
        for (GameObject gameObject : dynamicObjectsToReinsert) {
            add(gameObject);
        }
    }
}
