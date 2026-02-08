// code by jph
package ch.alpine.curios.sbf;

import java.io.IOException;
import java.nio.file.Path;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.io.Get;
import ch.alpine.tensor.io.Put;

class SbfTrack {
  private final Path local;
  public Tensor tensor;

  public SbfTrack(SbfType sbfType, int fallback) {
    local = SbfParser.ROOT.resolve(sbfType + ".tensor");
    try {
      tensor = Get.of(local);
    } catch (IOException e) {
      tensor = Array.zeros(fallback, 0);
    }
  }

  public void store() {
    try {
      Put.of(local, tensor);
      System.out.println("stored");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
