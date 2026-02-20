// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.exp.DLogisticSigmoid;
import ch.alpine.tensor.sca.exp.LogisticSigmoid;

class LogisticSigmoidDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Show show = new Show();
    Clip clip = Clips.absoluteOne();
    show.add(Plot.of(LogisticSigmoid.FUNCTION, clip));
    show.add(Plot.of(DLogisticSigmoid.FUNCTION, clip));
    return show;
  }

  static void main() {
    new LogisticSigmoidDemo().runStandalone();
  }
}
