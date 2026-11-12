// code by jph
package ch.alpine.curios.opt;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.red.Times;

enum Rosenbrock implements ScalarBinaryOperator {
  FUNCTION;

  public static final Tensor MIN = Tensors.vector(1, 1).unmodifiable();

  private static TensorUnaryOperator gradient() {
    return p -> {
      Scalar x = p.Get(0);
      Scalar y = p.Get(1);
      return Tensors.of(dx(x, y), dy(x, y));
    };
  }

  public static final TensorUnaryOperator GRADIENT = gradient();

  @Override
  public Scalar apply(Scalar x, Scalar y) {
    // (1 − x)^2 + 100(y − x^2 )^2
    Scalar tx = RealScalar.ONE.subtract(x); //
    Scalar x2 = x.multiply(x);
    Scalar ty = y.subtract(x2); //
    return tx.multiply(tx).add(Times.of(ty, ty, RealScalar.of(100)));
  }

  public static Scalar dx(Scalar x, Scalar y) {
    Scalar t1 = RealScalar.ONE.subtract(x).multiply(RealScalar.TWO);
    Scalar x2 = x.multiply(x);
    Scalar t2 = Times.of( //
        RealScalar.of(400), //
        x, //
        y.subtract(x2));
    return t1.add(t2).negate();
  }

  public static Scalar dy(Scalar x, Scalar y) {
    Scalar x2 = x.multiply(x);
    return y.subtract(x2).multiply(RealScalar.of(200));
  }
}
