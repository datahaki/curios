// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.DensityPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.sophis.noise.PerlinContinuousNoise;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.LinearColorDataGradient;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

class DuneNoiseDemo implements ShowProvider {
  public static Scalar binOp(Scalar x, Scalar y) {
    double dx = x.number().doubleValue();
    double dy = y.number().doubleValue();
    double r1 = PerlinContinuousNoise.FUNCTION.at(dx, dy);
    Scalar a1 = DoubleScalar.of(r1);
    double r2 = PerlinContinuousNoise.FUNCTION.at(10 + dx * 5, dy * 5);
    Scalar a2 = DoubleScalar.of(0.5 + r2 * 0.4);
    return a1.multiply(a2);
  }

  public static DensityPlot densityPlot() {
    int w = 2;
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.absolute(w), Clips.absolute(w));
    Show show = new Show();
    show.setPlotLabel("SimplexContinuousNoise[x,y]");
    int co1 = 192 + 32;
    Tensor colors = Tensors.of( //
        Tensors.vector(co1, co1, co1, 255), //
        Tensors.vector(255, 255, 255, 255));
    ColorDataGradient colorDataGradient = LinearColorDataGradient.of(colors);
    return DensityPlot.of(DuneNoiseDemo::binOp, cbb, colorDataGradient);
  }

  @Override
  public Show getShow() {
    Show show = new Show();
    DensityPlot densityPlot = densityPlot();
    densityPlot.setPlotPoints(200);
    show.add(densityPlot);
    return show;
  }

  static void main() {
    new DuneNoiseDemo().runStandalone();
  }
}
