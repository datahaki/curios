// code by jph
package ch.alpine.curios.usr;

import java.io.IOException;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Export;

/* package */ enum ExportGifDemo {
  ;
  static void main() throws IOException {
    Tensor matrix = Tensors.matrix((i, j) -> Tensors.vector(255 - i, j, 0, j < 128 ? 255 : i), 256, 256);
    Export.of(HomeDirectory.Pictures.resolve("redgreen.gif"), matrix);
    Export.of(HomeDirectory.Pictures.resolve("redgreen.png"), matrix);
    // TODO BRIDGE reread images and then plot
  }
}
