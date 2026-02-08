// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.ImagePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.ImageResize;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.tensor.sca.Clips;

/* package */ class ColorDataGradientDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Tensor domain = Subdivide.increasing(Clips.positive(1.0), 255).map(Tensors::of);
    Tensor result = Tensors.empty();
    for (ColorDataGradients colorDataGradients : ColorDataGradients.values())
      result.append(ImageResize.nearest(Transpose.of(domain.map(colorDataGradients)), 8, 1));
    Tensor image = Flatten.of(result, 1);
    IO.println(Dimensions.of(image));
    Show show = new Show();
    show.add(ImagePlot.of(ImageFormat.of(image)));
    return show;
  }

  static void main() {
    new ColorDataGradientDemo().run();
  }
}
