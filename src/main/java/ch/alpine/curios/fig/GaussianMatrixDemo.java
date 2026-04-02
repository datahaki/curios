// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.plt.MatrixPlot;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.col.ColorDataGradients;
import ch.alpine.tensor.mat.GaussianMatrix;

class GaussianMatrixDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Tensor tensor = GaussianMatrix.of(255);
    Show show = new Show();
    show.add(MatrixPlot.of(tensor, ColorDataGradients.PARULA));
    return show;
  }

  static void main() {
    new GaussianMatrixDemo().runStandalone();
  }
}
