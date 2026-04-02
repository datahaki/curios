// code by jph
package ch.alpine.curios.biv;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.DensityPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.col.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;

@ReflectionMarker
abstract class DensityPlotProvider implements ScalarBinaryOperator, ManipulateProvider {
  abstract CoordinateBoundingBox cbb();

  public ColorDataGradients cdg = ColorDataGradients.ALPINE;

  @Override
  public Container getContainer() {
    Show show = new Show();
    show.setShowLabel(getClass().getSimpleName());
    show.add(DensityPlot.of( //
        this, cbb(), cdg));
    return ShowGridComponent.of(show);
  }
}
