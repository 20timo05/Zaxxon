package thd.game.utilities;

import thd.game.level.Difficulty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Provides methods for accessing and managing game settings stored in a file.
 */
public class FileAccess {
    /**
     * The path to the file where game settings are stored.
     * It is located in the user's home directory and named "timo_rolf_game.txt".
     */
    public static final Path WICHTEL_GAME_FILE = Paths.get(System.getProperty("user.home")).resolve("timo_rolf_game.txt");

    /**
     * Writes the specified difficulty to the wichtelgame.txt file.
     * If an IOException occurs, prints the stack trace and terminates the
     * program with error status 1.
     *
     * @param difficulty The Difficulty enum to write.
     */
    public static void writeDifficultyToDisc(Difficulty difficulty) {
        try {
            Files.writeString(
                    WICHTEL_GAME_FILE,
                    difficulty.name(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Reads the difficulty from the wichtelgame.txt file.
     * Returns Difficulty.STANDARD if an IOException occurs or if the file
     * content does not match any valid Difficulty enum constant.
     *
     * @return The Difficulty read from the file, or Difficulty.STANDARD if an
     *         error occurs or content is invalid.
     */
    public static Difficulty readDifficultyFromDisc() {
        try {
            String difficultyStringFromFile = Files.readString(
                    WICHTEL_GAME_FILE,
                    StandardCharsets.UTF_8
            );
            String trimmedDifficultyString = difficultyStringFromFile.trim();

            // Check if the trimmed string matches any of the Difficulty enum constants
            for (Difficulty difficultyValue : Difficulty.values()) {
                if (difficultyValue.name().equals(trimmedDifficultyString)) {
                    return difficultyValue; // Content is valid, return parsed difficulty
                }
            }
            // If no match was found, the content is invalid.
            // Return STANDARD as per requirements.
            return Difficulty.STANDARD;
        } catch (IOException e) {
            // If an IOException occurs (e.g., file not found, read error),
            // return STANDARD as per requirements.
            return Difficulty.STANDARD;
        }
    }
}
