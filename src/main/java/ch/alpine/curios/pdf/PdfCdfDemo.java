// code by jph
package ch.alpine.curios.pdf;

import java.awt.Container;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.PlotOption;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.pdf.BinningMethods;
import ch.alpine.tensor.pdf.CDF;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.sca.Clip;

@ReflectionMarker
class PdfCdfDemo implements ManipulateProvider {
  public PdfEx pdfEx = PdfEx.BETA;
  @FieldSelectionArray({ "1000", "3000", "10000" })
  public Integer samples = 1000;
  public BinningMethods binning = BinningMethods.SQRT;

  @Override
  public Container getContainer() {
    Distribution distribution = pdfEx.distribution;
    Clip clipX = pdfEx.clipX;
    Distribution histogram = HistogramDistribution.of(RandomVariate.of(distribution, samples), binning);
    Show showPdf = new Show();
    showPdf.add(Plot.of(PDF.of(histogram)::at, clipX)).setLabel(histogram.toString());
    showPdf.add(Plot.of(PDF.of(distribution)::at, clipX, PlotOption.STRICT)).setLabel(distribution.toString());
    Show showCdf = new Show();
    showCdf.add(Plot.of(CDF.of(histogram)::p_lessThan, clipX)).setLabel("CDF " + histogram);
    if (distribution instanceof CDF)
      showCdf.add(Plot.of(CDF.of(distribution)::p_lessThan, clipX, PlotOption.STRICT)).setLabel("CDF " + distribution);
    return ShowGridComponent.of(showPdf, showCdf);
  }

  static void main() {
    new PdfCdfDemo().runStandalone();
  }
}
