// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;
import java.util.Optional;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.ColorDataLists;

@ReflectionMarker
class ColorDataGradientsShow implements ManipulateProvider {
  public ColorDataGradients colorDataGradients = ColorDataGradients.CLASSIC;

  @Override
  public Container getContainer() {
    Show show = new Show(ColorDataLists._109.strict().deriveWithAlpha(192));
    Optional<Tensor> optional = colorDataGradients.queryTableRgba();
    if (optional.isPresent()) {
      Tensor rgba = optional.orElseThrow();
      show.setPlotLabel(colorDataGradients.toString());
      {
        Tensor domain = Range.of(0, rgba.length());
        show.add(ListLinePlot.of(domain, rgba.get(Tensor.ALL, 0))).setLabel("red");
        show.add(ListLinePlot.of(domain, rgba.get(Tensor.ALL, 1))).setLabel("green");
        show.add(ListLinePlot.of(domain, rgba.get(Tensor.ALL, 2))).setLabel("blue");
      }
    }
    return ShowGridComponent.of(show);
  }

  static void main() {
    new ColorDataGradientsShow().runStandalone();
  }
}
