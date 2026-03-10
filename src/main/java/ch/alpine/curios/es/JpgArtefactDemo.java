// code by jph
package ch.alpine.curios.es;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import ch.alpine.bridge.gfx.GeometricComponent;
import ch.alpine.bridge.gfx.GeometricLayer;
import ch.alpine.bridge.gfx.RenderInterface;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.ext.HomeDirectory;

@ReflectionMarker
public class JpgArtefactDemo implements ManipulateProvider, RenderInterface {
  private static final File ROOT = HomeDirectory.Pictures.resolve("").toFile();
  // ---
  private BufferedImage bufferedImage;
  @FieldSelectionCallback("files")
  public String string = "";
  @FieldSlider
  @FieldClip(min = "0", max = "1")
  public Scalar ratio = Rational.HALF;
  @FieldSlider
  @FieldClip(min = "0", max = "100")
  public Integer len = 50;
  @FieldSlider
  @FieldClip(min = "0", max = "100")
  public Integer step = 73;
  @FieldSlider
  @FieldClip(min = "0", max = "100")
  public Integer val2 = 0;

  @ReflectionMarker
  public List<String> files() {
    File[] files = ROOT.listFiles();
    return Arrays.stream(files).map(File::getName).toList();
  }

  private final GeometricComponent geometricComponent = new GeometricComponent();

  public JpgArtefactDemo() {
    geometricComponent.addRenderInterface(this);
  }

  @Override
  public void render(GeometricLayer geometricLayer, Graphics2D graphics) {
    if (Objects.nonNull(bufferedImage))
      graphics.drawImage(bufferedImage, 0, 0, null);
  }

  public void stateChanged() {
    try {
      File file = new File(ROOT, string);
      byte[] data = Files.readAllBytes(file.toPath());
      int offset = (int) (data.length * (ratio.number().doubleValue()));
      byte val = val2.byteValue();
      for (int count = 0; count < len; ++count) {
        int index = offset + step * count;
        if (index < data.length)
          data[index] = val;
      }
      bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
    } catch (Exception exception) {
      // System.err.println("give up");
    }
  }

  @Override
  public Container getContainer() {
    stateChanged();
    return geometricComponent;
  }

  static void main() {
    new JpgArtefactDemo().runStandalone();
  }
}
