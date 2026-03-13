// code by jph
package ch.alpine.curios.man;

import java.awt.Container;

import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.Spectrogram;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.fft.ChirpFunctions;
import ch.alpine.tensor.fft.SpectrogramArrays;

@ReflectionMarker
public class CepstrogramDemo implements ManipulateProvider {
  public ChirpFunctions chirpFunctions = ChirpFunctions.LINEAR;
  public Scalar f0 = RealScalar.of(1);
  public Scalar p1 = RealScalar.of(10000);

  @Override
  public Container getContainer() {
    Tensor signal = Subdivide.of(0, 1, 10000).maps(chirpFunctions.of(f0, p1));
    return ShowGridComponent.of( //
        Spectrogram.of(SpectrogramArrays.FOURIER.operator(), signal, RealScalar.ONE).asShow(), //
        Spectrogram.of(SpectrogramArrays.REAL1.operator(), signal, RealScalar.ONE).asShow() //
    );
  }

  static void main() {
    new CepstrogramDemo().runStandalone();
  }
}
