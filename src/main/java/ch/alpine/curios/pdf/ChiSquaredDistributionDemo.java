// code by jph
package ch.alpine.curios.pdf;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.c.ChiSquareDistribution;
import ch.alpine.tensor.pdf.c.GammaDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class ChiSquaredDistributionDemo implements ShowProvider {
  @Override
  public Show getShow() {
    double alpha = 2.3;
    Distribution d1 = GammaDistribution.of(alpha / 2, 2);
    Distribution d2 = ChiSquareDistribution.of(alpha);
    Show show = new Show();
    Clip clip = Clips.positive(10);
    show.add(Plot.of(PDF.of(d1)::at, clip)).setLabel("gamma");
    show.add(Plot.of(PDF.of(d2)::at, clip)).setLabel("chisquared");
    return show;
  }

  static void main() {
    new ChiSquaredDistributionDemo().runStandalone();
  }
}
