// code by jph
// inspired by Shangtong Zhang
package ch.alpine.subare.book.ch08.maze;

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
import ch.alpine.subare.util.gfx.StateRasters;
import ch.alpine.tensor.io.ImageFormat;

/** action value iteration for cliff walk */
@ReflectionMarker
enum AVH_Dynamaze implements ManipulateProvider {
  START_0,
  START_1,
  START_2;

  private final JLabel jLabel;

  AVH_Dynamaze() {
    Dynamaze dynamaze = DynamazeHelper.create5(ordinal());
    DiscreteQsa est = DynamazeHeuristic.create(dynamaze);
    // est = DiscreteQsa.build(dynamaze);
    ActionValueIteration avi = ActionValueIteration.of(dynamaze, est);
    // ---
    DiscreteQsa ref = DynamazeHelper.getOptimalQsa(dynamaze);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (AnimatedGifWriter animatedGifWriter = AnimatedGifWriter.of(baos, 500, true)) {
      DynamazeRaster dynamazeRaster = new DynamazeRaster(dynamaze);
      for (int index = 0; index < 50; ++index) {
        Infoline infoline = Infoline.print(dynamaze, index, ref, avi.qsa());
        BufferedImage bufferedImage = ImageFormat.of(StateRasters.qsaLossRef(dynamazeRaster, avi.qsa(), ref));
        animatedGifWriter.write(bufferedImage);
        avi.step();
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

  static void main() throws Exception {
    // create("maze2", DynamazeHelper.original("maze2"));
    START_0.runStandalone();
  }
}
