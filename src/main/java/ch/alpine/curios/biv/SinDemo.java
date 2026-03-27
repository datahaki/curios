// code by jph
// https://mathematica.stackexchange.com/questions/9167/adapt-colorfunction-in-array-plot
package ch.alpine.curios.biv;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Re;
import ch.alpine.tensor.sca.tri.ArcTan;
import ch.alpine.tensor.sca.tri.Sin;

/** inspired by mathematica's documentation of Gamma */
@ReflectionMarker
class SinDemo extends DensityPlotProvider {
  public Integer depth = 3;

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar seed = ComplexScalar.of(re, im);
    return Re.FUNCTION.apply(ArcTan.FUNCTION.apply(Nest.of(Sin.FUNCTION, seed, depth)));
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.absolute(Pi.VALUE), Clips.absolute(Pi.VALUE));
  }

  static void main() {
    new SinDemo().runStandalone();
  }
}
