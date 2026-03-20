// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;
import java.util.List;

import ch.alpine.bridge.fig.PlotOption;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.MatrixPlot;
import ch.alpine.bridge.fig.plt.VectorPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.MatrixGradient;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.mat.GaussianMatrix;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class MatrixGradientDemo implements ManipulateProvider {
  public Boolean rescale = true;
  public ColorDataGradients cdg = ColorDataGradients.PARULA;

  @Override
  public Container getContainer() {
    Tensor matrix = GaussianMatrix.of(25);
    MatrixGradient grad = MatrixGradient.of(matrix);
    Clip clip = grad.range();
    if (rescale)
      grad = grad.rescale();
    Show showX = new Show();
    showX.setPlotLabel(clip.toString());
    showX.add(MatrixPlot.of(grad.dx(), cdg));
    Show showY = new Show();
    showY.setPlotLabel(grad.range().toString());
    showY.add(MatrixPlot.of(grad.dy(), cdg));
    Show showV = new Show();
    {
      showV.setPlotLabel("Matrix Gradient Array");
      Interpolation interpolation = LinearInterpolation.of(MatrixGradient.of(matrix).array());
      List<Integer> list = Dimensions.of(matrix);
      Showable showable = showV.add(VectorPlot.of(interpolation::get, //
          CoordinateBoundingBox.of( //
              Clips.positive(list.get(0) - 1), //
              Clips.positive(list.get(1) - 1))));
      showable.set(PlotOption.STRICT, true);
      showV.setAspectRatioOne();
    }
    Show showW = new Show();
    {
      showW.setPlotLabel("Matrix Gradient Cross");
      Interpolation interpolation = LinearInterpolation.of(MatrixGradient.of(matrix).cross());
      List<Integer> list = Dimensions.of(matrix);
      Showable showable = showW.add(VectorPlot.of(interpolation::get, //
          CoordinateBoundingBox.of( //
              Clips.positive(list.get(0) - 1), //
              Clips.positive(list.get(1) - 1))));
      showable.set(PlotOption.STRICT, true);
      showW.setAspectRatioOne();
    }
    return ShowGridComponent.of(showX, showY, showV, showW);
  }

  static void main() {
    new MatrixGradientDemo().runStandalone();
  }
}
