package thd.game.level;

public class Level2 extends Level{

    public Level2() {
        index = 1;
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
                w1      \s
                """;

        // all gameobjects should be visible in the beginning
        worldOffsetColumns = worldString.split("\\R").length;
        worldOffsetLines = 0;
    }
}
