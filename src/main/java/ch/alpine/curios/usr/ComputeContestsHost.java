// code by jph1
package ch.alpine.curios.usr;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.math.SI;
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
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Round;

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
    Showable s1;
    Showable s2;
    s1 = show.add(ListLinePlot.of(t_ser));
    s2 = show.add(ListLinePlot.of(t_par));
    Scalar t1 = Total.ofVector(t_ser.get(Tensor.ALL, 1));
    Scalar t2 = Total.ofVector(t_par.get(Tensor.ALL, 1));
    s1.setLabel(computeContests.label1);
    s2.setLabel(computeContests.label2);
    {
      Tensor rat1 = SI.PERCENT.unitConvert(t1.divide(t2)).maps(Round._2);
      Tensor rat2 = SI.PERCENT.unitConvert(t2.divide(t1)).maps(Round._2);
      show.setShowLabel(computeContests.toString() + " " + rat1 + " " + rat2);
    }
    return ShowGridComponent.of(show);
  }

  static void main() {
    new ComputeContestsHost().runStandalone();
  }
}
