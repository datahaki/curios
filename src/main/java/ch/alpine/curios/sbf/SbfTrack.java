// code by jph
package ch.alpine.curios.sbf;

import java.io.IOException;
import java.nio.file.Path;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.io.MathematicaFormat;

class SbfTrack {
  private final Path local;
  public Tensor tensor;

  public SbfTrack(SbfType sbfType, int fallback) {
    local = SbfParser.ROOT.resolve(sbfType + ".tensor");
    try {
      tensor = MathematicaFormat.Get.of(local);
    } catch (IOException ioException) {
      tensor = Array.zeros(fallback, 0);
    }
  }

  public void store() {
    try {
      MathematicaFormat.Put.of(local, tensor);
      System.out.println("stored");
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }
}
