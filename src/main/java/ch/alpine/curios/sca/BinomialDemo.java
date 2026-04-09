// code by jph
package ch.alpine.curios.sca;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.MatrixPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.col.ColorDataGradients;
import ch.alpine.tensor.num.GaussScalar;

@ReflectionMarker
class BinomialDemo implements ManipulateProvider {
  @FieldSelectionArray({ "251", "653", "997" })
  public Integer prime = 251;
  public ColorDataGradients cdg = ColorDataGradients.CLASSIC;

  @Override
  public Container getContainer() {
    Tensor tensor = Array.zeros(prime + 1, prime + 1);
    for (int i = 1; i < prime; ++i) {
      Scalar n = GaussScalar.of(i, prime);
      Tensor row = row(n);
      for (int k = 0; k < row.length(); ++k)
        tensor.set(RealScalar.of(row.Get(k).number()), i, k);
    }
    Show show = new Show();
    show.add(MatrixPlot.of(tensor, cdg));
    return ShowGridComponent.of(show);
  }

  static Tensor row(Scalar n) {
    Tensor row = Tensors.of(n.one());
    Scalar top = n;
    for (Scalar j = n.one(); true; j = j.add(n.one())) {
      Scalar x = Last.of(row);
      row.append(x.multiply(top).divide(j));
      if (j.equals(n))
        return row;
      top = top.subtract(n.one());
    }
  }

  static void main() {
    new BinomialDemo().runStandalone();
  }
}
