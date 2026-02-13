// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.ArrayPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.sophis.fit.RigidMotionFit;
import ch.alpine.sophus.lie.so2.ArcTan2D;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.red.Mean;

class RigidMotionFitShow implements ShowProvider {
  private static Tensor shufflePoints(int n) {
    Distribution distribution = NormalDistribution.standard();
    Tensor random = RandomVariate.of(distribution, n, 2);
    Tensor mean = Mean.of(random).negate();
    return Tensor.of(random.stream().map(mean::add));
  }

  @Override
  public Show getShow() {
    Tensor target = Array.zeros(1, 2);
    Tensor shuffl = shufflePoints(4);
    shuffl.forEach(target::append); // TODO Join
    Tensor points = target.copy();
    int RES = 128;
    Tensor param = Subdivide.of(-10, 10, RES);
    Scalar[][] array = new Scalar[RES][RES];
    for (int x = 0; x < RES; ++x)
      for (int y = 0; y < RES; ++y) {
        points.set(Tensors.of(param.get(x), param.get(y)), 0);
        RigidMotionFit rigidMotionFit = RigidMotionFit.of(target, points);
        Tensor rotation = rigidMotionFit.rotation(); // 2 x 2
        Scalar angle = ArcTan2D.of(rotation.get(Tensor.ALL, 0));
        array[x][y] = angle;
      }
    Show show = new Show();
    show.setAspectRatioOne();
    show.add(ArrayPlot.of(Tensors.matrix(array), ColorDataGradients.HUE));
    return show;
  }

  static void main() {
    new RigidMotionFitShow().run();
  }
}
