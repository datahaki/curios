// code by jph
package ch.alpine.curios.man;

import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.pdf.BinningMethods;
import ch.alpine.tensor.pdf.CDF;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ErlangDistribution;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
public class HistogramDistributionManipulate implements ManipulateProvider {
  public Scalar p1 = RealScalar.of(1.2);
  public Scalar p2 = RealScalar.of(1.3);
  public Scalar support = RealScalar.of(4);
  public Integer p3 = 10000;
  public BinningMethods bm = BinningMethods.IQR;

  @Override
  public JComponent getContainer() {
    Distribution d1 = ErlangDistribution.of(p1.number().intValue(), p2);
    Distribution d2 = HistogramDistribution.of(RandomVariate.of(d1, p3), bm);
    Show show = new Show();
    Clip clip = Clips.positive(support);
    show.add(Plot.of(PDF.of(d1)::at, clip)).setLabel("PDF " + d1.toString());
    show.add(Plot.of(PDF.of(d2)::at, clip)).setLabel("PDF " + d2.toString());
    show.add(Plot.of(CDF.of(d1)::p_lessEquals, clip)).setLabel("CDF " + d1.toString());
    show.add(Plot.of(CDF.of(d2)::p_lessEquals, clip)).setLabel("CDF " + d2.toString());
    return ShowGridComponent.of(List.of(show));
  }

  static void main() {
    new HistogramDistributionManipulate().runStandalone();
  }
}
