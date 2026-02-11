// code by jph
package ch.alpine.curios.biv;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Arg;
import ch.alpine.tensor.sca.Clips;

/* package */ record MandelbrotDemo(int depth) implements DensityPlotProvider {
  private static final Scalar TWO = RealScalar.of(2.0);

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    final Scalar c = ComplexScalar.of(re, im);
    Scalar arg = null;
    Scalar z = c;
    for (int index = 0; index < depth; ++index) {
      z = z.multiply(z).add(c);
      if (Scalars.lessThan(TWO, Abs.FUNCTION.apply(z)))
        return DoubleScalar.INDETERMINATE;
      if (index <= 6)
        arg = Arg.FUNCTION.apply(z);
    }
    return arg;
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.interval(-1.4, -1.0), Clips.interval(+0.0, +0.4));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.RAINBOW;
  }

  static void main() {
    new MandelbrotDemo(50).run();
  }
}
