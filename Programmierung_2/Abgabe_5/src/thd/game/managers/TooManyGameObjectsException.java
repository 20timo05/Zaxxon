package thd.game.managers;

/**
 * An unchecked Exception that is thrown, when too many GameObjects are rendered at the same time.
 * This prevents the Game from becoming too slowed down.
 */
public class TooManyGameObjectsException extends RuntimeException {

    /**
     * Initializes a {@code TooManyGameObjects} Exception.
     *
     * @param message the message
     */
    public TooManyGameObjectsException(String message) {
        super(message);
    }
}
