// code by jph
package ch.alpine.curios;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.awt.Dimension;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.io.TempDir;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.ext.ref.ImplementationDiscovery;

class ShowProviderTest implements Consumer<ShowProvider> {
  @TempDir
  Path tempDir;

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    ImplementationDiscovery<ShowProvider> classDiscUtils = new ImplementationDiscovery<>(ShowProvider.class);
    List<ShowProvider> list = classDiscUtils.getInstances("ch.alpine");
    assertFalse(list.isEmpty());
    return list.stream() //
        .map(instance -> DynamicTest.dynamicTest(instance.toString(), () -> accept(instance))) //
        .toList();
  }

  @Override
  public void accept(ShowProvider showProvider) {
    Show show = showProvider.getShow();
    Path file = tempDir.resolve(System.nanoTime() + ".png");
    try {
      show.export(file, new Dimension(800, 800));
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
