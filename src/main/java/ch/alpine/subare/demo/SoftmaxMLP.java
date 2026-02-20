// code adapted from chatgpt
package ch.alpine.subare.demo;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.ArgMax;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.num.SoftmaxLayer;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.Ramp;
import ch.alpine.tensor.sca.Round;
import ch.alpine.tensor.sca.UnitStep;

public class SoftmaxMLP {
  static final Distribution DISTRIBUTION = NormalDistribution.of(0.0, 0.1);
  final int INPUT_SIZE = 2;
  final int HIDDEN_SIZE = 8;
  final int OUTPUT_SIZE = 3;
  Tensor W1 = RandomVariate.of(DISTRIBUTION, INPUT_SIZE, HIDDEN_SIZE);
  Tensor b1 = RandomVariate.of(DISTRIBUTION, HIDDEN_SIZE).maps(Scalar::zero);
  Tensor W2 = RandomVariate.of(DISTRIBUTION, HIDDEN_SIZE, OUTPUT_SIZE);
  Tensor b2 = RandomVariate.of(DISTRIBUTION, OUTPUT_SIZE).maps(Scalar::zero);
  Scalar learningRate = RealScalar.of(0.05);

  void train(Tensor X, Tensor y, int epochs) {
    for (int epoch = 0; epoch < epochs; epoch++) {
      for (int n = 0; n < X.length(); n++) {
        // Forward pass
        Tensor x0 = X.get(n);
        Tensor x1 = x0.dot(W1).add(b1).maps(Ramp.FUNCTION);
        Tensor x2 = x1.dot(W2).add(b2);
        Tensor probs = SoftmaxLayer.of(x2);
        // One-hot target
        Tensor target = UnitVector.of(OUTPUT_SIZE, Scalars.intValueExact(y.Get(n)));
        // Backpropagation (Cross-Entropy + Softmax simplifies gradient)
        Tensor dOutput = probs.subtract(target);
        Tensor dHidden = Entrywise.mul().apply(W2.dot(dOutput), x1.maps(UnitStep.FUNCTION));
        // Update W2 and b2
        W2 = W2.subtract(TensorProduct.of(x1, dOutput.multiply(learningRate)));
        b2 = b2.subtract(dOutput.multiply(learningRate));
        // Update W1 and b1
        W1 = W1.subtract(TensorProduct.of(x0, dHidden.multiply(learningRate)));
        b1 = b1.subtract(dHidden.multiply(learningRate));
      }
    }
  }

  void evaluate(Tensor X, Tensor y) {
    System.out.println("Evaluation:");
    for (int n = 0; n < X.length(); n++) {
      Tensor x0 = X.get(n);
      Tensor x1 = x0.dot(W1).add(b1).maps(Ramp.FUNCTION);
      Tensor x2 = x1.dot(W2).add(b2);
      Tensor probs = SoftmaxLayer.of(x2);
      int prediction = ArgMax.of(probs);
      System.out.println("I: " + x0 + " | " + prediction + "=" + y.get(n) + " | p= " + probs.maps(Round._2));
    }
  }

  public static void main(String[] args) {
    // Toy 3-class dataset (2D points)
    Tensor X = Tensors.matrixDouble(new double[][] { //
        { 1, 1 }, { 1.5, 2 }, { 2, 1 }, // Class 0
        { 5, 5 }, { 6, 5 }, { 5, 6 }, // Class 1
        { 8, 1 }, { 9, 2 }, { 8, 2 } // Class 2
    });
    Tensor y = Tensors.vectorInt(new int[] { 0, 0, 0, 1, 1, 1, 2, 2, 2 });
    SoftmaxMLP softmaxMLP = new SoftmaxMLP();
    softmaxMLP.train(X, y, 5000);
    softmaxMLP.evaluate(X, y);
  }
}
