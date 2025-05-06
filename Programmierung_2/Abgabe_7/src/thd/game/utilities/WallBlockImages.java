package thd.game.utilities;

public class WallBlockImages {
    /**
     * This is an example {@code wallDescription} String for a sample wall.
     */
    public static final String SAMPLE_WALL = """
xxxxxxxxxx                 \s
xxxxxxxxxx                 \s
xxxxxxxxxxxxxxxxxxxxx      \s
xx     xxxxxxxxxxxxxx      \s
xx     xxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxx          xxxxxxx
xxxxxxxxxxx          xxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxx
""";

    /**
     * Smaller alternative to {@link #SAMPLE_WALL} that is faster to compute.
     */
    public static final String SMALL_SAMPLE_WALL = """
xxxxxxx         xxxxxx
xxxxxxx         xxxxxx
xxxxxxxxxxxxxxxxxxxxxx
""";

    static final String FULL_BLOCK_FRONT = """
LL
LLLL
LLggLL
LLggggLL
LLggggggLL
LLggggggggLL
LLggggggggggLL
LLggggggggggggLL
LLggggggggggggggLL
  LLggggggggggggLL
    LLggggggggggLL
      LLggggggggLL
        LLggggggLL
          LLggggLL
            LLggLL
              LLLL
                LL
""";
    static final String HALF_BLOCK_FRONT = """
LL
LLLL
LLggLL
LLggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
  LLggggLL
    LLggLL
      LLLL
        LL
""";

    static final String FULL_BLOCK_TOP = """
      LLLL
    LLggggLL
  LLggggggggLL
LLggggggggggggLL
LLggggggggggggggLL
  LLggggggggggggggLL
    LLggggggggggggggLL
      LLggggggggggggggLL
        LLggggggggggggLL
          LLggggggggLL
            LLggggLL
              LLLL
""";

    static final String HALF_BLOCK_TOP = """
      LLLL
    LLggggLL
  LLggggggggLL
LLggggggggggggLL
LLggggggggggggggLL
  LLggggggggggggLL
    LLggggggggLL
      LLggggLL
        LLLL
""";

    static final String BLOCK_SIDE = """
      LLLL
    LLggLL
  LLggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggLLLL
LLggLL
LLLL
""";

    static final String BLOCK_SIDE_FULL_TOP = """
      LLLL
    LLggLL
  LLggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggLLLL
LLggLLggggLL
LLLLggggggggLL
LLggggggggggggLL
LLggggggggggggggLL
  LLggggggggggggggLL
    LLggggggggggggggLL
      LLggggggggggggggLL
        LLggggggggggggLL
          LLggggggggLL
            LLggggLL
              LLLL
""";

    static final String BLOCK_SIDE_HALF_TOP = """
      LLLL
    LLggLL
  LLggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggggLL
LLggggLLLL
LLggLLggggLL
LLLLggggggggLL
LLggggggggggggLL
LLggggggggggggggLL
  LLggggggggggggLL
    LLggggggggLL
      LLggggLL
        LLLL
""";

    static final String BLOCK_SIDE_HALF = """
LL
LLLL
LLggLL
LLggggLL
LLggggggLL
LLggggLL
LLggLL
LLLL
""";
}
