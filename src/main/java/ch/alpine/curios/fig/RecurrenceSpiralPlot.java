// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.LinearRecurrence;
import ch.alpine.tensor.num.ReIm;

@ReflectionMarker
class RecurrenceSpiralPlot implements ManipulateProvider {
  public Tensor a = Tensors.fromString("{-0.6+0.7*I, 0.95*I}");
  public Tensor b = Tensors.vector(1, 1);
  public Integer n = 100;

  @Override
  public Container getContainer() {
    Tensor tensor = new LinearRecurrence(a, b).until(n);
    Show show = new Show();
    show.add(ListLinePlot.of(tensor.maps(ReIm::vector)));
    show.setAspectRatioOne();
    return ShowGridComponent.of(show);
  }

  static void main() {
    new RecurrenceSpiralPlot().runStandalone();
  }
}
