// code by jph
package ch.alpine.curios.pdf;

import java.util.LinkedList;
import java.util.List;

import ch.alpine.bridge.fig.Manipulate;
import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.swing.LookAndFeels;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.pdf.BinningMethods;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ErlangDistribution;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

public class TwoParam {
  public Scalar p1 = RealScalar.of(1.2);
  public Scalar p2 = RealScalar.of(1.3);
  public Scalar support = RealScalar.of(4);
  public Integer p3 = 10000;
  public BinningMethods bm = BinningMethods.IQR;

  public List<Show> normal() {
    List<Show> list = new LinkedList<>();
    {
      Distribution d1 = ErlangDistribution.of(p1.number().intValue(), p2);
      Distribution d2 = HistogramDistribution.of(RandomVariate.of(d1, p3), bm);
      Show show = new Show();
      Clip clip = Clips.absolute(support);
      show.add(Plot.of(PDF.of(d1)::at, clip)).setLabel(d1.toString());
      show.add(Plot.of(PDF.of(d2)::at, clip)).setLabel("hist");
      list.add(show);
    }
    return list;
  }

  static void main() {
    LookAndFeels.LIGHT.updateComponentTreeUI();
    TwoParam twoParam = new TwoParam();
    Manipulate.of(twoParam, twoParam::normal);
  }
}
