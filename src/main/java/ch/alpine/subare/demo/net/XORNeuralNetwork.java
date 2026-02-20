// code adapted from chatgpt
package ch.alpine.subare.demo.net;

import java.awt.Container;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
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
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;
import ch.alpine.tensor.sca.Clips;

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
  @FieldSelectionArray({ "4", "5", "6", "7" })
  public Integer hiddenSize = 4;
  public Scalar learningRate = Rational.HALF;
  public Integer maxEpoch = 8000;
  public Scalar timeout = Quantity.of(1, "s");

  public class XORNet {
    final LinearLayer l1;
    final LinearLayer l2;
    final Layer l3;
    final TableBuilder tableBuilder = new TableBuilder();

    public XORNet() {
      int INPUT_SIZE = 2;
      int OUTPUT_SIZE = 1;
      l1 = LinearLayer.logSig(DISTRIBUTION, INPUT_SIZE, hiddenSize);
      l2 = LinearLayer.logSig(DISTRIBUTION, hiddenSize, OUTPUT_SIZE);
      l3 = new IdentLayer();
    }

    void train(Tensor X, Tensor y) {
      int epoch = 0;
      Timing timing = Timing.started();
      while (Scalars.lessThan(timing.seconds(), timeout) && epoch < maxEpoch) {
        for (int sample = 0; sample < X.length(); ++sample) {
          Tensor x0 = X.get(sample);
          // Forward pass
          Tensor x1 = l1.forward(x0);
          Tensor x2 = l2.forward(x1);
          l3.forward(x2);
          // Backpropagation
          // ---
          Tensor e2 = l3.error(y.get(sample)).multiply(learningRate);
          Tensor d2 = l3.back(e2);
          Tensor d1 = l2.back(d2);
          // ---
          l2.update(d2);
          l1.update(d1);
        }
        if (epoch % 10 == 0)
          tableBuilder.appendRow(l1.W, l1.b, l2.W, l2.b);
        ++epoch;
      }
    }

    void evaluate(Tensor inputs) {
      System.out.println("Evaluation after training:");
      for (Tensor x0 : inputs) {
        Tensor x1 = l1.forward(x0);
        Tensor x2 = l2.forward(x1);
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

  static void main() {
    new XORNeuralNetwork().runStandalone();
  }
}
