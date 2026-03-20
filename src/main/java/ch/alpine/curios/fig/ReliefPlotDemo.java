// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.ReliefPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.tri.Sin;

@ReflectionMarker
class ReliefPlotDemo implements ManipulateProvider {
  public Integer resolution = 100;

  @Override
  public Container getContainer() {
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.absolute(4.0), Clips.absolute(4.0));
    Tensor matrix = mesheval((x, y) -> Sin.FUNCTION.apply(Vector2NormSquared.of(Tensors.of(x, y))).add(y), cbb, resolution);
    Show show = new Show();
    show.setPlotLabel("ArrowPlot");
    Showable showable = ReliefPlot.of(matrix, cbb, ColorDataGradients.ALPINE);
    show.add(showable);
    show.setAspectRatioOne();
    return ShowGridComponent.of(show);
  }

  static void main() {
    new ReliefPlotDemo().runStandalone();
  }

  private static Tensor mesheval(ScalarBinaryOperator sbo, CoordinateBoundingBox cbb, int resolution) {
    // TODO BRIDGE resolution based on aspect ratio and cbb ?
    Tensor dx = Subdivide.intermediate_increasing(cbb.clip(0), resolution);
    Tensor dy = Subdivide.intermediate_decreasing(cbb.clip(1), resolution);
    return Tensor.of(dy.stream().parallel() //
        .map(Scalar.class::cast) //
        .map(y -> Tensor.of(dx.stream().map(Scalar.class::cast).map(x -> sbo.apply(x, y)))));
  }
}
