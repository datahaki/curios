// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.sophus.rsm.RingRandomSample;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomSample;

record RingRandomSampleDemo(Number r1, Number r2) implements ShowProvider {
  @Override
  public Show getShow() {
    RingRandomSample randomSampleInterface = //
        new RingRandomSample(2, RealScalar.of(r1), RealScalar.of(r2));
    Tensor matrix = RandomSample.of(randomSampleInterface, 5000);
    Show show = new Show();
    show.add(ListPlot.of(matrix));
    show.setAspectRatioOne();
    return show;
  }

  static void main() {
    new RingRandomSampleDemo(2, 3).runStandalone();
  }
}
