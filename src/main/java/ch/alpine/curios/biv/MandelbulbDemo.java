// code by jph
package ch.alpine.curios.biv;

import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.DensityPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophus.lie.so.NylanderPower;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Round;

@ReflectionMarker
public class MandelbulbDemo implements ManipulateProvider {
  private static final CoordinateBoundingBox CBB = CoordinateBoundingBox.of(Clips.absolute(1), Clips.absolute(1));
  private static final Scalar THRESHOLD = RealScalar.of(5.0);
  public Boolean snapToInt = false;
  @FieldClip(min = "1", max = "10")
  @FieldSlider(showValue = true)
  public Scalar exponent = RealScalar.of(8);
  @FieldClip(min = "1", max = "100")
  @FieldSlider(showValue = true)
  public Integer depth = 40;
  @FieldClip(min = "-1", max = "1")
  @FieldSlider(showValue = true)
  public Scalar z = RealScalar.of(0.505);
  public ColorDataGradients cdg = ColorDataGradients.CLASSIC;

  @Override
  public JComponent getJComponent() {
    Scalar exp = snapToInt ? Round.FUNCTION.apply(exponent) : exponent;
    ScalarBinaryOperator sbo = (re, im) -> {
      Tensor c = Tensors.of(re, im, z);
      Tensor x = Tensors.vector(0.0, 0.0, 0.0);
      for (int index = 0; index < depth; ++index) {
        x = NylanderPower.of(x.add(c), exp);
        if (Scalars.lessThan(THRESHOLD, Vector2NormSquared.of(x)))
          return DoubleScalar.INDETERMINATE;
      }
      return Vector2NormSquared.of(x);
    };
    Show show = new Show();
    show.add(DensityPlot.of(sbo, CBB, cdg));
    show.setAspectRatioOne();
    return ShowGridComponent.of(List.of(show));
  }

  static void main() {
    new MandelbulbDemo().run();
  }
}
