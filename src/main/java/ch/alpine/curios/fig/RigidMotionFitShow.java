// code by jph
package ch.alpine.curios.fig;

import java.util.Random;
import java.util.random.RandomGenerator;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ArrayPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.fit.RigidMotionFit;
import ch.alpine.sophus.lie.so2.ArcTan2D;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.red.Mean;

@ReflectionMarker
public class RigidMotionFitShow implements ManipulateProvider {
  public Integer seed = 13;
  public Integer n = 4;
  public Integer res = 64;
  public ColorDataGradients cdg = ColorDataGradients.CLASSIC;

  @Override
  public JComponent getContainer() {
    return ShowGridComponent.of(getShow());
  }

  public Show getShow() {
    RandomGenerator randomGenerator = new Random(seed);
    Distribution distribution = NormalDistribution.standard();
    Tensor random = RandomVariate.of(distribution, randomGenerator, n, 2);
    Tensor mean = Mean.of(random).negate();
    Tensor shuffl = Tensor.of(random.stream().map(mean::add));
    final Tensor target = Join.of(Array.zeros(1, 2), shuffl);
    Tensor points = target.copy();
    Tensor param = Subdivide.of(-10, 10, res);
    Scalar[][] array = new Scalar[res][res];
    for (int x = 0; x < res; ++x)
      for (int y = 0; y < res; ++y) {
        points.set(Tensors.of(param.get(x), param.get(y)), 0);
        RigidMotionFit rigidMotionFit = RigidMotionFit.of(target, points);
        Tensor rotation = rigidMotionFit.rotation(); // 2 x 2
        Scalar angle = ArcTan2D.of(rotation.get(Tensor.ALL, 0));
        array[x][y] = angle;
      }
    Show show = new Show();
    show.add(ArrayPlot.of(Tensors.matrix(array), cdg));
    return show;
  }

  static void main() {
    new RigidMotionFitShow().runStandalone();
  }
}
