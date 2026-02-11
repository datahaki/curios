// code by jph
package ch.alpine.curios.biv;

import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Outer;
import ch.alpine.tensor.ext.ArgMin;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.red.Min;
import ch.alpine.tensor.sca.Clips;

/** used as logo of edelweis */
public class Se2onR2Demo implements BivariateEvaluation {
  private final Tensor actions = Tensors.of( //
      Tensors.vector(+0.1, +0.2, +0.3), //
      Tensors.vector(-0.3, +0.2, -0.5), //
      Tensors.vector(-0.2, -0.4, -1.0), //
      Tensors.vector(+0.2, -0.7, -1.5)).unmodifiable();

  private static Tensor action(Tensor xya, Tensor uv) {
    return Se2Group.INSTANCE.combine(xya, uv.copy().append(RealScalar.ONE)).extract(0, 2);
  }

  @Override
  public Scalar apply(Scalar x, Scalar y) {
    Tensor seed = Tensors.of(Tensors.of(x, y));
    for (int count = 0; count < 4; ++count)
      seed = Flatten.of(Outer.of(Se2onR2Demo::action, actions, seed), 1);
    Tensor tensor = Tensor.of(seed.stream().map(Vector2Norm::of));
    Scalar dist = (Scalar) tensor.stream().reduce(Min::of).get();
    return RealScalar.of(ArgMin.of(tensor) * 0.01).add(dist);
  }

  @Override
  public CoordinateBoundingBox cbb() {
    return CoordinateBoundingBox.of(Clips.absolute(2), Clips.absolute(2));
  }

  @Override
  public ColorDataGradient colorDataGradient() {
    return ColorDataGradients.CMYK_REVERSED;
  }

  static void main() {
    new Se2onR2Demo().run();
  }
}
