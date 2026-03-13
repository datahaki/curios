// code by jph
package ch.alpine.curios.man;

import java.awt.Container;
import java.util.function.Function;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.win.UniformWindowSampler;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.win.WindowFunctions;

@ReflectionMarker
public class WindowFunctionsDemo implements ManipulateProvider {
  public WindowFunctions wf = WindowFunctions.BARTLETT;
  @FieldSelectionArray({ "3", "4", "6", "9", "12", "15", "18" })
  public Integer res = 6;

  @Override
  public Container getContainer() {
    ScalarUnaryOperator suo = wf.get();
    Clip clip = Clips.absolute(Rational.HALF);
    Show show = new Show();
    show.add(Plot.of(suo, clip));
    Function<Integer, Tensor> sampler = UniformWindowSampler.of(suo);
    Tensor py = sampler.apply(res);
    Tensor px = Subdivide.increasing(clip, py.length() - 1);
    show.add(ListLinePlot.of(px, py));
    show.add(ListPlot.of(px, py));
    show.setCbb(CoordinateBoundingBox.of(clip, Clips.interval(-0.2, 1)));
    show.setAspectRatioOne();
    return ShowGridComponent.of(show);
  }

  static void main() {
    new WindowFunctionsDemo().runStandalone();
  }
}
