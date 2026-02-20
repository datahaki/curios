// code by jph
package ch.alpine.curios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.ref.InstanceDiscovery;

class ManipulateProviderTest implements Consumer<ManipulateProvider> {
  private static final AtomicInteger COUNT = new AtomicInteger();

  @TestFactory
  Stream<DynamicTest> dynamicTests() {
    return InstanceDiscovery.of("ch.alpine", ManipulateProvider.class).stream() //
        .map(Supplier::get) //
        .map(instance -> DynamicTest.dynamicTest(instance.toString(), () -> accept(instance)));
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
    COUNT.getAndIncrement();
  }

  @AfterAll
  static void here() {
    assertTrue(22 <= COUNT.get());
  }
}
