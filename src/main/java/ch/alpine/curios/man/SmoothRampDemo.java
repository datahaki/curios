// code by jph
package ch.alpine.curios.man;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.gbc.amp.SmoothRamp;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Sign;

@ReflectionMarker
class SmoothRampDemo implements ManipulateProvider {
  public Tensor params = Tensors.vector(0.1, 0.3, 1.0);
  public ColorDataLists cdl = ColorDataLists._058;

  @Override
  public Container getContainer() {
    Show show = new Show(cdl.cyclic());
    Clip clip = Clips.absolute(10);
    Flatten.scalars(params).filter(Sign::isPositive).distinct() //
        .forEach(s -> show.add(Plot.of(new SmoothRamp(s), clip)).setLabel(s.toString()));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new SmoothRampDemo().runStandalone();
  }
}
