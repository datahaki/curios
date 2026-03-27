// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.noise.SimplexContinuousNoise;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class NoiseDemo extends DensityPlotProvider {
  @Override
  public Scalar apply(Scalar x, Scalar y) {
    return SimplexContinuousNoise.FUNCTION.apply(Tensors.of(x, y));
  }
  // PERLIN {
  // @Override
  // public Scalar apply(Scalar x, Scalar y) {
  // return RealScalar.of(PerlinContinuousNoise.FUNCTION.at(x.number().doubleValue(), y.number().doubleValue()));
  // }
  // };

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.unit(), Clips.unit());
  }

  static void main() {
    new NoiseDemo().runStandalone();
  }
}
