// code by jph
package ch.alpine.curios.exp;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.flow.TimeIntegrators;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.TableBuilder;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class TimeIntegratorsDemo implements ManipulateProvider {
  public DEqs dEqs = DEqs.EXP_DECAY;

  @Override
  public Container getContainer() {
    Show show = new Show();
    show.add(Plot.of(dEqs.exact(), Clips.positive(10))).setLabel("exact");
    ;
    final Scalar h = RealScalar.of(0.1);
    for (TimeIntegrators ti : TimeIntegrators.values()) {
      TableBuilder tableBuilder = new TableBuilder();
      Tensor x = RealScalar.of(1);
      Scalar t = RealScalar.ZERO;
      for (int c = 0; c < 100; ++c) {
        tableBuilder.appendRow(t, x);
        x = ti.step(dEqs, x, null, h);
        t = t.add(h);
      }
      show.add(ListLinePlot.of(tableBuilder.getTable())).setLabel(ti.name());
    }
    return ShowGridComponent.of(show);
  }

  static void main() {
    new TimeIntegratorsDemo().runStandalone();
  }
}
