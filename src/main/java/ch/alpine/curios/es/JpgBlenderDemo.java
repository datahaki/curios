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
import java.util.Random;
import java.util.random.RandomGenerator;

import javax.imageio.ImageIO;

import ch.alpine.bridge.gfx.GeometricComponent;
import ch.alpine.bridge.gfx.GeometricLayer;
import ch.alpine.bridge.gfx.RenderInterface;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

@ReflectionMarker
public class JpgBlenderDemo implements ManipulateProvider, RenderInterface {
  private static final File ROOT = //
      new File("/home/datahaki/Downloads/jpgblender");
  // ---
  private BufferedImage bufferedImage;
  @FieldSelectionCallback("files")
  public String s1 = "9fdc57df.jpg";
  @FieldSelectionCallback("files")
  public String s2 = "8c0c1941.jpg";
  @FieldSlider
  @FieldClip(min = "0", max = "100")
  public final Integer seed = 73;
  @FieldSlider
  @FieldClip(min = "0", max = "20")
  public Integer exp2 = 10;
  @FieldSlider
  @FieldClip(min = "0", max = "7")
  public Integer bit = 0;
  @FieldSlider
  @FieldClip(min = "0", max = "1")
  public Scalar ratio = RealScalar.of(0.5);

  @ReflectionMarker
  public List<String> files() {
    File[] files = ROOT.listFiles();
    return Arrays.stream(files).map(File::getName).toList();
  }

  private final GeometricComponent geometricComponent = new GeometricComponent();

  public JpgBlenderDemo() {
    geometricComponent.addRenderInterface(this);
  }

  @Override
  public Container getContainer() {
    stateChanged();
    return geometricComponent;
  }

  @Override
  public void render(GeometricLayer geometricLayer, Graphics2D graphics) {
    if (Objects.nonNull(bufferedImage))
      graphics.drawImage(bufferedImage, 0, 0, null);
  }

  public void stateChanged() {
    try {
      byte[] d1 = Files.readAllBytes(new File(ROOT, s1).toPath());
      byte[] d2 = Files.readAllBytes(new File(ROOT, s2).toPath());
      System.out.println("===");
      System.out.println(d1.length);
      System.out.println(d2.length);
      RandomGenerator random = new Random(seed);
      int min = Math.min(d1.length, d2.length);
      double exp = Math.exp(-exp2);
      double fac = exp / min;
      byte mask = (byte) (1 << bit);
      int half = (int) (min * ratio.number().doubleValue());
      System.out.println(half);
      for (int count = 1000; count < min; ++count) {
        double p = count * fac;
        if (random.nextDouble() < p) {
          if (count < half)
            d1[count] ^= d2[count] & mask;
          else
            d1[count] = d2[count];
        }
      }
      bufferedImage = ImageIO.read(new ByteArrayInputStream(d1));
    } catch (Exception exception) {
      System.err.println("give up");
    }
  }

  static void main() {
    new JpgBlenderDemo().runStandalone();
  }
}
