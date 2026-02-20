// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.var.VariogramFunctions;

// TODO the loops cause overcrowding -> not a good demo
class VariogramFunctionsShow implements ShowProvider {
  @Override
  public Show getShow() {
    Scalar[] params = { RealScalar.ZERO, RealScalar.of(0.1), Rational.HALF, RealScalar.ONE, RealScalar.TWO };
    Show show = new Show();
    show.setAspectRatioOne();
    for (VariogramFunctions variograms : VariogramFunctions.values()) {
      show.setPlotLabel(variograms.toString());
      for (Scalar param : params)
        try {
          show.add(Plot.of(variograms.of(param), Clips.interval(0.5, 2)));
        } catch (Exception exception) {
          // System.out.println("doesnt work: "+variograms);
        }
    }
    return show;
  }

  static void main() {
    new VariogramFunctionsShow().runStandalone();
  }
}
