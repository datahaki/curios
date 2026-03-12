// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.PlotOption;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.var.VariogramFunctions;

@ReflectionMarker
class VariogramFunctionsShow implements ManipulateProvider {
  public transient VariogramFunctions vF = VariogramFunctions.GAUSSIAN;

  @Override
  public Container getContainer() {
    Scalar[] params = { RealScalar.ZERO, RealScalar.of(0.1), Rational.HALF, RealScalar.ONE, RealScalar.TWO };
    Show show = new Show();
    Clip clipx = Clips.interval(0, 2);
    for (Scalar param : params)
      try {
        ScalarUnaryOperator suo = vF.of(param);
        show.add(Plot.of(suo, Clips.interval(0, 2), PlotOption.STRICT)).setLabel(suo.toString());
      } catch (Exception exception) {
        // System.out.println("doesnt work: "+variograms);
      }
    show.setCbb(CoordinateBoundingBox.of(clipx, Clips.interval(-0.3, 2)));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new VariogramFunctionsShow().runStandalone();
  }
}
