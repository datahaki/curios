// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.MatrixPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.sophus.math.noise.NativeContinuousNoise;
import ch.alpine.sophus.math.noise.SimplexContinuousNoise;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.UnitStep;

class R2NoisePlot implements ShowProvider {
  private static final NativeContinuousNoise NOISE = SimplexContinuousNoise.FUNCTION;
  private static final int RES = 512;
  private static final Tensor RE = Subdivide.of(0, 5, RES - 1);
  private static final Tensor IM = Subdivide.of(0, 5, RES - 1);
  @SuppressWarnings("unused")
  private static final Clip CLIP = Clips.unit();

  private static Scalar function(int x, int y) {
    return UnitStep.FUNCTION.apply(DoubleScalar.of(NOISE.at( //
        RE.Get(x).number().doubleValue(), //
        IM.Get(y).number().doubleValue())).subtract(RealScalar.of(0.3)));
  }

  @Override
  public Show getShow() {
    Show show = new Show();
    Tensor matrix = Tensors.matrix(R2NoisePlot::function, RES, RES);
    show.add(MatrixPlot.of(matrix));
    return show;
  }

  static void main() {
    new R2NoisePlot().run();
  }
}
