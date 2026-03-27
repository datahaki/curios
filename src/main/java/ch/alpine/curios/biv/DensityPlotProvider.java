// code by jph
package ch.alpine.curios.biv;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.DensityPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;

// TODO CURIOS generalize subclasses
interface DensityPlotProvider extends ScalarBinaryOperator, ManipulateProvider {
  CoordinateBoundingBox cbb();

  ColorDataGradient colorDataGradient();

  @Override
  default Container getContainer() {
    Show show = new Show();
    show.setShowLabel(getClass().getSimpleName());
    show.add(DensityPlot.of( //
        this, cbb(), colorDataGradient()));
    return ShowGridComponent.of(show);
  }
}
