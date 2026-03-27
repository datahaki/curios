// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.num.GaussScalar;
import ch.alpine.tensor.num.Prime;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Floor;

@ReflectionMarker
class GaussScalarDemo implements DensityPlotProvider {
  public Integer prime = Scalars.intValueExact(Prime.of(100));

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    GaussScalar x = GaussScalar.of(Floor.intValueExact(re), prime);
    GaussScalar y = GaussScalar.of(Floor.intValueExact(im), prime);
    try {
      return RealScalar.of(x.divide(y).number());
    } catch (Exception e) {
      return DoubleScalar.INDETERMINATE;
    }
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
    new GaussScalarDemo().runStandalone();
  }
}
