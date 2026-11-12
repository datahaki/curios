package ch.alpine.curios.opt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;

class RosenbrockDemoTest {
  @Test
  void test() {
    RosenbrockDemo rosenbrockDemo = new RosenbrockDemo();
    GradientDescent gradientDescent = rosenbrockDemo.createGradientDescent();
    Tensor sequence = gradientDescent.simple(rosenbrockDemo.paramSGD.eta, rosenbrockDemo.max_epochs);
    assertEquals(sequence.length(), rosenbrockDemo.max_epochs);
  }
}
