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
import ch.alpine.tensor.sca.gam.Gamma;

/** inspired by Mathematica's documentation of Gamma */
/* package */ record GammaDemo(int depth) implements BivariateEvaluation {
  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar seed = ComplexScalar.of(re, im);
    try {
      return Arg.FUNCTION.apply(Nest.of(Gamma.FUNCTION, seed, depth));
    } catch (Exception exception) {
      System.out.println("fail=" + seed);
    }
    return DoubleScalar.INDETERMINATE;
  }

  @Override
  public Clip clipX() {
    return Clips.interval(-1.25, -0.6);
  }

  @Override
  public Clip clipY() {
    return Clips.interval(-0.25, +0.25);
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.HUE;
  }

  static void main() {
    new GammaDemo(2).run();
  }
}
