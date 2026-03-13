// code by jph
package ch.alpine.curios.man;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.Spectrogram;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.fft.SpectrogramArray;
import ch.alpine.tensor.fft.SpectrogramArrays;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.ply.Polynomial;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.win.WindowFunctions;

/** Example from Mathematica::Spectrogram:
 * Table[Cos[ i/4 + (i/20)^2], {i, 2000}] */
@ReflectionMarker
class SpectrogramRasterDemo implements ManipulateProvider {
  public Clip clip = Clips.interval(0, 100);
  public Integer numel = 2000;
  public Tensor coeffs = Tensors.vector(0, 5, 1);
  public Scalar sampleRate = RealScalar.ONE;
  public SpectrogramArrays spectrogramArrays = SpectrogramArrays.FOURIER;
  public WindowFunctions windowFunctions = WindowFunctions.DIRICHLET;
  public ColorDataGradients cdg = ColorDataGradients.CMYK_REVERSED;

  @Override
  public Container getContainer() {
    Tensor tensor = Subdivide.increasing(clip, numel).maps(Polynomial.of(coeffs)).maps(Cos.FUNCTION);
    Show show = new Show();
    SpectrogramArray spectrogramArray = spectrogramArrays.operator().config(windowFunctions.get());
    show.add(Spectrogram.of(spectrogramArray, tensor, sampleRate, cdg));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new SpectrogramRasterDemo().runStandalone();
  }
}
