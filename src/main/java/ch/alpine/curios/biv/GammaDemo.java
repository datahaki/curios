// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Complex;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Arg;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.gam.Gamma;

/** inspired by Mathematica's documentation of Gamma */
@ReflectionMarker
class GammaDemo extends DensityPlotProvider {
  public Integer depth = 2;

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    try {
      return Arg.FUNCTION.apply(Nest.of(Gamma.FUNCTION, Complex.of(re, im), depth));
    } catch (Exception exception) {
      // ---
    }
    return DoubleScalar.INDETERMINATE;
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.interval(-1.25, -0.6), Clips.interval(-0.25, +0.25));
  }

  static void main() {
    new GammaDemo().runStandalone();
  }
}
