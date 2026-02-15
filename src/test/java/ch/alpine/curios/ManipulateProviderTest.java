// code by jph
package ch.alpine.curios;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.io.TempDir;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.tensor.ext.HomeDirectory;

class ManipulateProviderTest implements Consumer<ManipulateProvider> {
  @TempDir
  Path tempDir;

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    List<ManipulateProvider> list = new ClassGraphUtils<>(ManipulateProvider.class).getInstances("ch");
    assertFalse(list.isEmpty());
    return list.stream() //
        .map(instance -> DynamicTest.dynamicTest(instance.toString(), () -> accept(instance))) //
        .toList();
  }

  @Override
  public void accept(ManipulateProvider manipulateProvider) {
    Container jComponent = manipulateProvider.getContainer();
    jComponent.setSize(800, 800);
    jComponent.doLayout(); // mandatory
    int width = jComponent.getWidth();
    int height = jComponent.getHeight();
    if (width == 0 || height == 0) {
      throw new IllegalStateException("Component must have a size");
    }
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = bufferedImage.createGraphics();
    jComponent.printAll(graphics);
    graphics.dispose();
    if (false)
      try {
        ImageIO.write(bufferedImage, "png", HomeDirectory.Pictures.resolve("" + System.nanoTime() + ".png").toFile());
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
  }
}
