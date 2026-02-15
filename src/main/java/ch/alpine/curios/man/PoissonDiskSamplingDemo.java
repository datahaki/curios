// code by jph
package ch.alpine.curios.man;

import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophus.math.sample.PoissonDiskSampling;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
public class PoissonDiskSamplingDemo implements ManipulateProvider {
  public Scalar wx = RealScalar.of(10);
  public Scalar wy = RealScalar.of(10);
  public Scalar r = RealScalar.of(0.1);
  public Scalar k = RealScalar.of(30);

  @Override
  public JComponent getContainer() {
    Show show = new Show();
    Clip clipx = Clips.positive(wx);
    Clip clipy = Clips.positive(wy);
    List<Tensor> list2 = new PoissonDiskSampling(wx, wy, r, k.number().intValue()).generate();
    Tensor pnts = Tensor.of(list2.stream());
    show.add(ListPlot.of(pnts));
    show.setCbb(CoordinateBoundingBox.of(clipx, clipy));
    show.setAspectRatioOne();
    return ShowGridComponent.of(List.of(show));
  }

  static void main() {
    new PoissonDiskSamplingDemo().run();
  }
}
