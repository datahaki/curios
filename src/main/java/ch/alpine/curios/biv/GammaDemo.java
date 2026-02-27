// code by jph
package ch.alpine.curios.biv;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Arg;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.gam.Gamma;

/** inspired by Mathematica's documentation of Gamma */
record GammaDemo(int depth) implements DensityPlotProvider {
  public static final DensityPlotProvider INSTANCE = new GammaDemo(2);

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar seed = ComplexScalar.of(re, im);
    try {
      return Arg.FUNCTION.apply(Nest.of(Gamma.FUNCTION, seed, depth));
    } catch (Exception exception) {
      // exception.printStackTrace();
      System.out.println("GammaDemo fail=" + seed);
    }
    return DoubleScalar.INDETERMINATE;
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.interval(-1.25, -0.6), Clips.interval(-0.25, +0.25));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.HUE;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
