// code by jph
package ch.alpine.curios.usr;

import java.awt.Container;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Timing;

@ReflectionMarker
public class DotMatMatDemo implements ManipulateProvider {
  public Integer dims = 40;
  public Integer trials = 10;
  public Boolean connect = false;

  @Override
  public Container getContainer() {
    Distribution distribution = NormalDistribution.of(1, 4);
    {
      int n = 100;
      Tensor a = RandomVariate.of(distribution, n, n);
      Tensor b = RandomVariate.of(distribution, n, n);
      a.dot(b);
      Parallelize.dot(a, b);
    }
    Tensor t_ser = Tensors.empty();
    Tensor t_par = Tensors.empty();
    for (int dim = 0; dim < dims; ++dim) {
      if (dim % 5 == 0)
        System.out.println(dim);
      Timing s_ser = Timing.stopped();
      Timing s_par = Timing.stopped();
      for (int count = 0; count < trials; ++count) {
        Tensor a = RandomVariate.of(distribution, dim, dim);
        Tensor b = RandomVariate.of(distribution, dim, dim);
        s_ser.start();
        Tensor cs = a.dot(b);
        s_ser.stop();
        s_par.start();
        Tensor cp = Parallelize.dot(a, b);
        s_par.stop();
        if (!Tolerance.CHOP.isClose(cs, cp))
          throw new Throw(cs);
      }
      Scalar n = RealScalar.of(dim);
      t_ser.append(Tensors.of(n, s_ser.seconds().divide(RealScalar.of(trials))));
      t_par.append(Tensors.of(n, s_par.seconds().divide(RealScalar.of(trials))));
    }
    Show show = new Show();
    show.setPlotLabel("Mat . Mat");
    if (connect) {
      show.add(ListLinePlot.of(t_ser)).setLabel("serial");
      show.add(ListLinePlot.of(t_par)).setLabel("parallel");
    } else {
      show.add(ListPlot.of(t_ser)).setLabel("serial");
      show.add(ListPlot.of(t_par)).setLabel("parallel");
    }
    return ShowGridComponent.of(show);
  }

  static void main() {
    new DotMatMatDemo().run();
  }
}
