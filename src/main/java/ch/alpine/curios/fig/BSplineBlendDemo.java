// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.itp.BSplineInterpolation;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.sca.Clips;

class BSplineBlendDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Tensor tensor = UnitVector.of(2, 1);
    Show show = new Show();
    show.setPlotLabel("BSplineInterpolation");
    for (int degree = 0; degree < 4; ++degree) {
      Interpolation interpolation = BSplineInterpolation.of(degree, tensor);
      Showable showable = show.add(Plot.of(interpolation::At, Clips.unit()));
      showable.setLabel("degree " + degree);
    }
    return show;
  }

  static void main() {
    new BSplineBlendDemo().run();
  }
}
