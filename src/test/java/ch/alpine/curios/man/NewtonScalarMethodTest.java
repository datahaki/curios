// code by jph
package ch.alpine.curios.man;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.pow.Sqrt;

class NewtonScalarMethodTest {
  @Test
  void test() {
    NewtonScalarMethod newtonScalarMethod = NewtonScalarMethod.polynomial(Tensors.vector(-2, 0, 1));
    Scalar x = RealScalar.of(2.0);
    Scalar scalar = newtonScalarMethod.apply(x);
    Tolerance.CHOP.requireClose(scalar, Sqrt.FUNCTION.apply(x));
  }
}
