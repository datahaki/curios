// code by jph
package ch.alpine.curios.man;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.jet.EllipticCurve;
import ch.alpine.tensor.mat.UpperEvaluation;
import ch.alpine.tensor.num.GaussScalar;

@ReflectionMarker
public class EllipticCurveFinite implements ManipulateProvider {
  @FieldSelectionArray({ "61", "347", "1667" })
  public Integer prime = 61;
  public Integer a = 9;
  public Integer b = 1;

  @Override
  public JComponent getJComponent() {
    EllipticCurve ellipticCurve = EllipticCurve.of(GaussScalar.of(a, prime), GaussScalar.of(b, prime));
    Scalar discriminant = ellipticCurve.discriminant();
    Show show = new Show();
    if (Scalars.isZero(discriminant)) {
      System.err.println("discriminant zero");
    } else {
      show.setPlotLabel("discriminant=" + discriminant);
      Tensor all = Tensors.empty();
      for (int i = 0; i < prime; ++i)
        try {
          Tensor p = ellipticCurve.complete(GaussScalar.of(i, prime));
          all.append(p);
        } catch (Exception e) {
          // System.err.println("empty for " + i);
        }
      Tensor matrix = UpperEvaluation.of(all, all, ellipticCurve::combine, s -> s);
      Tensor list = Tensor.of(Flatten.of(matrix, 1).stream() //
          .distinct() //
          .filter(Tensors::nonEmpty) //
          .map(xy -> Tensors.vector(xy.Get(0).number(), xy.Get(1).number())));
      show.add(ListPlot.of(list));
    }
    show.setAspectRatioOne();
    return ShowGridComponent.of(show);
  }

  static void main() {
    new EllipticCurveFinite().run();
  }
}
