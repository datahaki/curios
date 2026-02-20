package ch.alpine.subare.demo.net;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.ArgMax;
import ch.alpine.tensor.num.SoftmaxLayer;

public class SoftArgMax implements Layer {
  Tensor x;
  Tensor d;

  @Override
  public Scalar forward(Tensor x) {
    this.x = x;
    return RealScalar.of(ArgMax.of(SoftmaxLayer.of(x)));
  }

  @Override
  public Tensor back(Tensor d2) {
    return d2;
  }

  @Override
  public void update(Tensor d) {
  }

  @Override
  public Tensor error(Tensor y) {
    int k = Scalars.intValueExact((Scalar) y);
    return UnitVector.of(x.length(), k).subtract(SoftmaxLayer.of(x)); // one-hot target
  }
}
