// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.util.Optional;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ImagePlot;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class ColorDataGradientsShow implements ManipulateProvider {
  public ColorDataGradients colorDataGradients = ColorDataGradients.CLASSIC;

  @Override
  public Container getContainer() {
    Show show = new Show(ColorDataLists._109.strict().deriveWithAlpha(192));
    Show shov = new Show();
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
    {
      Tensor domain = Transpose.of(Subdivide.increasing(Clips.unit(), 255).maps(s -> Array.same(s, 20))).maps(colorDataGradients);
      BufferedImage bufferedImage = ImageFormat.of(domain);
      shov.add(ImagePlot.of(bufferedImage));
      shov.setAspectRatioDontCare(); // FIXME does not work
    }
    return ShowGridComponent.of(show, shov);
  }

  static void main() {
    new ColorDataGradientsShow().runStandalone();
  }
}
