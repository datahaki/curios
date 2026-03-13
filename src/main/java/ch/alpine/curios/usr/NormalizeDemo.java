// code by jph
package ch.alpine.curios.usr;

import java.util.Map;
import java.util.stream.Stream;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.red.Tally;

class NormalizeDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Distribution distribution = NormalDistribution.standard();
    Map<Scalar, Long> map = Tally.sorted(Stream.generate(() -> RandomVariate.of(distribution, 1000)) //
        .limit(10000) //
        .map(Vector2Norm.NORMALIZE) //
        .map(Vector2Norm::of));
    Tensor points = Tensor.of(map.entrySet().stream().map(e -> Tensors.of(e.getKey(), RealScalar.of(e.getValue()))));
    Show show = new Show();
    show.add(ListLinePlot.of(points));
    show.add(ListPlot.of(points));
    return show;
  }

  static void main() {
    new NormalizeDemo().runStandalone();
  }
}
