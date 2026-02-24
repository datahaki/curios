// code by jph
package ch.alpine.subare.book.ch05.wireloop;

import java.awt.Container;
import java.io.ByteArrayOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ch.alpine.ascony.io.AnimatedGifWriter;
import ch.alpine.bridge.awt.AwtUtil;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.subare.alg.Random1StepTabularQPlanning;
import ch.alpine.subare.util.ConstantLearningRate;
import ch.alpine.subare.util.DiscreteQsa;
import ch.alpine.subare.util.Infoline;
import ch.alpine.subare.util.TabularSteps;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.io.ImageFormat;

/** Example 4.1, p.82 */
@ReflectionMarker
enum RSTQP_Wireloop implements ManipulateProvider {
  INSTANCE;

  private final JLabel jLabel;

  RSTQP_Wireloop() {
    String name = "wire5";
    WireloopReward wireloopReward = WireloopReward.freeSteps();
    wireloopReward = WireloopReward.constantCost();
    Wireloop wireloop = WireloopHelper.create(name, WireloopReward::id_x, wireloopReward);
    WireloopRaster wireloopRaster = new WireloopRaster(wireloop);
    DiscreteQsa ref = WireloopHelper.getOptimalQsa(wireloop);
    DiscreteQsa qsa = DiscreteQsa.build(wireloop);
    Random1StepTabularQPlanning rstqp = Random1StepTabularQPlanning.of( //
        wireloop, qsa, ConstantLearningRate.of(RealScalar.ONE));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (AnimatedGifWriter animationWriter = AnimatedGifWriter.of(baos, 250, true)) {
      int batches = 50;
      for (int index = 0; index < batches; ++index) {
        Infoline infoline = Infoline.print(wireloop, index, ref, qsa);
        TabularSteps.batch(wireloop, wireloop, rstqp);
        animationWriter.write(ImageFormat.of(WireloopHelper.render(wireloopRaster, ref, qsa)));
        if (infoline.isLossfree())
          break;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    ImageIcon imageIcon = new ImageIcon(baos.toByteArray());
    jLabel = AwtUtil.iconAsLabel(imageIcon);
  }

  @Override
  public Container getContainer() {
    return jLabel;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
