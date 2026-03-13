// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.PlotOption;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.var.VariogramFunctions;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class VariogramFunctionsDemo implements ManipulateProvider {
  public VariogramFunctions vF = VariogramFunctions.GAUSSIAN;
  @FieldSlider
  @FieldClip(min = "0", max = "2")
  public Scalar param = RealScalar.ONE;

  @Override
  public Container getContainer() {
    Show show = new Show();
    Clip clipx = Clips.interval(0, 2);
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
    new VariogramFunctionsDemo().runStandalone();
  }
}
