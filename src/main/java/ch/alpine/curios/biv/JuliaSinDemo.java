// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Arg;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Im;
import ch.alpine.tensor.sca.tri.Sin;

/** inspired by document by Paul Bourke */
@ReflectionMarker
class JuliaSinDemo extends DensityPlotProvider {
  private static final Scalar MAX = RealScalar.of(50);
  private static final int MAX_ITERATIONS = 10;
  public Scalar c = ComplexScalar.of(1.1, 0.5);

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar z = ComplexScalar.of(re, im);
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      z = Sin.FUNCTION.apply(z).multiply(c);
      if (Scalars.lessThan(MAX, Abs.FUNCTION.apply(Im.FUNCTION.apply(z))))
        return DoubleScalar.INDETERMINATE;
    }
    return Arg.FUNCTION.apply(z);
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.interval(-2.3, +2.3), Clips.interval(-2.3, +2.3));
  }

  static void main() {
    new JuliaSinDemo().runStandalone();
  }
}
