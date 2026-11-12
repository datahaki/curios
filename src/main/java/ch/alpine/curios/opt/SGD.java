// code by jph
package ch.alpine.curios.opt;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** The simplest optimization algorithm is gradient descent */
record SGD(TensorUnaryOperator grad) {
  public Tensor sequence(Tensor theta, Scalar eta, int max_epochs) {
    Tensor sequence = Tensors.reserve(max_epochs + 1);
    sequence.append(theta);
    for (int epoch = 0; epoch < max_epochs; ++epoch) {
      theta = theta.add(grad.apply(theta).multiply(eta.negate()));
      sequence.append(theta);
    }
    return sequence;
  }
}
