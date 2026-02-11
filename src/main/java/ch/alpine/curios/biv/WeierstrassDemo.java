// code by jph
package ch.alpine.curios.biv;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.pow.Power;
import ch.alpine.tensor.sca.tri.Cos;

/* package */ record WeierstrassDemo(int depth) implements BivariateEvaluation {
  @Override
  public Scalar apply(Scalar re, Scalar im) {
    Scalar s = DoubleScalar.of(0.0);
    // b = 7.0 has to be a positive odd integer
    for (int n = 0; n < depth; ++n)
      s = s.add(Power.of(im, n).multiply(Cos.FUNCTION.apply(Power.of(7.0, n).multiply(DoubleScalar.of(Math.PI)).multiply(re))));
    return s;
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.interval(0.25, 1.0), Clips.interval(0.25, 1.0));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.ALPINE;
  }

  static void main() {
    new WeierstrassDemo(20).run();
  }
}
