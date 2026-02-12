// code by jph
package ch.alpine.curios.biv;

import ch.alpine.sophus.lie.so.NylanderPower;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

record MandelbulbDemo(int exponent, int depth, Scalar z) implements DensityPlotProvider {
  private static final Scalar THRESHOLD = RealScalar.of(5.0);
  public static final DensityPlotProvider INSTANCE = new MandelbulbDemo(8, 40, RealScalar.of(0.505));

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Tensor c = Tensors.of(re, im, z);
    Tensor x = Tensors.vector(0.0, 0.0, 0.0);
    Scalar nrm = null;
    for (int index = 0; index < depth; ++index) {
      x = NylanderPower.of(x.add(c), exponent);
      if (Scalars.lessThan(THRESHOLD, Vector2NormSquared.of(x)))
        return DoubleScalar.INDETERMINATE;
      if (index == 6)
        nrm = Vector2NormSquared.of(x.add(c)); //
    }
    return nrm;
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.positive(1), Clips.positive(1));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.CLASSIC;
  }

  static void main() {
    INSTANCE.run();
  }
}
