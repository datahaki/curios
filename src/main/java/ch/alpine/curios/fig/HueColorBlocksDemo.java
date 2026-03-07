// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.ImageShowable;
import ch.alpine.bridge.fig.ImagePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.img.ColorDataIndexed;
import ch.alpine.tensor.img.ImageResize;
import ch.alpine.tensor.io.ImageFormat;

class HueColorBlocksDemo implements ShowProvider {
  @Override
  public Show getShow() {
    ColorDataIndexed colorDataIndexed = HueColorBlocks.of(10, 5);
    Tensor tensor = Tensors.of(Range.of(0, colorDataIndexed.length())).maps(colorDataIndexed);
    Show show = new Show();
    Showable showable = show.add(ImagePlot.of(ImageFormat.of(tensor)));
    ImageShowable arrayShowable = (ImageShowable) showable;
    arrayShowable.setImageResize(ImageResize.DEGREE_0);
    return show;
  }

  static void main() {
    new HueColorBlocksDemo().runStandalone();
  }
}
