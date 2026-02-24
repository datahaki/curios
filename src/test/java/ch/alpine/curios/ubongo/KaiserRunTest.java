// code by jph
package ch.alpine.curios.ubongo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.curios.ubongo.KaiserRun.Pair;

class KaiserRunTest {
  @Test
  void test() {
    long count = Pair.all().count();
    assertEquals(count, 12 * 31);
  }
}
