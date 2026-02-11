// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.sophus.math.noise.ColoredNoise;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

class ColoredNoiseExport implements ShowProvider {
  @Override
  public Show getShow() {
    Show show = new Show();
    for (Tensor _x : Subdivide.of(0, 2, 10)) {
      Distribution coloredNoise =  ColoredNoise.of(((Scalar) _x).number().doubleValue());
      Tensor tensor = RandomVariate.of(coloredNoise, 1000);
      Showable showable = ListLinePlot.of(Range.of(0, tensor.length()), tensor);
      showable.setLabel("" + _x);
      show.add(showable);
    }
    return show;
  }

  static void main() {
    new ColoredNoiseExport().run();
  }
}
