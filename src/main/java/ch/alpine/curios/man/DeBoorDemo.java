// code by jph
package ch.alpine.curios.man;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.itp.BSplineFunction;
import ch.alpine.tensor.itp.BSplineFunctionString;
import ch.alpine.tensor.itp.DeBoor;

@ReflectionMarker
public class DeBoorDemo implements ManipulateProvider {
  @FieldSelectionArray({ "0", "1", "2", "3", "4", "5", "6" })
  public Scalar degree = RealScalar.of(1);
  public ColorDataLists cdl = ColorDataLists._097;
  public Color color = new Color(0, 0, 0);

  @Override
  public JComponent getContainer() {
    List<Show> list = new LinkedList<>();
    int deg = degree.number().intValue();
    for (int length = 2; length <= 6; ++length) {
      Tensor domain = Subdivide.of(0, length - 1, (length - 1) * 20);
      Show show = new Show(cdl.cyclic());
      for (int k_th = 0; k_th < length; ++k_th) {
        BSplineFunction bSplineFunction = (BSplineFunction) BSplineFunctionString.of(deg, UnitVector.of(length, k_th));
        DeBoor deBoor = bSplineFunction.deBoor(k_th);
        Tensor knots = deBoor.knots();
        String title = length + " " + k_th + ":" + knots.toString().replace(" ", ""); //
        Tensor values = domain.maps(bSplineFunction);
        Tensor tensor = Transpose.of(Tensors.of(domain, values));
        Showable showable = show.add(ListLinePlot.of(tensor));
        showable.setLabel(title);
      }
      list.add(show);
    }
    return ShowGridComponent.of(list);
  }

  static void main() {
    new DeBoorDemo().runStandalone();
  }
}
