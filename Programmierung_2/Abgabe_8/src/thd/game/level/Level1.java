package thd.game.level;

public class Level1 extends Level{

    public Level1() {
        index = 0;
        worldString = """
        \s
  f0    \s
        \s
s0      \s
    s0  \s
        \s
  f0    \s
        \s
w1      \s
        \s
        \s
    g0  \s
        \s
f0      \s
        \s
  w1    \s
        \s
        \s
s0  s0  \s
        \s
    f0  \s
        \s
w6      \s
        \s
        \s
  g1    \s
        \s
f0  s0  \s
        \s
  w6    \s
        \s
        \s
    v0  \s
        \s
s0  f0  \s
        \s
        \s
w0      \s
        \s
  e0    \s
        \s
    g0  \s
        \s
f0      \s
        \s
  w0    \s
g1      \s
    e1  \s
        \s
s0  f0  \s
        \s
r0      \s
        \s
w6      \s
        \s
  e4    \s
        \s
    v0  \s
        \s
  f0    \s
        \s
    w6  \s
        \s
e3      \s
    g1  \s
        \s
f0  s0  \s
        \s
w2      \s
        \s
  e0    \s
      e4\s
        \s
g0  g1  \s
        \s
  f0    \s
        \s
    w2  \s
        \s
e0  e4  \s
        \s
v0  s0  \s
        \s
  r0    \s
        \s
f0      \s
        \s
w1      \s
    e3  \s
        \s
  g0    \s
        \s
w0      \s
  e0    \s
      v0\s
        \s
    f0  \s
        \s
w3      \s
        \s
  e0    \s
      e4\s
        \s
g0      \s
    g1  \s
        \s
  f0    \s
        \s
    w3  \s
        \s
e0      \s
e4      \s
        \s
s0  v0  \s
        \s
r0      \s
        \s
w4      \s
        \s
  e0    \s
        \s
    f0  \s
        \s
g1      \s
        \s
  w4    \s
        \s
e0      \s
    e0  \s
        \s
v0  g0  \s
        \s
f0  f0  \s
        \s
w5      \s
        \s
  e0    \s
      e2\s
        \s
s0      \s
    s0  \s
        \s
  f0    \s
        \s
    w5  \s
        \s
e0  e2  \s
        \s
g0  g1  \s
        \s
r0      \s
        \s
w0      \s
  w6    \s
e2      \s
        \s
    f0  \s
        \s
v0  v0  \s
        \s
  w1    \s
w1      \s
    e4  \s
        \s
g0      \s
    g1  \s
        \s
f0      \s
        \s
w2      \s
  e0    \s
      e4\s
        \s
g0  v0  \s
        \s
  f0    \s
        \s
w4      \s
e0      \s
  g1    \s
      e0\s
        \s
r0  s0  \s
        \s
  f0    \s
        \s
w3      \s
  e0    \s
      e4\s
        \s
v0  g0  \s
        \s
    f0  \s
        \s
w0      \s
w6      \s
  e2    \s
      e2\s
        \s
g1  g0  \s
        \s
f0  f0  \s
        \s
r0  r0  \s
        \s
w1  w1  \s
  e4    \s
      e4\s
        \s
v0  v0  \s
        \s
s0  s0  \s
    s0  \s
        \s
f0  f0  \s
  f0    \s
        \s
g0      \s
    g1  \s
        \s
s0  f0  \s
        \s
  v0    \s
        \s
w1      \s
        \s
  w1    \s
        \s
    w1  \s
        \s
f0      \s
        \s
r0      \s
        \s
        \s
        \s
        \s
""";


        // all gameobjects should be visible in the beginning
        worldOffsetColumns = worldString.split("\\R").length;
        worldOffsetLines = 0;
    }
}
