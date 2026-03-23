// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.DensityPlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.noise.SimplexContinuousNoise;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class SimplexContinuousNoiseDemo implements ManipulateProvider {
  @Override
  public Container getContainer() {
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.absolute(15), Clips.absolute(15));
    Show show1 = new Show();
    show1.setShowLabel("SimplexContinuousNoise[x,y]");
    show1.add(DensityPlot.of( //
        (x, y) -> SimplexContinuousNoise.FUNCTION.apply(Tensors.of(x, y)), //
        cbb));
    Show show2 = new Show();
    show2.setShowLabel("SimplexContinuousNoise[x,y,0]");
    show2.add(DensityPlot.of( //
        (x, y) -> SimplexContinuousNoise.FUNCTION.apply(Tensors.of(x, y, x.zero())), //
        cbb));
    Show show3 = new Show();
    show3.setShowLabel("SimplexContinuousNoise[x,y,0,0]");
    show3.add(DensityPlot.of( //
        (x, y) -> SimplexContinuousNoise.FUNCTION.apply(Tensors.of(x, y, x.zero(), x.zero())), //
        cbb));
    return ShowGridComponent.of(show1, show2, show3);
  }

  static void main() {
    new SimplexContinuousNoiseDemo().runStandalone();
  }
}
