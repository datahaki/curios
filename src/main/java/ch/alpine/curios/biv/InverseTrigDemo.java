// code by jph
package ch.alpine.curios.biv;

import java.util.stream.Stream;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Im;
import ch.alpine.tensor.sca.pow.Power;
import ch.alpine.tensor.sca.tri.ArcCosh;
import ch.alpine.tensor.sca.tri.ArcSinh;
import ch.alpine.tensor.sca.tri.ArcTanh;

/** inspired by Mathematica's documentation of DensityPlot */
/* package */ record InverseTrigDemo(ScalarUnaryOperator... scalarUnaryOperators) implements BivariateEvaluation {
  private static final int EXPONENT = 3;

  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar seed = Power.of(ComplexScalar.of(re, im), EXPONENT);
    return Stream.of(scalarUnaryOperators) //
        .map(scalarUnaryOperator -> scalarUnaryOperator.apply(seed)) //
        .map(Im.FUNCTION) //
        .reduce(Scalar::add) //
        .get();
  }

  @Override
  public Clip clipX() {
    return Clips.absolute(2.0);
  }

  @Override
  public Clip clipY() {
    return Clips.absolute(2.0);
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.THERMOMETER;
  }

  static void main() {
    BivariateEvaluation bivariateEvaluation = new InverseTrigDemo(ArcSinh.FUNCTION, ArcCosh.FUNCTION, ArcTanh.FUNCTION);
    bivariateEvaluation.getShow();
  }
}
