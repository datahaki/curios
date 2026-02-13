// code by jph
package ch.alpine.curios.sea;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;
import ch.alpine.tensor.ext.ReadLine;
import ch.alpine.tensor.io.StringScalarQ;
import ch.alpine.tensor.red.ScalarSummaryStatistics;

class SeaImport {
  static void main() throws IOException {
    Path folder = HomeDirectory.Downloads.resolve("ELC_INSPIRE/xyz_data_utm32N_Northsea");
    Path fout = HomeDirectory.Ephemeral.resolve(folder.getFileName() + ".csv");
    ScalarSummaryStatistics sx = new ScalarSummaryStatistics();
    ScalarSummaryStatistics sy = new ScalarSummaryStatistics();
    ScalarSummaryStatistics sz = new ScalarSummaryStatistics();
    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(fout)) {
      for (Path file : Files.list(folder).toList()) {
        boolean ext = PathName.of(file).hasExtension("xyz");
        if (ext) {
          System.out.println(file.getFileName());
          List<String> lines = new ArrayList<>();
          try (InputStream inputStream = Files.newInputStream(file)) {
            lines = ReadLine.of(inputStream).toList();
          }
          for (String line : lines) {
            String[] splits = line.split(";");
            try {
              Scalar x = Scalars.fromString(splits[0]);
              Scalar y = Scalars.fromString(splits[1]);
              Scalar z = Scalars.fromString(splits[2]);
              Tensor row = Tensors.of(x, y, z);
              if (StringScalarQ.any(row)) {
                System.err.println(line);
              } else {
                sx.accept(x);
                sy.accept(y);
                sz.accept(z);
                bufferedWriter.append(x + "," + y + "," + z);
                bufferedWriter.newLine();
              }
            } catch (Exception e) {
              System.err.println(line);
            }
          }
        }
      }
    }
    System.out.println(sx);
    System.out.println(sy);
    System.out.println(sz);
  }
}
