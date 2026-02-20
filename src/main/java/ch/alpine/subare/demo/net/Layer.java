package ch.alpine.subare.demo.net;

import ch.alpine.tensor.Tensor;

public interface Layer {
  Tensor forward(Tensor x);

  Tensor back(Tensor d2);

  void update(Tensor d);

  Tensor error(Tensor y);
}
