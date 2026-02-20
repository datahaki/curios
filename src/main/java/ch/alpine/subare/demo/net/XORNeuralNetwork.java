// code adapted from chatgpt
package ch.alpine.subare.demo.net;

import java.awt.Container;
import java.util.List;
import java.util.stream.IntStream;

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
import ch.alpine.tensor.ext.MergeIllegal;
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
    final TableBuilder tableBuilder = new TableBuilder();
    private final List<Layer> layers;

    public XORNet() {
      int INPUT_SIZE = 2;
      int OUTPUT_SIZE = 1;
      layers = List.of( //
          LinearLayer.logSig(DISTRIBUTION, INPUT_SIZE, hiddenSize), //
          LinearLayer.logSig(DISTRIBUTION, hiddenSize, OUTPUT_SIZE), //
          new BinaryLayer());
    }

    void train(Tensor X, Tensor y) {
      int epoch = 0;
      Timing timing = Timing.started();
      while (Scalars.lessThan(timing.seconds(), timeout) && epoch < maxEpoch) {
        for (int sample = 0; sample < X.length(); ++sample) {
          Tensor x = X.get(sample);
          // Forward pass
          layers.stream().reduce(x, (r, l) -> l.forward(r), MergeIllegal.operator());
          // l3.forward(l2.forward(l1.forward(x)));
          // Backpropagation
          // ---
          Tensor d = layers.getLast().error(y.get(sample)).multiply(learningRate);
          IntStream.range(0, layers.size()).mapToObj(i -> layers.get(layers.size() - 1 - i)) //
              .reduce(d, (r, l) -> l.back(r), MergeIllegal.operator());
          // ---
          layers.forEach(Layer::update);
          // l3.update();
          // l2.update();
          // l1.update();
        }
        if (epoch % 10 == 0)
          tableBuilder.appendRow(Tensor.of(layers.stream().map(Layer::parameters)));
        ++epoch;
      }
    }

    void evaluate(Tensor inputs) {
      System.out.println("Evaluation after training:");
      for (Tensor x : inputs) {
        Tensor res = layers.stream().reduce(x, (r, l) -> l.forward(r), MergeIllegal.operator());
        System.out.printf("Input: %s -> Output: %s\n", x, res);
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
