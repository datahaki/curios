// code by jph
package ch.alpine.curios.man;

import java.awt.Container;
import java.util.stream.Stream;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Spectrogram;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.exp.LogisticMap;

@ReflectionMarker
public class SpectrogramDemo implements ManipulateProvider {
  @FieldClip(min = "0", max = "4")
  public Scalar r = RealScalar.of(3.857);
  public Scalar seed = RealScalar.of(0.5);

  @Override
  public Container getContainer() {
    Show show = new Show();
    Tensor signal = Tensor.of(Stream.generate(LogisticMap.of(r, seed)).limit(10000));
    show.add(Spectrogram.of(signal, RealScalar.ONE));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new SpectrogramDemo().run();
  }
}
