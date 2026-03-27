// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophus.clt.ClothoidBuilders;
import ch.alpine.sophus.clt.LagrangeQuadraticD;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.InvertUnlessZero;

@ReflectionMarker
class ClothoidFigure extends DensityPlotProvider {
  public ClothoidBuilders clothoidBuilders = ClothoidBuilders.SE2_ANALYTIC;
  public Tensor q0 = Array.zeros(3);
  @FieldClip(min = "-6.2831853071795", max = "6.2831853071795")
  @FieldSlider(showValue = true)
  public Scalar angle = RealScalar.of(2.6);

  @Override
  public Scalar apply(Scalar x, Scalar y) {
    Tensor q = Tensors.of(x, y, angle);
    LagrangeQuadraticD lagrangeQuadraticD = clothoidBuilders.clothoidBuilder().curve(q0, q).curvature();
    return InvertUnlessZero.FUNCTION.apply(lagrangeQuadraticD.maxAbs());
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.absolute(1.0), Clips.absolute(1.0));
  }

  static void main() {
    new ClothoidFigure().runStandalone();
  }
}
