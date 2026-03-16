package ch.alpine.curios.geo;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;

class EarthCoordinateTest {
  @Test
  void test() {
    TileCoordinate tileCoordinate = EarthCoordinate.from(0, RealScalar.of(0), RealScalar.of(0));
    IO.println(tileCoordinate);
  }
}
