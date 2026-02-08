// code by jph
package ch.alpine.curios.biv;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Arg;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.gam.Beta;

/** inspired by Mathematica's documentation of Beta */
/* package */ record BetaDemo(int depth) implements BivariateEvaluation {
  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar seed = ComplexScalar.of(re, im);
    try {
      return Arg.FUNCTION.apply(Nest.of(z -> Beta.of(z, z), seed, depth));
    } catch (Exception exception) {
      // ---
    }
    return DoubleScalar.INDETERMINATE;
  }

  @Override
  public Clip clipX() {
    return Clips.absolute(2.0);
  }

  @Override
  public Clip clipY() {
    return Clips.absolute(2.0);
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.HUE;
  }

  static void main() {
    new BetaDemo(2).run();
  }
}
