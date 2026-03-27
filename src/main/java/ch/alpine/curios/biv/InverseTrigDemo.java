// code by jph
package ch.alpine.curios.biv;

import java.util.Arrays;

import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Im;
import ch.alpine.tensor.sca.pow.Power;
import ch.alpine.tensor.sca.tri.ArcCosh;
import ch.alpine.tensor.sca.tri.ArcSinh;
import ch.alpine.tensor.sca.tri.ArcTanh;

/** inspired by Mathematica's documentation of DensityPlot */
@ReflectionMarker
record InverseTrigDemo(ScalarUnaryOperator... scalarUnaryOperators) implements DensityPlotProvider {
  private static final int EXPONENT = 3;
  public static final DensityPlotProvider INSTANCE = new InverseTrigDemo(ArcSinh.FUNCTION, ArcCosh.FUNCTION, ArcTanh.FUNCTION);

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar seed = Power.of(ComplexScalar.of(re, im), EXPONENT);
    return Arrays.stream(scalarUnaryOperators) //
        .map(scalarUnaryOperator -> scalarUnaryOperator.apply(seed)) //
        .map(Im.FUNCTION) //
        .reduce(Scalar::add) //
        .get();
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.absolute(2.0), Clips.absolute(2.0));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.THERMOMETER;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
