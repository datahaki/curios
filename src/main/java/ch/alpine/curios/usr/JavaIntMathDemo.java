// code by jph
package ch.alpine.curios.usr;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowComponent;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;

class JavaIntMathDemo implements ManipulateProvider {
  public JavaIntMath JIM = JavaIntMath.CEIL_DIV;
  private final ShowComponent sc = new ShowComponent();

  @Override
  public Container getContainer() {
    Show show = new Show();
    Tensor v = Tensor.of(Range.of(-10, 11).stream().map(Scalar.class::cast).mapToInt(s -> s.number().intValue()) //
        .mapToObj(i -> Tensors.vector(i, JIM.ibo.applyAsInt(i, 5))));
    show.add(ListPlot.of(v));
    sc.setShow(show);
    return sc;
  }

  static void main() {
    new JavaIntMathDemo().runStandalone();
  }
}
