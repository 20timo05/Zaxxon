package thd.game.level;

/**
 * This Exception is thrown if the Game wants to switch to the next Level, even though there is none.
 *
 * @see thd.game.level
 */
public class NoMoreLevelsAvailableException extends RuntimeException {

    /**
     * Creates a {@code NoMoreLevelsAvailableException} with the specified message for the console.
     *
     * @param message the message for the console
     */
    public NoMoreLevelsAvailableException(String message) {
        super(message);
    }
}
