// code by jph
package ch.alpine.curios.biv;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.tri.ArcCosh;
import ch.alpine.tensor.sca.tri.ArcSinh;
import ch.alpine.tensor.sca.tri.ArcTanh;

class BivariateEvaluationTest {
  @Test
  void testSimple() {
    List<BivariateEvaluation> list = Arrays.asList( //
        new BetaDemo(2), //
        new GammaDemo(2), //
        new GaussScalarDemo(719), //
        new InverseTrigDemo(ArcSinh.FUNCTION, ArcCosh.FUNCTION, ArcTanh.FUNCTION), //
        new JuliaSinDemo(ComplexScalar.of(1.1, 0.5)), //
        new MandelbrotDemo(30), //
        new NewtonDemo(Tensors.vector(1, 5, 0, 1)), //
        new SinDemo(3), //
        new WeierstrassDemo(10));
    for (BivariateEvaluation bivariateEvaluation : list)
      StaticHelper.image(bivariateEvaluation, 10);
  }
}
