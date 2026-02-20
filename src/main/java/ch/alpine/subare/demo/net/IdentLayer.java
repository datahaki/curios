package ch.alpine.subare.demo.net;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.exp.DLogisticSigmoid;

public class IdentLayer implements Layer {
  Tensor x;

  @Override
  public Tensor forward(Tensor x) {
    this.x = x;
    return x;
  }

  @Override
  public Tensor back(Tensor d2) {
    return Entrywise.mul().apply(d2, x.maps(DLogisticSigmoid.NESTED));
  }

  @Override
  public void update(Tensor d) {
    // TODO Auto-generated method stub
  }

  @Override
  public Tensor error(Tensor y) {
    return y.subtract(x);
  }
}
