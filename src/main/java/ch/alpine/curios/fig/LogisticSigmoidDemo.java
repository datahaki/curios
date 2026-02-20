// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Ramp;
import ch.alpine.tensor.sca.UnitStep;
import ch.alpine.tensor.sca.exp.DLogisticSigmoid;
import ch.alpine.tensor.sca.exp.LogisticSigmoid;

@ReflectionMarker
class LogisticSigmoidDemo implements ManipulateProvider {
  @Override
  public Container getContainer() {
    Show show1 = new Show();
    {
      Clip clip = Clips.absolute(3);
      show1.add(Plot.of(LogisticSigmoid.FUNCTION, clip)).setLabel("Function");
      show1.add(Plot.of(DLogisticSigmoid.FUNCTION, clip)).setLabel("Derivative");
      show1.add(Plot.of(DLogisticSigmoid.NESTED, clip)).setLabel("Nested");
    }
    Show show2 = new Show();
    {
      Clip clip = Clips.absolute(3);
      show2.add(Plot.of(Ramp.FUNCTION, clip)).setLabel("Function");
      show2.add(Plot.of(UnitStep.FUNCTION, clip)).setLabel("Derivative");
    }
    return ShowGridComponent.of(show1, show2);
  }

  static void main() {
    new LogisticSigmoidDemo().runStandalone();
  }
}
