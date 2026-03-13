// code by jph
package ch.alpine.curios.sca;

import java.awt.Container;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.ply.Polynomial;

@ReflectionMarker
class PolynomialShow implements ManipulateProvider {
  private static final Tensor x = Tensors.fromString("{100[K], 110.0[K], 120[K], 133[K], 140[K], 150[K]}");
  private static final Tensor y = Tensors.fromString("{10[bar], 20[bar], 22[bar], 23[bar], 25[bar], 26.0[bar]}");

  @Override
  public Container getContainer() {
    List<Show> list = new LinkedList<>();
    for (int degree = 0; degree <= 4; ++degree) {
      ScalarUnaryOperator x_to_y = Polynomial.fit(x, y, degree);
      ScalarUnaryOperator y_to_x = Polynomial.fit(y, x, degree);
      Clip domain_x = Clips.interval(Quantity.of(100, "K"), Quantity.of(150, "K"));
      Tensor samples_x = Subdivide.of(Quantity.of(100, "K"), Quantity.of(150, "K"), 50);
      Tensor samples_y = Subdivide.of(Quantity.of(10, "bar"), Quantity.of(26, "bar"), 50);
      samples_x.maps(x_to_y);
      samples_y.maps(y_to_x);
      Show show = new Show();
      show.setPlotLabel("Degree " + degree);
      show.add(ListPlot.of(x, y));
      show.add(Plot.of(x_to_y, domain_x));
      show.add(ListLinePlot.of(samples_y.maps(y_to_x), samples_y));
      list.add(show);
    }
    return ShowGridComponent.of(list);
  }

  static void main() {
    new PolynomialShow().runStandalone();
  }
}
