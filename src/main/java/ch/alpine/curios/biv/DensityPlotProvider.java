// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.fig.DensityPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;

interface DensityPlotProvider extends ScalarBinaryOperator, ShowProvider {
  CoordinateBoundingBox cbb();

  ColorDataGradient colorDataGradient();

  @Override
  default Show getShow() {
    Show show = new Show();
    show.setPlotLabel(getClass().getSimpleName());
    show.add(DensityPlot.of( //
        this, cbb(), colorDataGradient()));
    show.setAspectRatioOne();
    return show;
  }
}
