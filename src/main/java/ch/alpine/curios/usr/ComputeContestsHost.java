// code by jph
package ch.alpine.curios.usr;

import java.awt.Container;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;

@ReflectionMarker
class ComputeContestsHost implements ManipulateProvider {
  public ComputeContests computeContests = ComputeContests.MAT_VEC;
  @FieldSelectionArray({ "2[s]", "5[s]", "10[s]" })
  public Scalar timeout = Quantity.of(2, "s");

  @Override
  public Container getContainer() {
    Tensor t_ser = Tensors.empty();
    Tensor t_par = Tensors.empty();
    Timing timing = Timing.started();
    int dim = 2;
    do {
      Timing s_ser = Timing.stopped();
      Timing s_par = Timing.stopped();
      computeContests.runTrials(dim, s_ser, s_par);
      Scalar n = RealScalar.of(dim);
      t_ser.append(Tensors.of(n, s_ser.seconds()));
      t_par.append(Tensors.of(n, s_par.seconds()));
      dim += 2;
    } while (Scalars.lessThan(timing.seconds(), timeout));
    Show show = new Show();
    show.setPlotLabel(computeContests.toString());
    Showable s1;
    Showable s2;
    s1 = show.add(ListLinePlot.of(t_ser));
    s2 = show.add(ListLinePlot.of(t_par));
    s1.setLabel(computeContests.label1);
    s2.setLabel(computeContests.label2);
    return ShowGridComponent.of(show);
  }

  static void main() {
    new ComputeContestsHost().runStandalone();
  }
}
