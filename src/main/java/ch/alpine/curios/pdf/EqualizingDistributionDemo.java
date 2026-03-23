// code by jph
package ch.alpine.curios.pdf;

import java.awt.Container;

import ch.alpine.bridge.fig.PlotOption;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.InverseCDF;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.EqualizingDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.pdf.d.CategoricalDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class EqualizingDistributionDemo implements ManipulateProvider {
  public Integer samples = 20;

  @Override
  public Container getContainer() {
    Tensor unscaledPDF = RandomVariate.of(UniformDistribution.unit(30), samples);
    CategoricalDistribution dist1 = CategoricalDistribution.fromUnscaledPDF(unscaledPDF);
    Distribution dist2 = EqualizingDistribution.fromUnscaledPDF(unscaledPDF);
    Show show1 = new Show();
    show1.setShowLabel("PDF");
    show1.add(ListPlot.of(dist1::at, Range.of(0, 20))).setLabel("CategoricalDistribution");
    show1.add(Plot.of(PDF.of(dist2)::at, Clips.positive(20), PlotOption.FILL)).setLabel("EqualizingDistribution");
    Clip clip = Clips.unit();
    Show show2 = new Show();
    show2.setShowLabel("CDF");
    show2.add(Plot.of(InverseCDF.of(dist1)::quantile, clip, PlotOption.STRICT));
    show2.add(Plot.of(InverseCDF.of(dist2)::quantile, clip, PlotOption.STRICT));
    return ShowGridComponent.of(show1, show2);
  }

  static void main() {
    new EqualizingDistributionDemo().runStandalone();
  }
}
