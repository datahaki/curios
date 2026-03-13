// code by jph
package ch.alpine.curios.man;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.crv.GeodesicBSplineFunction;
import ch.alpine.sophus.lie.rn.RGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.img.ColorDataIndexed;
import ch.alpine.tensor.img.ColorDataLists;

@ReflectionMarker
class BSplineBasisDemo implements ManipulateProvider {
  @FieldSelectionArray({ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" })
  public Integer degree = 1;
  public ColorDataLists cdl = ColorDataLists._097;

  @Override
  public JComponent getContainer() {
    List<Show> list = new LinkedList<>();
    int _degree = degree;
    ColorDataIndexed colorDataIndexed = cdl.cyclic().deriveWithAlpha(192);
    for (int length = 2; length <= 8; ++length) {
      int upper = length - 1;
      Tensor domain = Subdivide.of(0, upper, 20 * upper);
      Show show = new Show(colorDataIndexed);
      for (int k_th = 0; k_th < length; ++k_th) {
        Tensor knots = UnitVector.of(length, k_th);
        GeodesicBSplineFunction bSplineFunction = //
            GeodesicBSplineFunction.of(RGroup.INSTANCE, _degree, knots);
        Tensor values = domain.maps(bSplineFunction);
        Tensor tensor = Transpose.of(Tensors.of(domain, values));
        show.add(ListLinePlot.of(tensor)).setLabel("" + knots);
      }
      list.add(show);
    }
    return ShowGridComponent.of(list);
  }

  static void main() {
    new BSplineBasisDemo().runStandalone();
  }
}
