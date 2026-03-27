// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.noise.NativeContinuousNoise;
import ch.alpine.sophis.noise.SimplexContinuousNoise;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.UnitStep;

@ReflectionMarker
class R2NoisePlot implements DensityPlotProvider {
  private static final NativeContinuousNoise NOISE = SimplexContinuousNoise.FUNCTION;
  private static final Clip CLIP = Clips.absolute(2);

  @Override
  public Scalar apply(Scalar x, Scalar y) {
    return UnitStep.FUNCTION.apply(NOISE.apply(Tensors.of(x, y)));
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(CLIP, CLIP);
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.ALPINE;
  }

  static void main() {
    new R2NoisePlot().runStandalone();
  }
}
