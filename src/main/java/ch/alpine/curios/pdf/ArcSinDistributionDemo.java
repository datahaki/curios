// code by jph
package ch.alpine.curios.pdf;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.PlotOption;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.pdf.CDF;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ArcSinDistribution;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class ArcSinDistributionDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Distribution d1 = ArcSinDistribution.INSTANCE;
    Distribution d2 = HistogramDistribution.of(RandomVariate.of(d1, 3000));
    Show show = new Show();
    Clip clip = Clips.absoluteOne();
    show.add(Plot.of(PDF.of(d1)::at, clip, PlotOption.STRICT)).setLabel("pdf");
    show.add(Plot.of(CDF.of(d1)::p_lessEquals, clip, PlotOption.STRICT)).setLabel("cdf");
    show.add(Plot.of(PDF.of(d2)::at, clip)).setLabel("hist");
    show.add(Plot.of(CDF.of(d2)::p_lessEquals, clip)).setLabel("hist cdf");
    return show;
  }

  static void main() {
    new ArcSinDistributionDemo().runStandalone();
  }
}
