// code by jph
package ch.alpine.curios.man;

import java.awt.Color;
import java.awt.Container;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGenerator;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.DensityPlot;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.fit.WeiszfeldMethod;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class SpatialMedianShow implements ManipulateProvider {
  public Integer seed = 30;
  public ColorDataGradients cdg = ColorDataGradients.NEON;
  public Color colorP = Color.BLACK;
  public Color colorM = Color.RED;

  /** @param points --- */
  private record Pixel2Coord(Tensor points) {
    Scalar dist(Scalar x, Scalar y) {
      Tensor p = Tensors.of(x, y);
      return points.stream().map(r -> Vector2Norm.between(r, p)).reduce(Scalar::add).get();
    }
  }

  @Override
  public Container getContainer() {
    Show show = new Show();
    RandomGenerator randomGenerator = new Random(seed);
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.unit(), Clips.unit());
    Tensor points = RandomVariate.of(UniformDistribution.unit(), randomGenerator, 15, 2);
    Optional<Tensor> optional = new WeiszfeldMethod(Chop._10).uniform(points);
    if (optional.isPresent()) {
      Tensor solution = optional.get();
      Pixel2Coord some = new Pixel2Coord(points);
      show.add(DensityPlot.of(some::dist, cbb, cdg));
      for (Tensor point : points)
        show.add(ListLinePlot.of(Tensors.of(solution, point))).setColor(new Color(64, 64, 64, 64));
      show.add(ListPlot.of(points)).setColor(colorP);
      show.add(ListPlot.of(Tensors.of(solution))).setColor(colorM);
    }
    return ShowGridComponent.of(show);
  }

  static void main() {
    new SpatialMedianShow().runStandalone();
  }
}
