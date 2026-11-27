// code by jph
package ch.alpine.curios.man;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophus.rsm.PoissonDiskSampling;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
public class PoissonDiskSamplingDemo implements ManipulateProvider {
  public Clip wx = Clips.positive(Quantity.of(10, "m"));
  public Clip wy = Clips.positive(Quantity.of(5, "m"));
  @FieldSlider(showValue = true)
  @FieldClip(min = "0.01", max = "0.3")
  public Scalar r = RealScalar.of(0.1);
  public Scalar k = RealScalar.of(10);

  @Override
  public JComponent getContainer() {
    Show show = new Show();
    RandomGenerator randomGenerator = ThreadLocalRandom.current();
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(wx, wy);
    try {
      Scalar rad = r.multiply(wx.length());
      Tensor points = new PoissonDiskSampling(cbb, rad, k.number().intValue()).generate(randomGenerator);
      show.add(ListPlot.of(points));
    } catch (Exception exception) {
      show.setShowLabel(exception.getMessage());
    }
    show.setCbb(cbb);
    show.setAspectRatioOne();
    return ShowGridComponent.of(List.of(show));
  }

  static void main() {
    new PoissonDiskSamplingDemo().runStandalone();
  }
}
