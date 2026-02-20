// code by jph
package ch.alpine.curios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import ch.alpine.bridge.pro.RunProvider;
import ch.alpine.tensor.ext.ref.InstanceDiscovery;

class RunProviderTest implements Consumer<RunProvider> {
  private static final AtomicInteger COUNT = new AtomicInteger();

  @TestFactory
  Stream<DynamicTest> dynamicTests() {
    return InstanceDiscovery.of("ch.alpine", RunProvider.class).stream() //
        .map(Supplier::get) //
        .map(instance -> DynamicTest.dynamicTest(instance.toString(), () -> accept(instance)));
  }

  @Override
  public void accept(RunProvider manipulateProvider) {
    manipulateProvider.runStandalone();
    COUNT.getAndIncrement();
  }

  @AfterAll
  static void here() {
    assertTrue(5 <= COUNT.get());
  }
}
