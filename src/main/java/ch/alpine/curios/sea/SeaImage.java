// code by jph
package ch.alpine.curios.sea;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Export;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.opt.nd.CoordinateBounds;
import ch.alpine.tensor.sca.Ceiling;
import ch.alpine.tensor.sca.Floor;
import ch.alpine.tensor.sca.Ramp;

class SeaImage {
  static void main() throws IOException {
    Path file = HomeDirectory.Public.resolve("xyz_data_utm32N_Northsea.csv");
    try (BufferedReader bufferedReader = Files.newBufferedReader(file)) {
      CoordinateBoundingBox cbb = CoordinateBounds.of( //
          Tensors.vector(147075.0, 5901725.0, -68.79), //
          Tensors.vector(521975.0, 6210375.0, 13.96));
      System.out.println(cbb);
      Scalar factor = RationalScalar.of(1, 50);
      Scalar min_x = cbb.clip(0).min();
      Scalar min_y = cbb.clip(1).min();
      int wx = Ceiling.intValueExact(cbb.clip(0).width().multiply(factor));
      int wy = Ceiling.intValueExact(cbb.clip(1).width().multiply(factor));
      Scalar zbuf = RealScalar.of(255).divide(cbb.clip(2).min().negate());
      Tensor zeros = Array.zeros(wx + 1, wy + 1);
      System.out.println(wx + " " + wy);
      while (true) {
        String line = bufferedReader.readLine();
        if (line == null)
          break;
        Tensor row = Tensors.fromString("{" + line + "}");
        int x = Floor.intValueExact(row.Get(0).subtract(min_x).multiply(factor));
        int y = Floor.intValueExact(row.Get(1).subtract(min_y).multiply(factor));
        zeros.set(Ramp.FUNCTION.apply(row.Get(2).negate()).multiply(zbuf), x, y);
      }
      Export.of(HomeDirectory.Pictures.resolve("northsea.png"), zeros);
    }
  }
}
