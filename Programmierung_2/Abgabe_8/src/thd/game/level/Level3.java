package thd.game.level;

public class Level3 extends Level{

    public Level3() {
        index = 2;
        worldString = """
                   f0   \s
                        \s
                        \s
                        \s
                        \s
                  f0  g1\s
                        \s
                     f0 \s
                        \s
                      g1\s
                        \s
                        \s
                        \s
                        \s
                        \s
                  w1    \s
                        \s
                        \s
                        \s
                        \s
                  r0    \s
                        \s
                        \s
                        \s
                 r0   r0\s
                        \s
                        \s
                        \s
                        \s
                        \s
                        \s
                        \s
                 g0     \s
                   v0   \s
                        \s
                      g1\s
                        \s
                        \s
                      r0\s
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
                w2      \s
                """;

        // all gameobjects should be visible in the beginning
        worldOffsetColumns = worldString.split("\\R").length;
        worldOffsetLines = 0;
    }
}
