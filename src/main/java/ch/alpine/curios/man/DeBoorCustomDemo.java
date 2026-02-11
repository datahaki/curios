// code by jph
package ch.alpine.curios.man;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldPreferredWidth;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophus.lie.rn.RGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.itp.DeBoor;

@ReflectionMarker
public class DeBoorCustomDemo implements ManipulateProvider {
  @FieldPreferredWidth(200)
  @FieldSelectionArray({ "{0, 1}", "{0, .2, .8, 1}", "{0, 0, 1, 1}" })
  public Tensor knots = Tensors.vector(0, 1);
  public ColorDataLists cdl = ColorDataLists._097;

  @Override
  public JComponent getJComponent() {
    Show show = new Show(cdl.cyclic().deriveWithAlpha(192));
    try {
      Tensor domain = Subdivide.of(0, 1, 100);
      Tensor domahi = Subdivide.of(1, 1.3, 100);
      Tensor _knots = knots;
      if (Integers.isEven(_knots.length())) {
        int degree = _knots.length() >> 1;
        int length = degree + 1;
        // ---
        for (int k_th = 0; k_th < length; ++k_th) {
          DeBoor deBoor = DeBoor.of(RGroup.INSTANCE, _knots, UnitVector.of(length, k_th));
          {
            Tensor values = domain.maps(deBoor);
            Tensor tensor = Transpose.of(Tensors.of(domain, values));
            show.add(ListLinePlot.of(tensor));
          }
          {
            Stroke stroke = new BasicStroke(1.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3 }, 0);
            Tensor values = domahi.maps(deBoor);
            Tensor tensor = Transpose.of(Tensors.of(domahi, values));
            Showable showable = show.add(ListLinePlot.of(tensor));
            showable.setStroke(stroke);
          }
        }
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return ShowGridComponent.of(List.of(show));
  }

  static void main() {
    new DeBoorCustomDemo().run();
  }
}
