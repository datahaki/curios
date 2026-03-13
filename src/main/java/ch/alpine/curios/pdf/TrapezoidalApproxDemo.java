// code by jph
package ch.alpine.curios.pdf;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TrapezoidalDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.pow.Sqrt;

@ReflectionMarker
class TrapezoidalApproxDemo implements ManipulateProvider {
  public Scalar sigma = RealScalar.of(1);
  @FieldSlider(showValue = true)
  @FieldClip(min = "3.01", max = "6")
  public transient Scalar spread = RealScalar.of(5);

  @Override
  public Container getContainer() {
    Distribution tr = TrapezoidalDistribution.with(RealScalar.ZERO, sigma, Sqrt.FUNCTION.apply(spread));
    Clip clip = Clips.absolute(3);
    Show show = new Show();
    show.add(Plot.of(PDF.of(NormalDistribution.of(RealScalar.ZERO, sigma))::at, clip));
    show.add(Plot.of(PDF.of(tr)::at, clip));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new TrapezoidalApproxDemo().runStandalone();
  }
}
