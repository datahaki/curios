// code by jph
package ch.alpine.curios.opt;

import java.awt.Color;
import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.DensityPlot;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.fig.plt.VectorPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class RosenbrockDemo implements ManipulateProvider {
  public Tensor start = Tensors.vector(-2, 1);
  public Scalar tolerance = RealScalar.of(1E-8);
  public Integer max_epochs = 2000;

  @ReflectionMarker
  public static class ParamGen {
    public Boolean show = false;
  }

  @ReflectionMarker
  public static class ParamSGD extends ParamGen {
    public Scalar eta = RealScalar.of(0.001);
  }

  public final ParamSGD paramSGD = new ParamSGD();

  @ReflectionMarker
  public static class ParamMGD extends ParamGen {
    @FieldSelectionArray({ "0.5", "0.9", "0.99" })
    public Scalar beta = RealScalar.of(0.9);
    public Scalar eta = RealScalar.of(0.0001);
  }

  public final ParamMGD paramMGD = new ParamMGD();

  @ReflectionMarker
  public static class ParamAdam extends ParamGen {
    @FieldSelectionArray({ "0.5", "0.9", "0.99" })
    public Scalar beta1 = RealScalar.of(0.9);
    @FieldSelectionArray({ "0.5", "0.9", "0.999" })
    public Scalar beta2 = RealScalar.of(0.999);
    public Scalar eta = RealScalar.of(0.02);
  }

  public final ParamAdam paramAdam = new ParamAdam();

  public GradientDescent createGradientDescent() {
    return new GradientDescent(Rosenbrock.GRADIENT, start.unmodifiable(), tolerance);
  }

  @Override
  public Container getContainer() {
    Show show = new Show();
    show.setShowLabel(getClass().getSimpleName());
    {
      CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.absolute(2), Clips.interval(-1, 3));
      show.add(DensityPlot.of(Rosenbrock.FUNCTION, cbb));
      show.add(VectorPlot.of(Rosenbrock.GRADIENT, cbb));
    }
    show.add(ListPlot.of(Tensors.of(Rosenbrock.MIN))).setColor(Color.RED);
    GradientDescent gradDec = createGradientDescent();
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.absolute(25), Clips.absolute(25));
    StringBuilder stringBuilder = new StringBuilder();
    if (paramSGD.show)
      try {
        Tensor sequence = gradDec.simple(paramSGD.eta, max_epochs);
        if (sequence.stream().allMatch(cbb)) {
          Showable showable = show.add(ListLinePlot.of(sequence));
          showable.setLabel("SGD");
        } else
          stringBuilder.append("(SGD: diverge) ");
      } catch (Exception e) {
        stringBuilder.append("(SGD: fail) ");
      }
    if (paramMGD.show)
      try {
        Tensor sequence = gradDec.moment(paramMGD.beta, paramMGD.eta, max_epochs);
        if (sequence.stream().allMatch(cbb)) {
          Showable showable = show.add(ListLinePlot.of(sequence));
          showable.setLabel("MGD");
        } else
          stringBuilder.append("(MGD: diverge) ");
      } catch (Exception e) {
        stringBuilder.append("(MGD: fail) ");
      }
    if (paramAdam.show)
      try {
        Tensor sequence = gradDec.adam(paramAdam.beta1, paramAdam.beta2, paramAdam.eta, max_epochs);
        if (sequence.stream().allMatch(cbb)) {
          Showable showable = show.add(ListLinePlot.of(sequence));
          showable.setLabel("Adam");
        } else
          stringBuilder.append("(Adam: diverge) ");
      } catch (Exception e) {
        stringBuilder.append("(Adam: fail) ");
      }
    show.setShowLabel(stringBuilder.toString());
    return ShowGridComponent.of(show);
  }

  static void main() {
    new RosenbrockDemo().runStandalone();
  }
}
