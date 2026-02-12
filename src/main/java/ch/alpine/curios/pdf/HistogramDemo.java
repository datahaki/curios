// code by jph
package ch.alpine.curios.pdf;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.sca.Clip;

record HistogramDemo(Distribution distribution, Clip clipX) implements ShowProvider {
  @Override
  public Show getShow() {
    Distribution histogram = HistogramDistribution.of(RandomVariate.of(distribution, 10000));
    Show show = new Show();
    show.add(Plot.of(PDF.of(distribution)::at, clipX)).setLabel(distribution.toString());
    show.add(Plot.of(PDF.of(histogram)::at, clipX)).setLabel(histogram.toString());
    return show;
  }
}
