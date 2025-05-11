package thd.game.level;

public class Level1 extends Level {

    public Level1() {
        index = 0;
        worldString =
// END OF LEVEL
                """
                        \s
                        \s
                        \s
                        \s
                """ +
// Section: Final Targets & Simple Wall
                        """
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
                        """ +
// Section: Mid Gaps (W5) - Simplified
                        """
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
                        """ +
// Section: Enemy Wave 2 (More open space)
                        """
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
                        """ +
// Section: Diagonal Traversal (W3 - Wider Slit) - Kept as a mild challenge
                        """
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
                        """ +
// Section: Enemy Wave 1 (Focus on shooting)
                        """
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
                        """ +
// Section: Mid-Passage Wall (W2) & Radar
                        """
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
                        """ +
// Section: Low Wall Challenge (W6)
                        """
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
                        """ +
// Section: High Wall Challenge (W0)
                        """
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
                        """ +
// Section: Intro to Targets & Low Wall (W1)
                        """
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
                        """ +
// START OF LEVEL (Initial Objects)
                        """
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
                                \s
                        """;



        // all gameobjects should be visible in the beginning
        worldOffsetColumns = worldString.split("\\R").length;
        worldOffsetLines = 0;
    }
}
