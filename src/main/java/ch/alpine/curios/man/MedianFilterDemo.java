// code by jph
package ch.alpine.curios.man;

import java.util.stream.IntStream;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.ImagePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.img.MedianFilter;
import ch.alpine.tensor.io.ImageFormat;

@ReflectionMarker
public class MedianFilterDemo implements ManipulateProvider {
  @FieldSlider
  @FieldClip(min = "0", max = "10")
  public Integer width = 2;

  @Override
  public JComponent getJComponent() {
    Tensor image = StaticHelper.IMAGE.copy();
    IntStream.range(0, 3).parallel().forEach(index -> //
    image.set(MedianFilter.of(image.get(Tensor.ALL, Tensor.ALL, index), width), //
        Tensor.ALL, Tensor.ALL, index));
    Show show = new Show();
    show.add(ImagePlot.of(ImageFormat.of(image)));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new MedianFilterDemo().run();
  }
}
