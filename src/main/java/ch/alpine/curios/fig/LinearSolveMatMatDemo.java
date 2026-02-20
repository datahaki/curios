// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Timing;

class LinearSolveMatMatDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Distribution distribution;
    distribution = NormalDistribution.of(1, 4);
    // distribution = PoissonDistribution.of(RealScalar.ONE);
    {
      int n = 100;
      Tensor a = RandomVariate.of(distribution, n, n);
      Tensor b = RandomVariate.of(distribution, n, n);
      Inverse.of(a);
      a.dot(b);
      Parallelize.dot(a, b);
    }
    Tensor timings = Tensors.empty();
    for (int dim = 1; dim < 40; ++dim) {
      Timing timing = Timing.stopped();
      int trials = 50;
      for (int count = 0; count < trials; ++count) {
        Tensor a = RandomVariate.of(distribution, dim, dim);
        Tensor b = RandomVariate.of(distribution, dim, dim);
        timing.start();
        LinearSolve.of(a, b);
        timing.stop();
      }
      timings.append(Tensors.of(RealScalar.of(dim), timing.nanoSeconds().divide(RealScalar.of(trials))));
    }
    Show show = new Show();
    show.setPlotLabel("LinearSolve Mat Mat");
    show.add(ListLinePlot.of(timings)).setLabel("serial");
    return show;
  }

  static void main() {
    new LinearSolveMatMatDemo().runStandalone();
  }
}
