// code by jph
package ch.alpine.curios.gal;

import java.awt.Window;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ShowWindow;
import ch.alpine.bridge.pro.WindowProvider;
import ch.alpine.bridge.swing.LookAndFeels;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.ply.Chebyshev;

enum ChebyshevUTDemo implements WindowProvider {
  INSTANCE;

  @Override
  public Window getWindow() {
    LookAndFeels.autoDetect();
    List<Show> list = new LinkedList<>();
    for (Chebyshev chebyshev : Chebyshev.values()) {
      Show show = new Show();
      show.setPlotLabel("Chebyshev " + chebyshev);
      for (int d = 0; d < 5; ++d) {
        ScalarUnaryOperator suo = chebyshev.of(d);
        Showable showable2 = show.add(Plot.of(suo, Clips.absoluteOne()));
        showable2.setLabel("deg " + d);
      }
      list.add(show);
    }
    return ShowWindow.asDialog(list);
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
