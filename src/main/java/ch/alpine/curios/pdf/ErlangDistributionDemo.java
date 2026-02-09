// code by jph
package ch.alpine.curios.pdf;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.pdf.CDF;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ErlangDistribution;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.GammaDistribution;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

record ErlangDistributionDemo(int k) implements ShowProvider {
  @Override
  public Show getShow() {
    Scalar lambda = RealScalar.of(3.3);
    Distribution d1 = ErlangDistribution.of(k, lambda);
    Distribution d2 = HistogramDistribution.of(RandomVariate.of(d1, 3000));
    Distribution d3 = GammaDistribution.of(RealScalar.of((double) k), lambda.reciprocal());
    Throw.unless(d3 instanceof GammaDistribution);
    Show show = new Show();
    Clip clip = Clips.positive(5);
    show.add(Plot.of(PDF.of(d1)::at, clip)).setLabel("erlang");
    show.add(Plot.of(PDF.of(d2)::at, clip)).setLabel("hist");
    show.add(Plot.of(PDF.of(d3)::at, clip)).setLabel("gamma");
    if (k == 1)
      show.add(Plot.of(PDF.of(ExponentialDistribution.of(lambda))::at, clip)).setLabel("expon");
    show.add(Plot.of(CDF.of(d1)::p_lessEquals, clip)).setLabel("erlang cdf");
    show.add(Plot.of(CDF.of(d2)::p_lessEquals, clip)).setLabel("hist cdf");
    return show;
  }

  static void main() {
    new ErlangDistributionDemo(3).run();
  }
}
