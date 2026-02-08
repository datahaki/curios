// code by jph
package ch.alpine.curios.biv;

import ch.alpine.bridge.fig.ArrayPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;

interface BivariateEvaluation extends ScalarBinaryOperator, ShowProvider {
  Clip clipX();

  Clip clipY();

  ColorDataGradient colorDataGradient();

  @Override
  default Show getShow() {
    Show show = new Show();
    show.setPlotLabel(getClass().getSimpleName());
    // TODO should not flip y axis!
    show.add(ArrayPlot.of( //
        StaticHelper.image(this), //
        CoordinateBoundingBox.of(clipX(), clipY()), //
        colorDataGradient()));
    show.setAspectRatio(RealScalar.ONE);
    return show;
  }
}
