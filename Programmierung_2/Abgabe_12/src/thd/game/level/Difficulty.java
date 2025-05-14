package thd.game.level;

/**
 * Represents the difficulty levels available in the game.
 */
public enum Difficulty {
    /**
     * Easy difficulty level, suitable for beginners.
     */
    EASY("Einfach"),

    /**
     * Standard difficulty level, offering a balanced challenge.
     */
    STANDARD("Standard");

    /**
     * A name for the Difficulty in German, that is displayed to the User via the Start- & Endscreen.
     *
     * @see thd.screens.Screens
     */
    public final String name;

    /**
     * A constructor that is setting the {@link #name}.
     *
     * @param name the german translation
     */
    Difficulty(String name) {
        this.name = name;
    }

    /**
     * A method that translates the german label to an Entry of Difficulty.
     *
     * @param name the german label
     * @return a {@link Difficulty}
     */
    public static Difficulty fromName(String name) {
        if (name.equals("Einfach")) {
            return EASY;
        } else if (name.equals("Standard")) {
            return STANDARD;
        } else {
            throw new IllegalArgumentException(name + " is not a valid Difficulty!");
        }
    }
}
