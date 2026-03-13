// code by jph
package ch.alpine.curios.man;

import java.util.stream.IntStream;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.ImagePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.ShowOption;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.ext.ResourceData;
import ch.alpine.tensor.io.ImageFormat;

@ReflectionMarker
public class ImageFiltersDemo implements ManipulateProvider {
  public static final Tensor IMAGE = ImageFormat.from( //
      ResourceData.bufferedImage("ch/alpine/curios/man/ca54d607.png")).unmodifiable();
  // ---
  public ImageFilters imageFilters = ImageFilters.MEDIAN;
  @FieldSlider
  @FieldClip(min = "0", max = "10")
  public Integer width = 2;

  @Override
  public JComponent getContainer() {
    Tensor image = IMAGE.copy();
    IntStream.range(0, 3).parallel().forEach(index -> //
    image.set(imageFilters.filter(image.get(Tensor.ALL, Tensor.ALL, index), width), //
        Tensor.ALL, Tensor.ALL, index));
    Show show = new Show();
    show.setPlotLabel(Dimensions.of(image).toString());
    show.add(ImagePlot.of(ImageFormat.of(image)));
    show.set(ShowOption.GRID, false);
    return ShowGridComponent.of(show);
  }

  static void main() {
    new ImageFiltersDemo().runStandalone();
  }
}
