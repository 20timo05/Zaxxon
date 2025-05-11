package thd.game.level;

public class Level2 extends Level{

    public Level2() {
        index = 1;
        worldString =
// END OF LEVEL 2
                """
                        \s
                        \s
                        \s
                        \s
                """ +
// Section: Final Barrage & Pillar Wall
                        """
                        s0  f0  \s
                                \s
                            g1  \s
                                \s
                          v0    \s
                                \s
                        w8      \s
                                \s
                              e4\s
                                \s
                          e0    \s
                                \s
                        r0      \s
                                \s
                                \s
                        """ +
// Section: Dual Slits Challenge (W4)
                        """
                        f0  s0  \s
                                \s
                            g0  \s
                                \s
                              e2\s
                                \s
                        w4      \s
                                \s
                        s0  v0  \s
                                \s
                              f0\s
                                \s
                                \s
                        """ +
// Section: Enemy Wave with Guns
                        """
                        g0  g1  \s
                                \s
                                \s
                        s0  f0s0\s
                                \s
                                \s
                            r0  \s
                                \s
                                \s
                          v0    \s
                                \s
                                \s
                        """ +
// Section: Stairs Up (W7) with Barriers
                        """
                        f0      \s
                                \s
                            g0  \s
                                \s
                              e4\s
                                \s
                          e0    \s
                                \s
                        w7      \s
                                \s
                        s0      \s
                                \s
                              f0\s
                                \s
                                \s
                        """ +
// Section: Mid Gaps (W5) and Rockets
                        """
                            v0  \s
                                \s
                        g1  s0  \s
                                \s
                              e2\s
                                \s
                          e0    \s
                                \s
                        w5      \s
                                \s
                        f0  f0  \s
                                \s
                                \s
                        """ +
// Section: Offset Slits Intro (W9)
                        """
                        s0      \s
                                \s
                            g0  \s
                                \s
                              e3\s
                                \s
                        w9      \s
                                \s
                        f0  r0  \s
                                \s
                                \s
                        """ +
// Section: More Targets, Low Wall (W1)
                        """
                            f0  \s
                                \s
                        s0  g1s0\s
                                \s
                                \s
                        w1      \s
                                \s
                        v0      \s
                                \s
                              f0\s
                                \s
                                \s
                        """ +
// Section: High/Low Combo (W0 then W6) - Adjusted for playability
                        """
                        s0      \s
                                \s
                            g0  \s
                                \s
                                \s
                        w0      \s
                                \s
                              e0\s
                                \s
                                \s
                                \s
                                \s
                              f0\s
                                \s
                                \s
                                \s
                                \s
                        w6      \s
                                \s
                              e4\s
                                \s
                        s0  v0  \s
                                \s
                                \s
                        """ +
// Section: Basic Targets & Mid Passage (W2)
                        """
                        f0  s0  \s
                                \s
                            g1  \s
                                \s
                              e3\s
                                \s
                        w2      \s
                                \s
                        r0      \s
                                \s
                              f0\s
                                \s
                                \s
                        """ +
// START OF LEVEL 2
                        """
                        s0  f0  \s
                                \s
                                \s
                            g0  \s
                                \s
                        s0      \s
                                \s
                              f0\s
                                \s
                        v0      \s
                                \s
                                \s
                                \s
                        """;



        // all gameobjects should be visible in the beginning
        worldOffsetColumns = worldString.split("\\R").length;
        worldOffsetLines = 0;
    }
}
