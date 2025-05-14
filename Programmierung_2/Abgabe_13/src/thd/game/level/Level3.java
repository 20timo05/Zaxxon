package thd.game.level;

/**
 * Level 3: The most challenging stage, featuring complex new walls
 * (W10, W11), dense enemy formations, intricate puzzles, and
 * multi-threat situations, culminating in a demanding final gauntlet.
 */
public class Level3 extends Level {

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
            // END OF LEVEL 3 (Original Part 1)
            """
                            \s
                            \s
                            \s
                            \s
                    """
                    // Section: Final Gauntlet - Intense!
                    + """
                    g0v0g1  \s
                            \s
                    w10     \s
                            \s
                          e4\s
                            \s
                      e0    \s
                            \s
                    w4      \s
                            \s
                    r0f0r0  \s
                            \s
                    w8      \s
                            \s
                    v0  v0  \s
                            \s
                            \s
                    """
                    // Section: Layered Side Holes (W11) & Funnel (W10)
                    + """
                    s0  g0  \s
                            \s
                          e3\s
                            \s
                    w11     \s
                            \s
                    f0  v0  \s
                            \s
                          e0\s
                            \s
                    w10     \s
                            \s
                    s0  g1s0\s
                            \s
                          f0\s
                            \s
                            \s
                    """
                    // Section: Advanced Weaving (W9) with Crossfire
                    + """
                    g0  f0g1\s
                            \s
                          e4\s
                            \s
                      e0    \s
                            \s
                    w9      \s
                            \s
                    v0  r0v0\s
                            \s
                    s0      \s
                            \s
                          f0\s
                            \s
                            \s
                    """
                    // Section: Stairs (W7) into Dual Slits (W4)
                    + """
                    f0  g0  \s
                            \s
                          e2\s
                            \s
                    w7      \s
                            \s
                    s0  v0  \s
                            \s
                          e0\s
                            \s
                    w4      \s
                            \s
                    g1  f0  \s
                            \s
                    r0      \s
                            \s
                            \s
                    """
                    // Section: Central Pillar (W8) Surrounded
                    + """
                    v0  s0v0\s
                            \s
                        g0g1\s
                            \s
                    w8      \s
                            \s
                          e4\s
                            \s
                      e0    \s
                            \s
                    f0  f0  \s
                            \s
                    s0  r0s0\s
                            \s
                            \s
                    """
                    // Section: Diagonal (W3) & Mid Gaps (W5) Combo
                    + """
                    g0  f0  \s
                            \s
                          e4\s
                            \s
                      e0    \s
                            \s
                    w3      \s
                            \s
                    s0  v0  \s
                            \s
                          e2\s
                            \s
                    w5      \s
                            \s
                    g1  s0  \s
                            \s
                          f0\s
                            \s
                            \s
                    """
                    // Section: High/Low Pressure (W0, W6) with Rockets - Adjusted
                    + """
                    v0  f0v0\s
                            \s
                        g0  \s
                            \s
                            \s
                    w0      \s
                            \s
                          e0\s
                            \s
                    s0  r0s0\s
                            \s
                            \s
                            \s
                            \s
                    w6      \s
                            \s
                          e4\s
                            \s
                    g1  f0g0\s
                            \s
                            \s
                    """
                    // Section: Intro to New Walls (W10, W11) - Milder
                    + """
                    s0  f0  \s
                            \s
                        g1  \s
                            \s
                          e2\s
                            \s
                    w10     \s
                            \s
                    f0  v0  \s
                            \s
                          e3\s
                            \s
                    w11     \s
                            \s
                    s0  g0  \s
                            \s
                          f0\s
                            \s
                            \s
                    """
                    // START OF LEVEL 3 (Original Part 1)
                    + """
                    f0  s0  \s
                            \s
                            \s
                        g0  \s
                            \s
                    v0      \s
                            \s
                    w1      \s
                            \s
                          f0\s
                            \s
                    s0  g1  \s
                            \s
                    r0      \s
                            \s
                            \s
                          r0\s
                    """;

    private static final String WORLD_STRING_STANDARD = WORLD_STRING_STANDARD_PART1 + EMPTY_SPACER_CHUNK + "// REPEATING STANDARD PART 1 FOR LENGTH\n" + WORLD_STRING_STANDARD_PART1;
    private static final String WORLD_STRING_EASY_PART1 =
            // END OF LEVEL 3 (EASY - Original Part 1)
            """
                            \s
                            \s
                            \s
                            \s
                    """
                    // Section: Final Targets
                    + """
                            g0  f0g1\s
                                    \s
                                    \s
                            s0  r0  \s
                                    \s
                                    \s
                            f0      \s
                                    \s
                                    \s
                            """
                    // Section: Second Wall (W3 - Wide Diagonal)
                    + """
                            v0  s0  \s
                                    \s
                                    \s
                            w3      \s
                                    \s
                                    \s
                            g0  f0  \s
                                    \s
                            s0      \s
                                    \s
                                    \s
                            """
                    // Section: Enemy Wave
                    + """
                            r0  g1r0\s
                                    \s
                                    \s
                            s0  f0s0\s
                                    \s
                                    \s
                            v0  v0  \s
                                    \s
                                    \s
                            """
                    // Section: Isolated Energy Barrier (e1)
                    + """
                            f0      \s
                                    \s
                                g0  \s
                                    \s
                              e1    \s
                                    \s
                            s0      \s
                                    \s
                                  f0\s
                                    \s
                                    \s
                            """
                    // Section: First Wall (W2 - Mid Passage)
                    + """
                                v0  \s
                                    \s
                            g1  s0  \s
                                    \s
                                    \s
                            w2      \s
                                    \s
                                    \s
                            f0  r0  \s
                                    \s
                                    \s
                            """
                    // Section: More Targets
                    + """
                            s0  g0s0\s
                                    \s
                                f0  \s
                                    \s
                                  g1\s
                                    \s
                            f0      \s
                                    \s
                            s0      \s
                                    \s
                                    \s
                            """
                    // START OF LEVEL 3 (EASY - Original Part 1)
                    + """
                                f0  \s
                                    \s
                            s0  g0  \s
                                    \s
                                  s0\s
                                    \s
                            f0      \s
                                    \s
                            v0  r0  \s
                                    \s
                                    \s
                                    \s
                            """;
    private static final String WORLD_STRING_EASY = WORLD_STRING_EASY_PART1 + EMPTY_SPACER_CHUNK + "// REPEATING EASY PART 1 FOR LENGTH\n" + WORLD_STRING_EASY_PART1;

    /**
     * Initializes the Level 3.
     */
    public Level3() {
        number = 3;
        name = "The Zaxxon Core";
        world = difficulty == Difficulty.EASY ? WORLD_STRING_EASY : WORLD_STRING_STANDARD;

        // all gameobjects should be visible in the beginning
        worldOffsetColumns = world.split("\\R").length;
        worldOffsetLines = 0;
    }
}
