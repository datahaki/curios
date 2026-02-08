// code by jph
package ch.alpine.ubongo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.ubongo.KaiserRun.Pair;

class KaiserRunTest {
  @Test
  void test() {
    long count = Pair.all().count();
    assertEquals(count, 12 * 31);
  }
}
