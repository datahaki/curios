// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.DensityPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowWindow;
import ch.alpine.sophus.math.noise.SimplexContinuousNoise;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clips;

public enum NoiseDemo {
  ;
  static void main() {
    CoordinateBoundingBox cbb = CoordinateBoundingBox.of(Clips.absolute(15), Clips.absolute(15));
    Show show1 = new Show();
    show1.setPlotLabel("SimplexContinuousNoise[x,y]");
    show1.add(DensityPlot.of( //
        (x, y) -> SimplexContinuousNoise.FUNCTION.apply(Tensors.of(x, y)), //
        cbb));
    Show show2 = new Show();
    show2.setPlotLabel("SimplexContinuousNoise[x,y,0]");
    show2.add(DensityPlot.of( //
        (x, y) -> SimplexContinuousNoise.FUNCTION.apply(Tensors.of(x, y, x.zero())), //
        cbb));
    Show show3 = new Show();
    show3.setPlotLabel("SimplexContinuousNoise[x,y,0,0]");
    show3.add(DensityPlot.of( //
        (x, y) -> SimplexContinuousNoise.FUNCTION.apply(Tensors.of(x, y, x.zero(), x.zero())), //
        cbb));
    ShowWindow.asDialog(show1, show2, show3);
  }
}
