// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.exp.Gelu;

class GeluDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Show show = new Show();
    Clip clip = Clips.absolute(2);
    show.add(Plot.of(Gelu.FUNCTION, clip));
    show.add(Plot.of(Gelu::dx, clip));
    show.setAspectRatioOne();
    return show;
  }

  static void main() {
    new GeluDemo().runStandalone();
  }
}
