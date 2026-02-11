// code by jph
package ch.alpine.curios;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.tensor.ext.HomeDirectory;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

class ManipulateProviderTest {
  @TempDir
  Path tempDir;

  static Collection<Class<?>> manipulateProviders() {
    List<Class<?>> list = new LinkedList<>();
    try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages("ch") //
        .scan()) {
      scanResult.getClassesImplementing(ManipulateProvider.class.getName()) //
          .loadClasses() //
          .forEach(list::add);
    }
    return list;
  }

  @ParameterizedTest
  @MethodSource("manipulateProviders")
  void testWindow(Class<?> cls) {
    // TODO need to catch Them All
    if (cls.isEnum()) {
      for (Object object : cls.getEnumConstants()) {
        Enum<?> enm = (Enum<?>) object;
        _check((ManipulateProvider) object, tempDir, cls.getSimpleName() + "_" + enm.name());
      }
    } else //
    if (cls.isInterface()) {
    } else //
    if (cls.isRecord()) {
    } else //
    if (cls.isAnonymousClass()) {
    } else //
    {
      Constructor<?> constructor = null;
      try {
        constructor = cls.getDeclaredConstructor();
      } catch (Exception e) {
      }
      if (Objects.nonNull(constructor)) {
        constructor.setAccessible(true);
        Object object = null;
        try {
          object = constructor.newInstance();
        } catch (Exception e) {
        }
        if (Objects.nonNull(object))
          _check((ManipulateProvider) object, tempDir, cls.getSimpleName());
      }
    }
  }

  public static void _check(ManipulateProvider showProvider, Path tempDir, String string) {
    JComponent jComponent = showProvider.getJComponent();
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
