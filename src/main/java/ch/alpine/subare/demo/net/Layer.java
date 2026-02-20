// code by jph
package ch.alpine.subare.demo.net;

import java.util.function.BiFunction;

import ch.alpine.tensor.Tensor;

public interface Layer {
  public static BiFunction<Tensor, Layer, Tensor> back() {
    return (d, layer) -> layer.back(d);
  }

  public static BiFunction<Tensor, Layer, Tensor> forward() {
    return (x, layer) -> layer.forward(x);
  }

  Tensor forward(Tensor x);

  Tensor back(Tensor d);

  void update();

  Tensor error(Tensor y);

  Tensor parameters();
}
