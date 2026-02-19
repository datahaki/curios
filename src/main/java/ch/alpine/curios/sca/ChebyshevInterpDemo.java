// code by jph
package ch.alpine.curios.sca;

import java.awt.BasicStroke;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ShowWindow;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.ply.ChebyshevInterpolation;
import ch.alpine.tensor.sca.ply.ChebyshevNodes;
import ch.alpine.tensor.sca.ply.InterpolatingPolynomial;
import ch.alpine.tensor.sca.tri.Sin;

public enum ChebyshevInterpDemo {
  ;
  static void main() {
    int n = 6;
    ScalarUnaryOperator suo0 = x -> Sin.FUNCTION.apply(x.multiply(x).negate().add(x));
    // suo = Exp.FUNCTION;
    Clip clip = Clips.absoluteOne();
    ChebyshevNodes chebyshevNodes = ChebyshevNodes._1;
    ScalarUnaryOperator suo1 = ChebyshevInterpolation.of(suo0, chebyshevNodes, n);
    ScalarUnaryOperator suo2 = ChebyshevInterpolation.of(suo0, chebyshevNodes, n);
    Tensor knots = chebyshevNodes.of(n);
    InterpolatingPolynomial ip = InterpolatingPolynomial.of(knots);
    ScalarUnaryOperator suo3 = ip.scalarUnaryOperator(knots.maps(suo0));
    Show show1 = new Show(ColorDataLists._097.cyclic().deriveWithAlpha(128));
    show1.setPlotLabel("Functions");
    show1.add(Plot.of(suo0, clip)).setLabel("f");
    show1.add(Plot.of(suo1, clip)).setLabel("interp");
    Showable showable = show1.add(Plot.of(suo2, clip));
    showable.setLabel("alt");
    showable.setStroke(new BasicStroke(5));
    show1.add(Plot.of(suo3, clip)).setLabel("inpol");
    Show show2 = new Show();
    show2.setPlotLabel("Error");
    show2.add(Plot.of(s -> suo1.apply(s).subtract(suo2.apply(s)), clip));
    ShowWindow.asDialog(show1, show2);
  }
}
