// code by jph
package ch.alpine.curios.sea;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Import;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.opt.nd.CoordinateBounds;
import ch.alpine.tensor.sca.Floor;

public class SeaClip {
  static void main() throws IOException {
    Tensor tensor = Import.of(HomeDirectory.path("xyz_data_utm32N_Baltic_sea.csv"));
    {
      CoordinateBoundingBox cbb = CoordinateBounds.of(tensor);
      System.out.println(cbb);
      Scalar factor = RationalScalar.of(1, 50);
      Scalar min_x = cbb.clip(0).min();
      Scalar min_y = cbb.clip(1).min();
      Path fout = HomeDirectory.path("baltic.csv");
      BufferedWriter bufferedWriter = Files.newBufferedWriter(fout);
      for (Tensor row : tensor) {
        int x = Floor.intValueExact(row.Get(0).subtract(min_x).multiply(factor));
        int y = Floor.intValueExact(row.Get(1).subtract(min_y).multiply(factor));
        bufferedWriter.append(x + "," + y + "," + row.Get(2));
        bufferedWriter.newLine();
      }
      bufferedWriter.close();
    }
  }
}
