// code by jph
package ch.alpine.curios.man;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
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
public class BSplineBasisDemo implements ManipulateProvider {
  @FieldSelectionArray({ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" })
  public Integer degree = 1;
  public ColorDataLists cdl = ColorDataLists._097;

  @Override
  public JComponent getJComponent() {
    List<Show> list = new LinkedList<>();
    int _degree = degree;
    ColorDataIndexed colorDataIndexed = cdl.cyclic().deriveWithAlpha(192);
    for (int length = 2; length <= 8; ++length) {
      Tensor domain = Subdivide.of(0, length - 1, 100);
      Show show = new Show(colorDataIndexed);
      for (int k_th = 0; k_th < length; ++k_th) {
        GeodesicBSplineFunction bSplineFunction = //
            GeodesicBSplineFunction.of(RGroup.INSTANCE, _degree, UnitVector.of(length, k_th));
        Tensor values = domain.maps(bSplineFunction);
        Tensor tensor = Transpose.of(Tensors.of(domain, values));
        show.add(ListLinePlot.of(tensor));
      }
      list.add(show);
    }
    return ShowGridComponent.of(list);
  }

  static void main() {
    new BSplineBasisDemo().run();
  }
}
