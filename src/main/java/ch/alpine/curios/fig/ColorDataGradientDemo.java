// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.plt.ImagePlot;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.ImageResize;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.tensor.sca.Clips;

class ColorDataGradientDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Tensor domain = Subdivide.increasing(Clips.positive(1.0), 255).maps(Tensors::of);
    Tensor result = Tensors.empty();
    for (ColorDataGradients colorDataGradients : ColorDataGradients.values())
      result.append(ImageResize.nearest(Transpose.of(domain.maps(colorDataGradients)), 1, 1));
    Tensor image = Flatten.of(result, 1);
    // IO.println(Dimensions.of(image));
    Show show = new Show();
    show.add(ImagePlot.of(ImageFormat.of(image)));
    show.setAspectRatioDontCare();
    return show;
  }

  static void main() {
    new ColorDataGradientDemo().runStandalone();
  }
}
