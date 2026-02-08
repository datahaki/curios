// code by jph
package ch.alpine.curios.pdf;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.pdf.CDF;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.GammaDistribution;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.var.CubicVariogram;

class GammaDistributionDemo implements ShowProvider {
  @Override
  public Show getShow() {
    double alpha = 1.1;
    Distribution d1 = GammaDistribution.of(alpha, 1);
    Distribution d2 = HistogramDistribution.of(RandomVariate.of(d1, 3000));
    Distribution d3 = GammaDistribution.of(alpha, 1.0);
    Show show = new Show();
    Clip clip = Clips.positive(10);
    show.add(Plot.of(PDF.of(d1)::at, clip)).setLabel("gamma");
    show.add(Plot.of(PDF.of(d2)::at, clip)).setLabel("hist");
    show.add(Plot.of(CDF.of(d2)::p_lessEquals, clip)).setLabel("hist cdf");
    show.add(Plot.of(PDF.of(d3)::at, clip)).setLabel("gamma ab");
    show.add(Plot.of(new CubicVariogram(RealScalar.of(0.3), RealScalar.of(4)), clip)).setLabel("cubic var");
    return show;
  }

  static void main() {
    new GammaDistributionDemo().run();
  }
}
