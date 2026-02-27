// code by jph
package ch.alpine.curios.biv;

import ch.alpine.sophis.noise.PerlinContinuousNoise;
import ch.alpine.sophis.noise.SimplexContinuousNoise;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

enum NoiseDemo implements DensityPlotProvider {
  SIMPLEX {
    @Override
    public Scalar apply(Scalar x, Scalar y) {
      return SimplexContinuousNoise.FUNCTION.apply(Tensors.of(x, y));
    }
  },
  PERLIN {
    @Override
    public Scalar apply(Scalar x, Scalar y) {
      return RealScalar.of(PerlinContinuousNoise.FUNCTION.at(x.number().doubleValue(), y.number().doubleValue()));
    }
  };

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.unit(), Clips.unit());
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.TEMPERATURE;
  }

  static void main() {
    NoiseDemo.SIMPLEX.runStandalone();
  }
}
