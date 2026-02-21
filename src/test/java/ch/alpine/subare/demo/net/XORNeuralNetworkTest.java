// code by jph
package ch.alpine.subare.demo.net;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import ch.alpine.subare.demo.net.XORNeuralNetwork.Network;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;

class XORNeuralNetworkTest {
  @Test
  void test() {
    Tensor y = XORNeuralNetwork.XOR;
    for (int attempt = 0; attempt < 3; ++attempt) {
      XORNeuralNetwork xorNeuralNetwork = new XORNeuralNetwork();
      Network xorNet = xorNeuralNetwork.new Network();
      xorNet.train(y);
      Scalar error = xorNet.evaluate(y);
      if (Scalars.lessThan(error, RealScalar.of(0.1)))
        return;
    }
    fail();
  }
}
