package ch.alpine.curios.geo;

import org.junit.jupiter.api.Test;

class TileCoordinateTest {
  @Test
  void test() {
    long l = 1 << 12;
    IO.println(l);
    l >>= 2;
    IO.println(l);
  }
}
