// code by jph
package ch.alpine.curios.sca;

import java.awt.BasicStroke;
import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.col.ColorDataLists;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.ply.ChebyshevInterpolation;
import ch.alpine.tensor.sca.ply.ChebyshevNodes;
import ch.alpine.tensor.sca.ply.InterpolatingPolynomial;
import ch.alpine.tensor.sca.tri.Sin;

@ReflectionMarker
class ChebyshevInterpDemo implements ManipulateProvider {
  public Integer n = 6;
  public ChebyshevNodes chebyshevNodes = ChebyshevNodes._1;
  public ColorDataLists cdl = ColorDataLists._097;

  @Override
  public Container getContainer() {
    ScalarUnaryOperator suo0 = x -> Sin.FUNCTION.apply(x.multiply(x).negate().add(x));
    Clip clip = Clips.absoluteOne();
    ScalarUnaryOperator suo1 = ChebyshevInterpolation.of(suo0, chebyshevNodes, n);
    ScalarUnaryOperator suo2 = ChebyshevInterpolation.of(suo0, chebyshevNodes, n);
    Tensor knots = chebyshevNodes.of(n);
    InterpolatingPolynomial ip = InterpolatingPolynomial.of(knots);
    ScalarUnaryOperator suo3 = ip.scalarUnaryOperator(knots.maps(suo0));
    Show show1 = new Show(cdl.cyclic().deriveWithAlpha(128));
    show1.setShowLabel("Functions");
    show1.add(Plot.of(suo0, clip)).setLabel("f");
    show1.add(Plot.of(suo1, clip)).setLabel("interp");
    Showable showable = show1.add(Plot.of(suo2, clip));
    showable.setLabel("alt");
    showable.setStroke(new BasicStroke(5));
    show1.add(Plot.of(suo3, clip)).setLabel("inpol");
    Show show2 = new Show();
    show2.setShowLabel("Error");
    show2.add(Plot.of(s -> suo1.apply(s).subtract(suo2.apply(s)), clip));
    return ShowGridComponent.of(show1, show2);
  }

  static void main() {
    new ChebyshevInterpDemo().runStandalone();
  }
}
