// code by jph
package ch.alpine.curios.boat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.tensor.sca.Round;

class BoatsTest {
  @ParameterizedTest
  @EnumSource
  void test(Boats boats) {
    boats.boat.textValues(Round._1);
  }
}
