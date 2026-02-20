// code adapted from chatgpt
package ch.alpine.subare.demo.net;

import java.awt.Container;
import java.util.List;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.ext.MergeIllegal;
import ch.alpine.tensor.io.TableBuilder;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;

@ReflectionMarker
public class SoftmaxMLP implements ManipulateProvider {
  static final Distribution DISTRIBUTION = NormalDistribution.of(0.0, 0.1);
  public static final Tensor X = Tensors.matrixDouble(new double[][] { //
      { 1, 1 }, { 1.5, 2 }, { 2, 1 }, // Class 0
      { 5, 5 }, { 6, 5 }, { 5, 6 }, // Class 1
      { 8, 1 }, { 9, 2 }, { 8, 2 } // Class 2
  }).unmodifiable();
  public static final Tensor y = Tensors.vectorInt(new int[] { 0, 0, 0, 1, 1, 1, 2, 2, 2 }).unmodifiable();
  @FieldSelectionArray({ "6", "7", "8", "10" })
  public Integer hiddenSize = 8;
  public Scalar learningRate = RealScalar.of(0.05);
  public Integer maxEpoch = 8000;
  public Scalar timeout = Quantity.of(1, "s");

  public class XORNet {
    final int INPUT_SIZE = 2;
    final int OUTPUT_SIZE = 3;
    private final List<Layer> layers;
    final TableBuilder tableBuilder = new TableBuilder();

    public XORNet() {
      layers = List.of( //
          LinearLayer.reLu(DISTRIBUTION, INPUT_SIZE, hiddenSize), //
          LinearLayer.maxE(DISTRIBUTION, hiddenSize, OUTPUT_SIZE), //
          new SoftArgMax());
    }

    void train(Tensor X, Tensor T) {
      int epoch = 0;
      Timing timing = Timing.started();
      while (Scalars.lessThan(timing.seconds(), timeout) && epoch < maxEpoch) {
        for (int n = 0; n < X.length(); n++) {
          // Forward pass
          layers.stream().reduce(X.get(n), Layer.forward(), MergeIllegal.operator());
          // Backpropagation
          Tensor d = layers.getLast().error(T.Get(n)).multiply(learningRate);
          // Cross-Entropy + Softmax simplifies gradient
          layers.reversed().stream().reduce(d, Layer.back(), MergeIllegal.operator());
          // ---
          layers.forEach(Layer::update);
        }
        if (epoch % 10 == 0)
          tableBuilder.appendRow(Tensor.of(layers.stream().map(Layer::parameters)));
        ++epoch;
      }
    }

    Scalar evaluate(Tensor X, Tensor T) {
      System.out.println("Evaluation:");
      Tensor errors = Tensors.empty();
      for (int n = 0; n < X.length(); n++) {
        Tensor x = X.get(n);
        Tensor y = layers.stream().reduce(x, Layer.forward(), MergeIllegal.operator());
        Tensor error = y.subtract(T.get(n));
        System.out.println("I: " + x + " | " + y + "=" + T.get(n)); // + probs.maps(Round._2)
        errors.append(error);
      }
      return FrobeniusNorm.of(errors);
    }
  }

  public Show getShow() {
    XORNet xorNet = new XORNet();
    xorNet.train(X, y);
    Scalar error = xorNet.evaluate(X, y);
    Tensor table = xorNet.tableBuilder.getTable();
    IO.println(error);
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
