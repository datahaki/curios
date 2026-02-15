// code by jph
package ch.alpine.curios;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.io.TempDir;

import ch.alpine.bridge.pro.RunProvider;

class RunProviderTest implements Consumer<RunProvider> {
  @TempDir
  Path tempDir;

  @TestFactory
  Collection<DynamicTest> dynamicTests() {
    List<RunProvider> list = new ClassGraphUtils<>(RunProvider.class).getInstances("ch");
    assertFalse(list.isEmpty());
    return list.stream() //
        .map(instance -> DynamicTest.dynamicTest(instance.toString(), () -> accept(instance))) //
        .toList();
  }

  @Override
  public void accept(RunProvider manipulateProvider) {
    manipulateProvider.run();
  }
}
