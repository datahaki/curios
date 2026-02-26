// code by jph
package ch.alpine.curios.pdf;

import java.awt.Container;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.pdf.InverseCDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UnivariateDistribution;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
enum HistogramDistributionDemo implements ManipulateProvider {
  INSTANCE;

  public Integer samples = 2000;

  @Override
  public Container getContainer() {
    UnivariateDistribution dist = (UnivariateDistribution) NormalDistribution.of(1, 2);
    HistogramDistribution distribution = (HistogramDistribution) //
    HistogramDistribution.of(RandomVariate.of(dist, samples), RealScalar.of(0.25));
    Show show1 = new Show();
    Show show2 = new Show();
    {
      Clip clip = Clips.interval(-5, 8);
      show1.add(Plot.of(dist::at, clip)).setLabel("PDF");
      show1.add(Plot.of(dist::p_lessEquals, clip)).setLabel("CDF");
      show1.add(Plot.of(distribution::at, clip)).setLabel("PDF");
      show1.add(Plot.of(distribution::p_lessEquals, clip)).setLabel("CDF");
    }
    {
      Clip clip = Clips.unit();
      show2.setPlotLabel("InverseCDF");
      show2.add(Plot.of(InverseCDF.of(distribution)::quantile, clip));
      show2.add(Plot.of(InverseCDF.of(dist)::quantile, clip));
    }
    return ShowGridComponent.of(show1, show1);
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
