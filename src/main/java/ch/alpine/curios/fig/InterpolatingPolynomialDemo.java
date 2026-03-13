// code by jph
package ch.alpine.curios.fig;

import java.awt.BasicStroke;
import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.ply.ChebyshevNodes;
import ch.alpine.tensor.sca.ply.InterpolatingPolynomial;
import ch.alpine.tensor.sca.ply.Polynomial;

@ReflectionMarker
class InterpolatingPolynomialDemo implements ManipulateProvider {
  public Tensor coeffs = Tensors.vector(3, 2, .3, -1);
  public Clip clip = Clips.interval(0.3, 0.8);

  @Override
  public Container getContainer() {
    Polynomial f = Polynomial.of(coeffs);
    Show show = new Show();
    Showable showable = show.add(Plot.of(f, clip));
    showable.setStroke(new BasicStroke(10));
    for (int d = 1; d < 10; ++d) {
      Tensor init = ChebyshevNodes._1.of(d);
      Tensor knots = init.maps(Clips.absoluteOne()::rescale);
      knots = knots.maps(LinearInterpolation.of(clip));
      InterpolatingPolynomial interpolatingPolynomial = InterpolatingPolynomial.of(knots);
      ScalarUnaryOperator suo = interpolatingPolynomial.scalarUnaryOperator(knots.maps(f));
      Showable showable2 = show.add(Plot.of(suo, clip));
      showable2.setLabel("deg " + d);
    }
    return ShowGridComponent.of(show);
  }

  static void main() {
    new InterpolatingPolynomialDemo().runStandalone();
  }
}
