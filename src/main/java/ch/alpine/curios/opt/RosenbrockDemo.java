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
  public Scalar eta = RealScalar.of(0.0003);
  @FieldSelectionArray({ "0.5", "0.9", "0.99" })
  public Scalar beta = RealScalar.of(0.9);
  public Integer max_epochs = 2000;

  @Override
  public Container getContainer() {
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.absolute(2), Clips.interval(-1, 3));
    Show show = new Show();
    show.setShowLabel(getClass().getSimpleName());
    show.add(DensityPlot.of(Rosenbrock.FUNCTION, cbb));
    show.add(VectorPlot.of(Rosenbrock.GRADIENT, cbb));
    show.add(ListPlot.of(Tensors.of(Rosenbrock.MIN))).setColor(Color.RED);
    {
      SGD sgd = new SGD(Rosenbrock.GRADIENT);
      Tensor sequence = sgd.sequence(start, eta, max_epochs);
      Showable showable = show.add(ListLinePlot.of(sequence));
      showable.setLabel("SGD");
    }
    {
      MGD sgd = new MGD(Rosenbrock.GRADIENT);
      Tensor sequence = sgd.sequence(start, beta, eta, max_epochs);
      Showable showable = show.add(ListLinePlot.of(sequence));
      showable.setLabel("MGD");
    }
    return ShowGridComponent.of(show);
  }

  static void main() {
    new RosenbrockDemo().runStandalone();
  }
}
