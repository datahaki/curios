package ch.alpine.subare.demo.net;

import ch.alpine.tensor.Tensor;

public interface Layer {
  Tensor forward(Tensor x);

  Tensor back(Tensor d);

  void update();

  Tensor error(Tensor y);

  Tensor parameters();
}
