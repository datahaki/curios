// code by jph
package ch.alpine.curios.pdf;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.c.BetaDistribution;
import ch.alpine.tensor.pdf.c.FRatioDistribution;
import ch.alpine.tensor.pdf.c.HoytDistribution;
import ch.alpine.tensor.pdf.c.NakagamiDistribution;
import ch.alpine.tensor.pdf.c.RiceDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

enum HistogramDemos implements ShowProvider {
  BETA(BetaDistribution.of(1.7, 1.3), Clips.unit()),
  FRATIO(FRatioDistribution.of(4.7, 4.3), Clips.positive(10)),
  HOYT(HoytDistribution.of(0.3, 4.3), Clips.positive(10)),
  NAKAGAMI(NakagamiDistribution.of(1.2, 3.4), Clips.positive(5)),
  RICE(RiceDistribution.of(3.5, 1.5), Clips.positive(10));

  private HistogramDemo histrogramDemo;

  HistogramDemos(Distribution distribution, Clip clipX) {
    histrogramDemo = new HistogramDemo(distribution, clipX);
  }

  @Override
  public Show getShow() {
    return histrogramDemo.getShow();
  }

  static void main() {
    NAKAGAMI.run();
  }
}
