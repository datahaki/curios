// code by jph
package ch.alpine.curios.usr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;

class SpecialContentTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    SpecialContent sc = new SpecialContent();
    SpecialContent cp = Serialization.copy(sc);
    assertEquals(cp.value, Tensors.vector(1, 2, 3));
    assertEquals(cp.handled, Tensors.vector(99, 100));
  }
}
