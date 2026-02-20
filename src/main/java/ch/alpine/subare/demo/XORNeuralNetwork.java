// code adapted from chatgpt
package ch.alpine.subare.demo;

import java.awt.Container;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.io.TableBuilder;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.exp.DLogisticSigmoid;
import ch.alpine.tensor.sca.exp.LogisticSigmoid;

/** Quote from chatgpt:
 * 
 * This example:
 * Uses 1 hidden layer
 * Uses sigmoid activation
 * Trains with stochastic gradient descent
 * Prints evaluation after training
 * 
 * The example network has 3 layers total:
 * Input layer – 2 neurons
 * Inputs: x1, x2
 * Hidden layer – 4 neurons
 * Uses sigmoid activation
 * This is what allows the network to solve XOR (non-linear problem)
 * Output layer – 1 neuron
 * Produces the XOR result
 * Also uses sigmoid activation */
@ReflectionMarker
public class XORNeuralNetwork implements ManipulateProvider {
  static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(0.5));
  private final Tensor inputs = Tensors.matrixInt(new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }).unmodifiable();
  private final Tensor targets = Tensors.matrixInt(new int[][] { { 0 }, { 1 }, { 1 }, { 0 } }).unmodifiable();
  // ---
  public Integer hiddenSize = 4;
  public Scalar learningRate = Rational.HALF;
  public Integer maxEpoch = 8000;
  public Scalar timeout = Quantity.of(0.4, "s");
  public Boolean advance = false;

  public class XORNet {
    Tensor w1;
    Tensor b1;
    Tensor w2;
    Tensor b2;
    TableBuilder tableBuilder = new TableBuilder();

    public XORNet() {
      int INPUT_SIZE = 2;
      int OUTPUT_SIZE = 1;
      w1 = RandomVariate.of(DISTRIBUTION, hiddenSize, INPUT_SIZE);
      b1 = RandomVariate.of(DISTRIBUTION, hiddenSize);
      w2 = RandomVariate.of(DISTRIBUTION, OUTPUT_SIZE, hiddenSize);
      b2 = RandomVariate.of(DISTRIBUTION, OUTPUT_SIZE);
    }

    void train(Tensor inputs, Tensor target) {
      int epoch = 0;
      Timing timing = Timing.started();
      while (Scalars.lessThan(timing.seconds(), timeout) && epoch < maxEpoch) {
        for (int sample = 0; sample < inputs.length(); ++sample) {
          Tensor x0 = inputs.get(sample);
          Tensor y2 = target.get(sample);
          // Forward pass ---
          Tensor x1 = w1.dot(x0).add(b1).maps(LogisticSigmoid.FUNCTION);
          Tensor x2 = w2.dot(x1).add(b2).maps(LogisticSigmoid.FUNCTION);
          // Backpropagation ---
          Tensor e2 = y2.subtract(x2);
          Tensor d2 = Entrywise.mul().apply(e2, x2.maps(DLogisticSigmoid.NESTED));
          Tensor e1 = d2.dot(w2);
          Tensor d1 = Entrywise.mul().apply(e1, x1.maps(DLogisticSigmoid.NESTED));
          w2 = w2.add(TensorProduct.of(d2, x1).multiply(learningRate));
          b2 = b2.add(d2.multiply(learningRate));
          w1 = w1.add(TensorProduct.of(d1, x0).multiply(learningRate));
          b1 = b1.add(d1.multiply(learningRate));
        }
        if (epoch % 10 == 0) {
          tableBuilder.appendRow(w1, b1, w2, b2);
        }
        ++epoch;
      }
    }

    void evaluate(Tensor inputs) {
      System.out.println("Evaluation after training:");
      for (Tensor x0 : inputs) {
        Tensor x1 = w1.dot(x0).add(b1).maps(LogisticSigmoid.FUNCTION);
        Tensor x2 = w2.dot(x1).add(b2).maps(LogisticSigmoid.FUNCTION);
        System.out.printf("Input: %s -> Output: %s\n", x0, x2);
      }
    }
  }

  public Show getShow() {
    XORNet xorNet = new XORNet();
    xorNet.train(inputs, targets);
    xorNet.evaluate(inputs);
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

  public static void main(String[] args) {
    new XORNeuralNetwork().runStandalone();
  }
}
