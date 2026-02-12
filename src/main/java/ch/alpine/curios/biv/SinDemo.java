// code by jph
// https://mathematica.stackexchange.com/questions/9167/adapt-colorfunction-in-array-plot
package ch.alpine.curios.biv;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Re;
import ch.alpine.tensor.sca.tri.ArcTan;
import ch.alpine.tensor.sca.tri.Sin;

/** inspired by mathematica's documentation of Gamma */
/* package */ record SinDemo(int depth) implements DensityPlotProvider {
  public static final DensityPlotProvider INSTANCE = new SinDemo(3);

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar seed = ComplexScalar.of(re, im);
    return Re.FUNCTION.apply(ArcTan.FUNCTION.apply(Nest.of(Sin.FUNCTION, seed, depth)));
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.absolute(Pi.VALUE), Clips.absolute(Pi.VALUE));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.SUNSET;
  }

  static void main() {
    INSTANCE.run();
  }
}
