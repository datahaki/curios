// code by jph
package ch.alpine.subare.book.ch04.gambler;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ch.alpine.ascony.io.AnimatedGifWriter;
import ch.alpine.bridge.awt.AwtUtil;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.subare.alg.ActionValueIteration;
import ch.alpine.subare.util.DiscreteQsa;
import ch.alpine.subare.util.Infoline;
import ch.alpine.subare.util.gfx.StateActionRasters;
import ch.alpine.tensor.io.ImageFormat;

/** action value iteration for gambler's dilemma
 * 
 * visualizes each pass of the action value iteration */
@ReflectionMarker
enum AVI_GamblerAnimation implements ManipulateProvider {
  INSTANCE;

  private final JLabel jLabel;

  private AVI_GamblerAnimation() {
    GamblerModel gamblerModel = GamblerModel.createDefault();
    final DiscreteQsa ref = GamblerHelper.getOptimalQsa(gamblerModel);
    ActionValueIteration avi = ActionValueIteration.of(gamblerModel);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (AnimatedGifWriter animationWriter = //
        AnimatedGifWriter.of(baos, 500, true)) {
      for (int index = 0; index < 13; ++index) {
        DiscreteQsa qsa = avi.qsa();
        Infoline.print(gamblerModel, index, ref, qsa);
        BufferedImage bufferedImage = ImageFormat.of(StateActionRasters.qsaPolicyRef(new GamblerRaster(gamblerModel), qsa, ref));
        animationWriter.write(bufferedImage);
        avi.step();
      }
      animationWriter.write(ImageFormat.of(StateActionRasters.qsaPolicyRef(new GamblerRaster(gamblerModel), avi.qsa(), ref)));
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
