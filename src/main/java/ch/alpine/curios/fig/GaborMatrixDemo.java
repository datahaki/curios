// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.MatrixPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.GaborMatrix;

class GaborMatrixDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Tensor tensor = GaborMatrix.of(255, Tensors.vector(0.1, 0.2), RealScalar.of(0.3));
    Show show = new Show();
    show.add(MatrixPlot.of(tensor));
    return show;
  }

  static void main() {
    new GaborMatrixDemo().runStandalone();
  }
}
