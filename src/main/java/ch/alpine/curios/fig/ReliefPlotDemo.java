// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;
import java.util.List;

import ch.alpine.bridge.fig.Meshgrid;
import ch.alpine.bridge.fig.PlotOption;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.ReliefImage;
import ch.alpine.bridge.fig.plt.ReliefPlot;
import ch.alpine.bridge.fig.plt.VectorPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.MatrixGradient;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Round;
import ch.alpine.tensor.sca.tri.Sin;

@ReflectionMarker
class ReliefPlotDemo implements ManipulateProvider {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  public Clip ranx = Clips.absolute(4);
  public Clip rany = Clips.absolute(4);
  @FieldSelectionArray({ "20", "30", "50", "100", "200" })
  public Integer resx = 100;
  @FieldSelectionArray({ "20", "30", "50", "100", "200" })
  public Integer resy = 100;
  public ColorDataGradients cdg = ColorDataGradients.ALPINE;
  public transient Tensor vec = ReliefImage.REF.maps(Round._2);

  @Override
  public Container getContainer() {
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(ranx, rany);
    ScalarBinaryOperator sbo = (x, y) -> Sin.FUNCTION.apply(Vector2NormSquared.of(Tensors.of(x, y)));
    Tensor matrix = new Meshgrid(cbb, resx, resy).image(sbo);
    Show showR = new Show();
    showR.setPlotLabel("ReliefPlot");
    ReliefImage.REF = NORMALIZE_UNLESS_ZERO.apply(vec);
    Showable showable = ReliefPlot.of(matrix, cbb, cdg);
    showR.add(showable);
    showR.setAspectRatioOne();
    Show showV = new Show();
    showV.setPlotLabel("Matrix Gradient Array");
    Interpolation interpolation = LinearInterpolation.of(MatrixGradient.of(matrix).array());
    List<Integer> list = Dimensions.of(matrix);
    Showable showable2 = showV.add(VectorPlot.of(interpolation::get, //
        CoordinateBoundingBox.of( //
            Clips.positive(list.get(0) - 1), //
            Clips.positive(list.get(1) - 1))));
    showable2.set(PlotOption.STRICT, true);
    showV.setAspectRatioOne();
    return ShowGridComponent.of(showR, showV);
  }

  static void main() {
    new ReliefPlotDemo().runStandalone();
  }
}
