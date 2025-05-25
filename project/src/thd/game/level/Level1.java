package thd.game.level;

/**
 * Level 1: An introductory stage focusing on basic flight, shooting,
 * and navigating simpler wall types (W0, W1, W2, W3, W5, W6).
 * Features open spaces for combat and gentle introductions to
 * Wall + EnergyBarrier combinations. Designed for learning core mechanics.
 */
public class Level1 extends Level {
    private static final String EMPTY_SPACER_CHUNK =
            """
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
                    """;
    private static final String WORLD_STRING_STANDARD_PART1 =
            // END OF LEVEL (Original Part 1)
            """
                            \s
                            \s
                            \s
                            \s
                    """
                    // Section: Final Targets & Simple Wall
                    + """
                    s0  f0  \s
                            \s
                                \s
                        g0  \s
                            \s
                            \s
                      w1    \s
                            \s
                            \s
                    f0      \s
                            \s
                            \s
                    """
                    // Section: Mid Gaps (W5) - Simplified
                    + """
                    s0      \s
                            \s
                            \s
                    g1  f0  \s
                            \s
                            \s
                        e2    \s
                            \s
                      e0    \s
                            \s
                        w5  \s
                            \s
                            \s
                    s0      \s
                            \s
                            \s
                    """
                    // Section: Enemy Wave 2 (More open space)
                    + """
                    f0  r0  \s
                            \s
                            \s
                        g1  \s
                            \s
                            \s
                    s0  s0  \s
                            \s
                            \s
                          f0\s
                            \s
                            \s
                    v0      \s
                            \s
                            \s
                    """
                    // Section: Diagonal Traversal (W3 - Wider Slit) - Kept as a mild challenge
                    + """
                    s0  f0  \s
                            \s
                            \s
                    g0      \s
                            \s
                          e4\s
                            \s
                      e0    \s
                            \s
                      w3    \s
                            \s
                            \s
                    f0      \s
                            \s
                            \s
                    """
                    // Section: Enemy Wave 1 (Focus on shooting)
                    + """
                        v0  \s
                            \s
                            \s
                    s0  g0  \s
                            \s
                            \s
                    f0  f0  \s
                            \s
                            \s
                          s0\s
                            \s
                            \s
                    r0      \s
                            \s
                            \s
                    """
                    // Section: Mid-Passage Wall (W2) & Radar
                    + """
                    s0  f0  \s
                            \s
                            \s
                        g0  \s
                            \s
                      e0    \s
                            \s
                        w2  \s
                            \s
                            \s
                    r0      \s
                            \s
                            \s
                          f0\s
                            \s
                            \s
                    """
                    // Section: Low Wall Challenge (W6)
                    + """
                    s0  f0  \s
                            \s
                            \s
                        v0  \s
                            \s
                      e4    \s
                            \s
                      w6    \s
                            \s
                            \s
                    g1      \s
                            \s
                            \s
                    """
                    // Section: High Wall Challenge (W0)
                    + """
                    f0  s0  \s
                            \s
                            \s
                        g0  \s
                            \s
                          e0\s
                            \s
                      w0    \s
                            \s
                            \s
                    s0      \s
                            \s
                            \s
                    """
                    // Section: Intro to Targets & Low Wall (W1)
                    + """
                        f0  \s
                            \s
                            \s
                    s0  g0  \s
                            \s
                            \s
                      w1    \s
                            \s
                            \s
                    f0      \s
                            \s
                            \s
                    """
                    // START OF LEVEL (Original Part 1)
                    + """
                          f0\s
                            \s
                            \s
                        s0  \s
                            \s
                            \s
                      f0    \s
                            \s
                    s0      \s
                            \s
                            \s
                    r0      \s
                    """;
    private static final String WORLD_STRING_STANDARD = WORLD_STRING_STANDARD_PART1 + EMPTY_SPACER_CHUNK + "// REPEATING STANDARD PART 1 FOR LENGTH\n" + WORLD_STRING_STANDARD_PART1;
    private static final String WORLD_STRING_EASY_PART1 = // END OF LEVEL 1 (EASY - Original Part 1)
            """
                            \s
                            \s
                            \s
                            \s
                    """
                    // Section: Final Targets
                    + """
                    s0  f0  \s
                            \s
                            \s
                        g0  \s
                            \s
                            \s
                    f0      \s
                            \s
                            \s
                    """
                    // Section: Simple Enemy Wave
                    + """
                    s0      \s
                            \s
                        g1  \s
                            \s
                          f0\s
                            \s
                    s0      \s
                            \s
                            \s
                    """
                    // Section: Isolated Energy Barrier (e2)
                    + """
                    f0  r0  \s
                            \s
                            \s
                        e2    \s
                            \s
                            \s
                    s0      \s
                            \s
                            \s
                    """
                    // Section: More Targets
                    + """
                        g0  \s
                            \s
                    f0      \s
                            \s
                          s0\s
                            \s
                    v0      \s
                            \s
                            \s
                    """
                    // Section: Single Wall (W1)
                    + """
                    s0  f0  \s
                            \s
                            \s
                    w1      \s
                            \s
                            \s
                    g1      \s
                            \s
                            \s
                    """
                    // Section: Intro Targets
                    + """
                    f0      \s
                            \s
                        s0  \s
                            \s
                          g0\s
                            \s
                    f0      \s
                            \s
                            \s
                    """
                    // START OF LEVEL 1 (EASY - Original Part 1)
                    + """
                          f0\s
                            \s
                    s0      \s
                            \s
                        s0  \s
                            \s
                    f0      \s
                            \s
                            \s
                            \s
                    """;
    private static final String WORLD_STRING_EASY = WORLD_STRING_EASY_PART1 + EMPTY_SPACER_CHUNK + "// REPEATING EASY PART 1 FOR LENGTH\n" + WORLD_STRING_EASY_PART1;

    /**
     * Initializes the Level 1.
     */
    public Level1() {
        number = 1;
        name = "Outer Perimeter"; // Name from your provided file
        world = difficulty == Difficulty.EASY ? WORLD_STRING_EASY : WORLD_STRING_STANDARD;

        // all gameobjects should be visible in the beginning
        worldOffsetColumns = world.split("\\R").length;
        worldOffsetLines = 0;
    }
}
