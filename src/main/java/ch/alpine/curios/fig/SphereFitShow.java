// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;
import java.util.Optional;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.ParametricPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.fit.SphereFit;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.rot.AngleVector;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class SphereFitShow implements ManipulateProvider {
  public Integer n = 7;

  @Override
  public Container getContainer() {
    Tensor points = RandomVariate.of(UniformDistribution.unit(), n, 2);
    Optional<SphereFit> optional = SphereFit.of(points);
    Tensor center = optional.get().center();
    Scalar radius = optional.get().radius();
    Show show = new Show();
    show.setAspectRatioOne();
    show.add(ParametricPlot.of(s -> AngleVector.of(s).multiply(radius).add(center), Clips.absolute(Pi.VALUE)));
    show.add(ListPlot.of(points));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new SphereFitShow().runStandalone();
  }
}
