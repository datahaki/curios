// code by jph
package ch.alpine.curios.usr;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowDialog;
import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.ext.Timing;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;

/* package */ enum DotVecVecDemo {
  ;
  static void main() {
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
    for (int dim = 0; dim < 200; ++dim) {
      System.out.println(dim);
      Timing s_ser = Timing.stopped();
      Timing s_par = Timing.stopped();
      int trials = 200;
      for (int count = 0; count < trials; ++count) {
        Tensor a = RandomVariate.of(distribution, dim);
        Tensor b = RandomVariate.of(distribution, dim);
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
      t_ser.append(Tensors.of(n, Quantity.of(s_ser.nanoSeconds() / trials, "ns")));
      t_par.append(Tensors.of(n, Quantity.of(s_par.nanoSeconds() / trials, "ns")));
    }
    Show show = new Show();
    show.setPlotLabel("Vec . Vec");
    show.add(ListPlot.of(t_ser)).setLabel("serial");
    show.add(ListPlot.of(t_par)).setLabel("parallel");
    ShowDialog.of(show);
  }
}
