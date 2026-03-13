// code by jph
package ch.alpine.curios.pdf;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.pdf.BinningMethods;
import ch.alpine.tensor.pdf.CDF;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ErlangDistribution;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class ErlangDistributionDemo implements ManipulateProvider {
  public Integer k = 1;
  public Scalar lambda = Quantity.of(3.3, "m");
  public Clip clip = Clips.positive(Quantity.of(5, "m^-1"));
  public Integer samples = 3000;
  public BinningMethods binning = BinningMethods.IQR;

  @Override
  public Container getContainer() {
    Distribution d1 = ErlangDistribution.of(k, lambda);
    Distribution d2 = HistogramDistribution.of(RandomVariate.of(d1, samples), binning);
    Show show1 = new Show();
    show1.add(Plot.of(PDF.of(d1)::at, clip)).setLabel("erlang");
    show1.add(Plot.of(PDF.of(d2)::at, clip)).setLabel("hist");
    if (k == 1)
      show1.add(Plot.of(PDF.of(ExponentialDistribution.of(lambda))::at, clip)).setLabel("expon");
    Show show2 = new Show();
    show2.add(Plot.of(CDF.of(d1)::p_lessEquals, clip)).setLabel("erlang cdf");
    show2.add(Plot.of(CDF.of(d2)::p_lessEquals, clip)).setLabel("hist cdf");
    return ShowGridComponent.of(show1, show2);
  }

  static void main() {
    new ErlangDistributionDemo().runStandalone();
  }
}
