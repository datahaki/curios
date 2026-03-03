// code by jph
package ch.alpine.curios.img;

import java.awt.Container;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Spectrogram;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.fft.Fourier;
import ch.alpine.tensor.fft.SpectrogramArray;
import ch.alpine.tensor.fft.SpectrogramArrays;
import ch.alpine.tensor.img.Raster;
import ch.alpine.tensor.sca.ply.Polynomial;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.win.WindowFunctions;

/** Example from Mathematica::Spectrogram:
 * Table[Cos[ i/4 + (i/20)^2], {i, 2000}] */
@ReflectionMarker
class SpectrogramWindowDemo implements ManipulateProvider {
  public static Tensor vector(Tensor vector, ScalarUnaryOperator window, Function<Scalar, ? extends Tensor> function) {
    return Raster.of(SpectrogramArray.of(Fourier.FORWARD::transform).config(window).half_abs(vector), function);
  }

  @Override
  public Container getContainer() {
    Tensor signal = Subdivide.of(0, 100, 2000).maps(Polynomial.of(Tensors.vector(0, 5, 1))).maps(Cos.FUNCTION);
    List<Show> list = new LinkedList<>();
    for (WindowFunctions windowFunctions : WindowFunctions.values()) {
      Show show = new Show();
      show.add(Spectrogram.of(SpectrogramArrays.FOURIER.operator().config(windowFunctions.get()), signal, RealScalar.ONE)).setLabel(windowFunctions.name());
      list.add(show);
    }
    return ShowGridComponent.column(list, new Dimension(400, 300));
  }

  static void main() {
    new SpectrogramWindowDemo().runStandalone();
  }
}
