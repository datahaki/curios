// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.ImagePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Outer;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.ext.ArgMin;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.Raster;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Min;

/** used as logo of edelweis */
class Se2onR2Demo implements ShowProvider {
  private static final int RES = 192;
  private final Tensor actions = Tensors.of( //
      Tensors.vector(+0.1, +0.2, +0.3), //
      Tensors.vector(-0.3, +0.2, -0.5), //
      Tensors.vector(-0.2, -0.4, -1.0), //
      Tensors.vector(+0.2, -0.7, -1.5)).unmodifiable();

  public Scalar min(Tensor start) {
    Tensor seed = Tensors.of(start);
    for (int count = 0; count < 4; ++count)
      seed = Flatten.of(Outer.of(Se2onR2Demo::action, actions, seed), 1);
    Tensor tensor = Tensor.of(seed.stream().map(Vector2Norm::of));
    Scalar dist = (Scalar) tensor.stream().reduce(Min::of).get();
    return RealScalar.of(ArgMin.of(tensor) * 0.01).add(dist);
  }

  private static Tensor action(Tensor xya, Tensor uv) {
    return Se2Group.INSTANCE.combine(xya, uv.copy().append(RealScalar.ZERO)).extract(0, 2);
  }

  @Override
  public Show getShow() {
    Tensor x = Subdivide.of(-2, +2, RES - 1);
    Tensor y = Subdivide.of(-2, +2, RES - 1);
    Tensor matrix = Parallelize.matrix((i, j) -> min(Tensors.of(x.Get(i), y.Get(j))), x.length(), y.length());
    Tensor image = Raster.of(matrix, ColorDataGradients.CMYK_REVERSED);
    Show show = new Show();
    show.add(ImagePlot.of(ImageFormat.of(image)));
    return show;
  }

  static void main() {
    new Se2onR2Demo().run();
  }
}
