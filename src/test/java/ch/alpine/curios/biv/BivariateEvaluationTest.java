// code by jph
package ch.alpine.curios.biv;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.bridge.fig.Show;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.tri.ArcCosh;
import ch.alpine.tensor.sca.tri.ArcSinh;
import ch.alpine.tensor.sca.tri.ArcTanh;

class BivariateEvaluationTest {
  @TempDir
  Path tempDir;

  static List<BivariateEvaluation> bivariateEvaluations() {
    return Arrays.asList( //
        new BetaDemo(2), //
        new GammaDemo(2), //
        new GaussScalarDemo(719), //
        new InverseTrigDemo(ArcSinh.FUNCTION, ArcCosh.FUNCTION, ArcTanh.FUNCTION), //
        new JuliaSinDemo(ComplexScalar.of(1.1, 0.5)), //
        new MandelbrotDemo(30), //
        new NewtonDemo(Tensors.vector(1, 5, 0, 1)), //
        new SinDemo(3), //
        new WeierstrassDemo(10));
  }

  @ParameterizedTest
  @MethodSource("bivariateEvaluations")
  void testSimple(BivariateEvaluation bivariateEvaluation) throws IOException {
    String string = bivariateEvaluation.getClass().getSimpleName();
    Show show = bivariateEvaluation.getShow();
    show.export(tempDir.resolve(string + ".png"), new Dimension(300, 300));
  }
}
