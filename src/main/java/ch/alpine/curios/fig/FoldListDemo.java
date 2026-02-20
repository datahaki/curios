// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Accumulate;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Timing;

class FoldListDemo implements ShowProvider {
  private static final int NUMEL = 1_000_000;

  @Override
  public Show getShow() {
    Distribution distribution = UniformDistribution.unit();
    Tensor t_ser = Tensors.empty();
    Tensor t_par = Tensors.empty();
    for (int count = 0; count < 10; ++count) {
      Scalar index = RealScalar.of(count);
      Tensor tensor = RandomVariate.of(distribution, NUMEL);
      {
        Timing timing = Timing.started();
        FoldListTry.of(Tensor::add, tensor);
        t_ser.append(Tensors.of(index, timing.nanoSeconds()));
      }
      {
        Timing timing = Timing.started();
        Accumulate.of(tensor);
        t_par.append(Tensors.of(index, timing.nanoSeconds()));
      }
    }
    Show show = new Show();
    show.setPlotLabel("Mat . Vec");
    show.add(ListLinePlot.of(t_ser)).setLabel("serial");
    show.add(ListLinePlot.of(t_par)).setLabel("parallel");
    return show;
  }

  static void main() {
    new FoldListDemo().runStandalone();
  }
}
