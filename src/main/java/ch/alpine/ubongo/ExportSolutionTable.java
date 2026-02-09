package ch.alpine.ubongo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.Month;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Get;
import ch.alpine.ubongo.gui.UbongoRender;

enum ExportSolutionTable {
  ;
  static void main() throws IOException {
    Tensor tensor = Get.of(HomeDirectory.path("kaiser.mathematica"));
    Path path = HomeDirectory.Documents.resolve("kaiser5x10.txt");
    try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8))) {
      for (Month month : Month.values())
        for (int day = 1; day <= 31; day++)
          for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            Tensor entry = tensor.get(month.ordinal(), day - 1, dayOfWeek.ordinal());
            if (entry instanceof Scalar) {
              IO.println("missing!");
            } else {
              String string = month.toString().substring(0, 3) //
                  + String.format(" %02d ", day) //
                  + dayOfWeek.toString().substring(0, 3) + " " + UbongoRender.stringMatrix(entry);
              // IO.println(string);
              printWriter.println(string);
            }
          }
    }
  }
}
