// code by jph
package ch.alpine.curios.biv;

import ch.alpine.sophus.clt.ClothoidBuilder;
import ch.alpine.sophus.clt.ClothoidBuilders;
import ch.alpine.sophus.clt.LagrangeQuadraticD;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

record ClothoidFigure(Scalar angle) implements DensityPlotProvider {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();
  public static final DensityPlotProvider INSTANCE = new ClothoidFigure(RealScalar.of(2.6));

  @Override
  public Scalar apply(Scalar x, Scalar y) {
    Tensor q = Tensors.of(x, y, angle);
    LagrangeQuadraticD headTailInterface = CLOTHOID_BUILDER.curve(q.maps(Scalar::zero), q).curvature();
    return headTailInterface.maxAbs().reciprocal();
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.interval(-1, 1), Clips.interval(0.1, 2.1));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.SUNSET;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
