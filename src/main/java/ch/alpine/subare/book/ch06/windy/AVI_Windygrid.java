// code by jph
// inspired by Shangtong Zhang
package ch.alpine.subare.book.ch06.windy;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ch.alpine.bridge.awt.AwtUtil;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.subare.alg.ActionValueIteration;
import ch.alpine.subare.api.Policy;
import ch.alpine.subare.util.DiscreteQsa;
import ch.alpine.subare.util.DiscreteUtils;
import ch.alpine.subare.util.DiscreteVs;
import ch.alpine.subare.util.Infoline;
import ch.alpine.subare.util.Policies;
import ch.alpine.subare.util.PolicyType;
import ch.alpine.subare.util.gfx.StateActionRasters;
import ch.alpine.tensor.ext.AnimatedGifWriter;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Export;
import ch.alpine.tensor.io.ImageFormat;

/** action value iteration for cliff walk */
enum AVI_Windygrid implements ManipulateProvider {
  INSANCE;

  private final JLabel jLabel;

  private AVI_Windygrid() {
    Windygrid windygrid = Windygrid.createFour();
    WindygridRaster windygridRaster = new WindygridRaster(windygrid);
    DiscreteQsa ref = WindygridHelper.getOptimalQsa(windygrid);
    ActionValueIteration avi = ActionValueIteration.of(windygrid);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (AnimatedGifWriter animationWriter = //
        AnimatedGifWriter.of(baos, 250, true)) {
      Export.of(HomeDirectory.Pictures.resolve("windygrid_qsa_avi.png"), //
          StateActionRasters.qsa_rescaled(windygridRaster, ref));
      for (int index = 0; index < 20; ++index) {
        Infoline infoline = Infoline.print(windygrid, index, ref, avi.qsa());
        BufferedImage bufferedImage = ImageFormat.of(StateActionRasters.qsaLossRef(windygridRaster, avi.qsa(), ref));
        animationWriter.write(bufferedImage);
        avi.step();
        if (infoline.isLossfree())
          break;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    ImageIcon imageIcon = new ImageIcon(baos.toByteArray());
    // TODO SUBARE extract code below to other file
    DiscreteVs vs = DiscreteUtils.createVs(windygrid, ref);
    DiscreteUtils.print(vs);
    Policy policy = PolicyType.GREEDY.bestEquiprobable(windygrid, ref, null);
    Policies.print(policy, windygrid.states());
    jLabel = AwtUtil.iconAsLabel(imageIcon);
  }

  @Override
  public Container getContainer() {
    return jLabel;
  }

  static void main() {
    INSANCE.runStandalone();
  }
}
