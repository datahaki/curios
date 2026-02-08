// code by jph
package ch.alpine.curios.sca;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.itp.BSplineBasis;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class BSplineBasisDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Clip clip = Clips.absolute(3);
    Show show = new Show();
    show.setPlotLabel("BSplineBasis");
    for (int d = 0; d < 6; ++d) {
      Showable showable = show.add(Plot.of(BSplineBasis.of(d), clip));
      showable.setLabel("deg " + d);
    }
    return show;
  }

  static void main() {
    new BSplineBasisDemo().run();
  }
}
