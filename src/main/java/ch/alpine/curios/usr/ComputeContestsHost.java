// code by jph
package ch.alpine.curios.usr;

import java.awt.Container;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.qty.Timing;

@ReflectionMarker
public class ComputeContestsHost implements ManipulateProvider {
  public ComputeContests computeContests = ComputeContests.MAT_VEC;
  @FieldSelectionArray({ "100", "200" })
  public Integer dims = 100;
  public Boolean connect = true;

  @Override
  public Container getContainer() {
    Tensor t_ser = Tensors.empty();
    Tensor t_par = Tensors.empty();
    for (int dim = 1; dim < dims; ++dim) {
      if (dim % 5 == 0)
        System.out.println(dim);
      Timing s_ser = Timing.stopped();
      Timing s_par = Timing.stopped();
      computeContests.runTrials(dim, s_ser, s_par);
      Scalar n = RealScalar.of(dim);
      t_ser.append(Tensors.of(n, s_ser.seconds()));
      t_par.append(Tensors.of(n, s_par.seconds()));
    }
    Show show = new Show();
    show.setPlotLabel(computeContests.toString());
    Showable s1;
    Showable s2;
    if (connect) {
      s1 = show.add(ListLinePlot.of(t_ser));
      s2 = show.add(ListLinePlot.of(t_par));
    } else {
      s1 = show.add(ListPlot.of(t_ser));
      s2 = show.add(ListPlot.of(t_par));
    }
    s1.setLabel(computeContests.label1);
    s2.setLabel(computeContests.label2);
    return ShowGridComponent.of(show);
  }

  static void main() {
    new ComputeContestsHost().run();
  }
}
