// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.tri.ArcTan;

class ArcTanDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Show show = new Show();
    Clip clip = Clips.absolute(1);
    show.add(Plot.of(ArcTan.FUNCTION, clip));
    return show;
  }

  static void main() {
    new ArcTanDemo().runStandalone();
  }
}
