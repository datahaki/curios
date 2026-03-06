// code by jph
package ch.alpine.curios.biv;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.num.GaussScalar;
import ch.alpine.tensor.num.Prime;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

// TODO
record GaussScalarDemo(int prime) implements DensityPlotProvider {
  public static final DensityPlotProvider INSTANCE = //
      new GaussScalarDemo(Prime.of(100).number().intValue());

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    GaussScalar x = GaussScalar.of(re.number().intValue(), prime);
    GaussScalar y = GaussScalar.of(im.number().intValue(), prime);
    return RealScalar.of(x.divide(y).number());
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.interval(1, prime - 1), Clips.interval(1, prime - 1));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.STARRY_NIGHT;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
