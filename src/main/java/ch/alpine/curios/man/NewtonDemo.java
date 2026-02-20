// code by jph
package ch.alpine.curios.man;

import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.DensityPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Arg;
import ch.alpine.tensor.sca.Clips;

/** inspired by Mathematica's documentation of Gamma */
@ReflectionMarker
public class NewtonDemo implements ManipulateProvider {
  @FieldClip(min = "1", max = "5")
  public Integer depth = 2;
  public Tensor coeffs = Tensors.vector(1, 5, 0, 1);
  public ColorDataGradients cdg = ColorDataGradients.PARULA;

  @Override
  public JComponent getContainer() {
    Show show = new Show();
    ScalarUnaryOperator scalarUnaryOperator = NewtonScalarMethod.polynomial(coeffs).iteration;
    ScalarBinaryOperator sbo = (re, im) -> Arg.FUNCTION.apply(Nest.of(scalarUnaryOperator, ComplexScalar.of(re, im), depth));
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.absolute(2.0), Clips.absolute(2.0));
    show.add(DensityPlot.of(sbo, cbb, cdg));
    return ShowGridComponent.of(List.of(show));
  }

  static void main() {
    new NewtonDemo().runStandalone();
  }
}
