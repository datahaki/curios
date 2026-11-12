// code by jph
package ch.alpine.curios.opt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

class RosenbrockTest {
  @Test
  void test() {
    Scalar min = Rosenbrock.FUNCTION.apply(RealScalar.ONE, RealScalar.ONE);
    assertEquals(min, RealScalar.ZERO);
  }
}
