// code by jph
package ch.alpine.curios.opt;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.itp.LinearBinaryAverage;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.pow.Sqrt;

public record GradientDescent(TensorUnaryOperator gradient, Tensor start, Scalar tolerance) {
  /** The simplest optimization algorithm is gradient descent
   * 
   * @param eta
   * @param max_epochs
   * @return */
  public Tensor simple(Scalar eta, int max_epochs) {
    Tensor theta = start;
    Tensor sequence = Tensors.reserve(max_epochs + 1);
    sequence.append(theta);
    for (int epoch = 1; epoch < max_epochs; ++epoch) {
      Tensor grad = gradient.apply(theta);
      theta = theta.add(grad.multiply(eta.negate()));
      sequence.append(theta);
      if (Scalars.lessThan(Vector2Norm.of(grad), tolerance))
        break;
    }
    return sequence;
  }

  /** Momentum Gradient Descent, or
   * Gradient descent with momentum
   * 
   * @param beta momentum coefficient in unit interval
   * @param eta
   * @param max_epochs
   * @return */
  public Tensor moment(Scalar beta, Scalar eta, int max_epochs) {
    Tensor theta = start;
    Tensor sequence = Tensors.reserve(max_epochs + 1);
    sequence.append(theta);
    Tensor vel = theta.maps(Scalar::zero);
    for (int epoch = 1; epoch < max_epochs; ++epoch) {
      Tensor grad = gradient.apply(theta);
      vel = vel.multiply(beta).add(grad);
      theta = theta.subtract(vel.multiply(eta));
      sequence.append(theta);
      if (Scalars.lessThan(Vector2Norm.of(grad), tolerance))
        break;
    }
    return sequence;
  }

  /** @param beta1
   * @param beta2
   * @param eta
   * @param max_epochs
   * @return */
  public Tensor adam(Scalar beta1, Scalar beta2, Scalar eta, int max_epochs) {
    Scalar eps = RealScalar.of(1E-8);
    Tensor theta = start;
    Tensor sequence = Tensors.reserve(max_epochs + 1);
    sequence.append(theta);
    Tensor m = theta.maps(Scalar::zero);
    Tensor v = theta.maps(Scalar::zero);
    Scalar beta1_t = RealScalar.ONE;
    Scalar beta2_t = RealScalar.ONE;
    for (int epoch = 1; epoch < max_epochs; ++epoch) {
      Tensor grad = gradient.apply(theta);
      {
        // Biased first moment estimate (momentum)
        m = LinearBinaryAverage.INSTANCE.split(grad, m, beta1);
      }
      {
        // Biased second raw moment estimate (uncentered variance)
        Tensor g2 = Entrywise.mul().apply(grad, grad);
        v = LinearBinaryAverage.INSTANCE.split(g2, v, beta2);
      }
      // Bias-corrected moment estimates:
      beta1_t = beta1_t.multiply(beta1);
      Tensor m_h = m.divide(RealScalar.ONE.subtract(beta1_t));
      beta2_t = beta2_t.multiply(beta2);
      Tensor v_h = v.divide(RealScalar.ONE.subtract(beta2_t));
      Tensor den = v_h.maps(s -> Sqrt.FUNCTION.apply(s).add(eps));
      theta = theta.subtract(Entrywise.div().apply(m_h, den).multiply(eta));
      sequence.append(theta);
      if (Scalars.lessThan(Vector2Norm.of(grad), tolerance))
        break;
    }
    return sequence;
  }
}
