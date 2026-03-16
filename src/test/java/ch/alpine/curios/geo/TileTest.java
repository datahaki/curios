package ch.alpine.curios.geo;

import java.util.Objects;

import org.junit.jupiter.api.Test;

class TileTest {
  @Test
  void test() {
    {
      int x = 12;
      int y = 23;
      IO.println(Objects.hash(x, y));
    }
    {
      long x = 12;
      long y = 23;
      IO.println(Objects.hash(x, y));
    }
  }
}
