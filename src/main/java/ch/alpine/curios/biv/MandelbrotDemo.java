// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Complex;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.AbsSquared;
import ch.alpine.tensor.sca.Arg;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class MandelbrotDemo extends DensityPlotProvider {
  private static final Scalar FOUR = RealScalar.of(4.0);
  public Integer depth = 80;
  public Integer limit = 6;

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    final Scalar c = Complex.of(re, im);
    Scalar arg = DoubleScalar.INDETERMINATE;
    Scalar z = c;
    for (int index = 0; index < depth; ++index) {
      z = z.multiply(z).add(c);
      if (Scalars.lessThan(FOUR, AbsSquared.FUNCTION.apply(z)))
        return DoubleScalar.INDETERMINATE;
      if (index <= limit)
        arg = Arg.FUNCTION.apply(z);
    }
    return arg;
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.interval(-1.4, 0.5), Clips.interval(-1, +1));
  }

  static void main() {
    new MandelbrotDemo().runStandalone();
  }
}
