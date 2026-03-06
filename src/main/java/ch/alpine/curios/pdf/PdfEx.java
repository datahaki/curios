// code by jph
package ch.alpine.curios.pdf;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.c.ArcSinDistribution;
import ch.alpine.tensor.pdf.c.BetaDistribution;
import ch.alpine.tensor.pdf.c.FRatioDistribution;
import ch.alpine.tensor.pdf.c.FisherZDistribution;
import ch.alpine.tensor.pdf.c.GammaDistribution;
import ch.alpine.tensor.pdf.c.HoytDistribution;
import ch.alpine.tensor.pdf.c.MaxwellDistribution;
import ch.alpine.tensor.pdf.c.NakagamiDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.RiceDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
enum PdfEx {
  ARCSIN(ArcSinDistribution.INSTANCE, Clips.absoluteOne()),
  BETA(BetaDistribution.of(1.7, 1.3), Clips.unit()),
  FISHERZ(FisherZDistribution.of(2.7, 1.3), Clips.absolute(10)),
  FRATIO(FRatioDistribution.of(4.7, 4.3), Clips.positive(10)),
  GAMMA(GammaDistribution.of(1.1, 1), Clips.positive(10)),
  HOYT(HoytDistribution.of(0.3, 4.3), Clips.positive(10)),
  MAXWELL1(MaxwellDistribution.of(0.3), Clips.positive(10)),
  MAXWELL2(MaxwellDistribution.of(1.3), Clips.positive(10)),
  NAKAGAMI(NakagamiDistribution.of(1.2, 3.4), Clips.positive(5)),
  NORMAL(NormalDistribution.standard(), Clips.absolute(4)),
  RICE(RiceDistribution.of(3.5, 1.5), Clips.positive(10));

  public final Distribution distribution;
  public final Clip clipX;

  PdfEx(Distribution distribution, Clip clipX) {
    this.distribution = distribution;
    this.clipX = clipX;
  }
}
