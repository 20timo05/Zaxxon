package thd.gameobjects.base;

/**
 * This refers to GameObjects that are spawned at the beginning of the Game but stay inactive until a certain event.
 *
 * @param <T> the type of the info parameter that this object receives in {@link #tryToActivate}
 */
public interface ActivatableGameObject<T> {

    /**
     * A method that determines whether this GameObject should be active.
     *
     * @param info information necessary to calculate whether this GameObject should be activated
     * @return a boolean whether this GameObject should be active
     */
    boolean tryToActivate(T info);
}
