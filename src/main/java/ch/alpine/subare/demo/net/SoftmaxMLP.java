// code adapted from chatgpt
package ch.alpine.subare.demo.net;

import java.awt.Container;

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
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.io.TableBuilder;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;

@ReflectionMarker
public class SoftmaxMLP implements ManipulateProvider {
  static final Distribution DISTRIBUTION = NormalDistribution.of(0.0, 0.1);
  static final Tensor X = Tensors.matrixDouble(new double[][] { //
      { 1, 1 }, { 1.5, 2 }, { 2, 1 }, // Class 0
      { 5, 5 }, { 6, 5 }, { 5, 6 }, // Class 1
      { 8, 1 }, { 9, 2 }, { 8, 2 } // Class 2
  }).unmodifiable();
  static final Tensor y = Tensors.vectorInt(new int[] { 0, 0, 0, 1, 1, 1, 2, 2, 2 }).unmodifiable();
  @FieldSelectionArray({ "6", "7", "8", "10" })
  public Integer hiddenSize = 8;
  public Scalar learningRate = RealScalar.of(0.05);
  public Integer maxEpoch = 8000;
  public Scalar timeout = Quantity.of(1, "s");

  public class XORNet {
    final int INPUT_SIZE = 2;
    final int OUTPUT_SIZE = 3;
    final LinearLayer l1;
    final LinearLayer l2;
    final Layer l3;
    final TableBuilder tableBuilder = new TableBuilder();

    public XORNet() {
      l1 = LinearLayer.reLu(DISTRIBUTION, INPUT_SIZE, hiddenSize);
      l2 = LinearLayer.maxE(DISTRIBUTION, hiddenSize, OUTPUT_SIZE);
      l3 = new SoftArgMax();
    }

    void train(Tensor X, Tensor y) {
      int epoch = 0;
      Timing timing = Timing.started();
      while (Scalars.lessThan(timing.seconds(), timeout) && epoch < maxEpoch) {
        for (int n = 0; n < X.length(); n++) {
          // Forward pass
          Tensor x0 = X.get(n);
          Tensor x1 = l1.forward(x0);
          Tensor x2 = l2.forward(x1);
          Tensor x3 = l3.forward(x2);
          // Backpropagation
          Tensor d3 = l3.error(y.Get(n)).multiply(learningRate);
          // Cross-Entropy + Softmax simplifies gradient
          l1.back(l2.back(l3.back(d3)));
          // ---
          l3.update();
          l2.update();
          l1.update();
        }
        if (epoch % 10 == 0)
          tableBuilder.appendRow(l1.W, l1.b, l2.W, l2.b);
        ++epoch;
      }
    }

    void evaluate(Tensor X, Tensor y) {
      System.out.println("Evaluation:");
      for (int n = 0; n < X.length(); n++) {
        Tensor x0 = X.get(n);
        Tensor x1 = l1.forward(x0);
        Tensor x2 = l2.forward(x1);
        Tensor x3 = l3.forward(x2);
        System.out.println("I: " + x0 + " | " + x3 + "=" + y.get(n)); // + probs.maps(Round._2)
      }
    }
  }

  public Show getShow() {
    XORNet xorNet = new XORNet();
    xorNet.train(X, y);
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
