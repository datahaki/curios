// code by jph
package ch.alpine.curios.fig;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.num.ShannonEntropy;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class ShannonEntropyDemo implements ShowProvider {
  @Override
  public Show getShow() {
    Show show = new Show();
    Clip clip = Clips.unit();
    show.add(Plot.of(ShannonEntropy.BITS, clip)).setLabel("bits");
    show.add(Plot.of(ShannonEntropy.NATS, clip)).setLabel("nats");
    show.setAspectRatioOne();
    return show;
  }

  static void main() {
    new ShannonEntropyDemo().runStandalone();
  }
}
