// code by jph
package ch.alpine.curios.geo;

import java.awt.Color;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowDialog;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.sophis.dv.Biinvariant;
import ch.alpine.sophis.dv.Biinvariants;
import ch.alpine.sophis.fit.KMeans;
import ch.alpine.sophus.lie.rn.RGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.red.Mean;

enum KMeansDemo {
  ;
  static void main() {
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RGroup.INSTANCE);
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 500, 2);
    KMeans kMeans = new KMeans( //
        biinvariant.distances(sequence), //
        Mean::of, //
        sequence);
    kMeans.setSeeds(5);
    kMeans.complete();
    Show show = new Show();
    for (Tensor index : kMeans.partition())
      show.add(ListPlot.of(index));
    Showable showable = show.add(ListPlot.of(kMeans.seeds()));
    ListPlot listPlot = (ListPlot) showable;
    listPlot.setPointsize(4);
    listPlot.setColor(Color.GREEN);
    show.setAspectRatio(RealScalar.ONE);
    ShowDialog.of(show);
  }
}
