// code by jph
package ch.alpine.curios.pdf;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.FisherZDistribution;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class FisherZDistributionDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Distribution d1 = FisherZDistribution.of(2.7, 1.3);
    Distribution d2 = HistogramDistribution.of(RandomVariate.of(d1, 10000));
    Show show = new Show();
    Clip clip = Clips.positive(10);
    show.add(Plot.of(PDF.of(d1)::at, clip)).setLabel("beta");
    show.add(Plot.of(PDF.of(d2)::at, clip)).setLabel("hist");
    return show;
  }

  static void main() {
    new FisherZDistributionDemo().run();
  }
}
