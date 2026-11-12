// code by jph
package ch.alpine.curios.opt;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** Momentum Gradient Descent, or
 * Gradient descent with momentum */
record MGD(TensorUnaryOperator grad) {
  /** @param theta
   * @param beta momentum coefficient
   * @param eta
   * @param max_epochs
   * @return */
  public Tensor sequence(Tensor theta, Scalar beta, Scalar eta, int max_epochs) {
    Tensor sequence = Tensors.reserve(max_epochs + 1);
    Tensor vel = theta.maps(Scalar::zero);
    sequence.append(theta);
    for (int epoch = 0; epoch < max_epochs; ++epoch) {
      vel = vel.multiply(beta).add(grad.apply(theta));
      theta = theta.subtract(vel.multiply(eta));
      sequence.append(theta);
    }
    return sequence;
  }
}
