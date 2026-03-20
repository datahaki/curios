// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.MatrixPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.MatrixGradient;
import ch.alpine.tensor.mat.GaussianMatrix;

@ReflectionMarker
class MatrixGradientDemo implements ManipulateProvider {
  @Override
  public Container getContainer() {
    Tensor matrix = GaussianMatrix.of(25);
    MatrixGradient grad = MatrixGradient.of(matrix);
    Show showX = new Show();
    showX.add(MatrixPlot.of(grad.dx(), ColorDataGradients.PARULA));
    Show showY = new Show();
    showY.add(MatrixPlot.of(grad.dy(), ColorDataGradients.PARULA));
    return ShowGridComponent.of(showX, showY);
  }

  static void main() {
    new MatrixGradientDemo().runStandalone();
  }
}
