// code adapted from chatgpt
package ch.alpine.subare.demo.net;

import java.awt.Container;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.ArgMax;
import ch.alpine.tensor.io.TableBuilder;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.num.SoftmaxLayer;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.Ramp;
import ch.alpine.tensor.sca.Round;
import ch.alpine.tensor.sca.UnitStep;

@ReflectionMarker
public class SoftmaxMLP implements ManipulateProvider {
  static final Distribution DISTRIBUTION = NormalDistribution.of(0.0, 0.1);
  static final Tensor X = Tensors.matrixDouble(new double[][] { //
      { 1, 1 }, { 1.5, 2 }, { 2, 1 }, // Class 0
      { 5, 5 }, { 6, 5 }, { 5, 6 }, // Class 1
      { 8, 1 }, { 9, 2 }, { 8, 2 } // Class 2
  }).unmodifiable();
  static final Tensor y = Tensors.vectorInt(new int[] { 0, 0, 0, 1, 1, 1, 2, 2, 2 }).unmodifiable();
  public Integer HIDDEN_SIZE = 8;
  public Scalar learningRate = RealScalar.of(0.05);
  public Integer maxEpoch = 8000;
  public Scalar timeout = Quantity.of(0.4, "s");

  public class XORNet {
    final int INPUT_SIZE = 2;
    final int OUTPUT_SIZE = 3;
    Tensor W1;
    Tensor b1;
    Tensor W2;
    Tensor b2;
    TableBuilder tableBuilder = new TableBuilder();

    public XORNet() {
      W1 = RandomVariate.of(DISTRIBUTION, INPUT_SIZE, HIDDEN_SIZE);
      b1 = RandomVariate.of(DISTRIBUTION, HIDDEN_SIZE).maps(Scalar::zero);
      W2 = RandomVariate.of(DISTRIBUTION, HIDDEN_SIZE, OUTPUT_SIZE);
      b2 = RandomVariate.of(DISTRIBUTION, OUTPUT_SIZE).maps(Scalar::zero);
    }

    void train(Tensor X, Tensor y, int epochs) {
      int epoch = 0;
      Timing timing = Timing.started();
      while (Scalars.lessThan(timing.seconds(), timeout) && epoch < maxEpoch) {
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
        if (epoch % 10 == 0) {
          tableBuilder.appendRow(W1, b1, W2, b2);
        }
        ++epoch;
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
  }

  public Show getShow() {
    XORNet xorNet = new XORNet();
    xorNet.train(X, y, 5000);
    xorNet.evaluate(X, y);
    Tensor table = xorNet.tableBuilder.getTable();
    IO.println(Dimensions.of(table));
    int n = Unprotect.dimension1Hint(table);
    Tensor domain = Range.of(0, table.length());
    Show show = new Show();
    for (int i = 0; i < n; ++i)
      show.add(ListLinePlot.of(domain, table.get(Tensor.ALL, i)));
    return show;
  }

  @Override
  public Container getContainer() {
    return ShowGridComponent.of(getShow());
  }

  static void main() {
    new SoftmaxMLP().runStandalone();
  }
}
