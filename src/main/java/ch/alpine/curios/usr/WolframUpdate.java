// code by jph
package ch.alpine.curios.usr;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.fft.FourierDCT;
import ch.alpine.tensor.io.Pretty;
import ch.alpine.tensor.sca.Round;

enum WolframUpdate {
  ;
  static void main() {
    Tensor matrix = FourierDCT._2.matrix(4);
    IO.println(Pretty.of(matrix.maps(Round._3)));
  }
}
