// code by jph
package ch.alpine.curios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.bridge.cgr.ClassDiscovery;
import ch.alpine.bridge.cgr.ClassPaths;
import ch.alpine.bridge.ref.util.ClassFieldCheck;
import ch.alpine.bridge.ref.util.FieldValueRecord;

class ReflectionMarkerTest {
  @Test
  void test() throws Exception {
    ClassFieldCheck classFieldCheck = new ClassFieldCheck();
    ClassDiscovery.execute(ClassPaths.getDefault(), classFieldCheck);
    // IO.println(classFieldCheck.getInspected().size());
    assertTrue(40 < classFieldCheck.getInspected().size());
    for (Class<?> cls : classFieldCheck.getFailures())
      IO.println(cls);
    assertTrue(classFieldCheck.getFailures().isEmpty());
    // ---
    for (FieldValueRecord fvc : classFieldCheck.invalidFields())
      IO.println(fvc);
    assertTrue(classFieldCheck.invalidFields().isEmpty());
  }
}
