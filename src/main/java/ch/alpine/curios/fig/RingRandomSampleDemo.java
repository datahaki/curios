// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.sophus.rsm.RingRandomSample;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomSample;

class RingRandomSampleDemo implements ManipulateProvider {
  @FieldClip(min = "0", max = "1")
  @FieldSlider
  public Scalar r1 = RealScalar.of(1);
  @FieldClip(min = "1", max = "2")
  @FieldSlider
  public Scalar r2 = RealScalar.of(2);
  public Integer samples = 5000;

  @Override
  public Container getContainer() {
    RingRandomSample randomSampleInterface = new RingRandomSample(2, r1, r2);
    Tensor matrix = RandomSample.of(randomSampleInterface, samples);
    Show show = new Show();
    show.add(ListPlot.of(matrix));
    show.setAspectRatioOne();
    return ShowGridComponent.of(show);
  }

  static void main() {
    new RingRandomSampleDemo().runStandalone();
  }
}
