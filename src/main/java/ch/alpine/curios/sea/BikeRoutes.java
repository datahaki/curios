// code by jph
package ch.alpine.curios.sea;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.ReadLine;

public class BikeRoutes {
  static void main() throws IOException {
    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(HomeDirectory.Ephemeral.resolve("asdf.html"))) {
      bufferedWriter.append("<table border=0>\n");
      InputStream fileInputStream = Files.newInputStream(HomeDirectory.Documents.resolve("2024_routes.slk"));
      List<String> list = //
          ReadLine.of(fileInputStream).filter(s -> s.startsWith("C;X")).collect(Collectors.toList());
      BikeEntry bikeEntry = new BikeEntry();
      for (String line : list) {
        String[] splits = line.split(";");
        int x = Integer.parseInt(splits[1].substring(1));
        int y = Integer.parseInt(splits[2].substring(1));
        if (x == 1) {
          if (bikeEntry.isComplete()) {
            bufferedWriter.append("<tr>");
            bufferedWriter.append("<td>");
            bufferedWriter.append(bikeEntry.location);
            bufferedWriter.append("<td>");
            bufferedWriter.append("" + bikeEntry.km);
            bufferedWriter.append("<td>");
            bufferedWriter.append("" + bikeEntry.incr.add(bikeEntry.decr));
            bufferedWriter.append("<td>");
            bufferedWriter.append(bikeEntry.gps0);
            bufferedWriter.append("<td>");
            bufferedWriter.append(bikeEntry.gps1);
            bufferedWriter.append("<td><a href='");
            bufferedWriter.append(bikeEntry.link);
            bufferedWriter.append("'>map</a>");
            bufferedWriter.append("</tr>\n");
          }
          bikeEntry = new BikeEntry();
        }
        String entry = splits[3].substring(1);
        if (entry.startsWith("\"") && entry.endsWith("\""))
          entry = entry.substring(1, entry.length() - 1);
        // System.out.println(x + "," + y + " " + entry);
        switch (x) {
        case 1: {
          bikeEntry.date = entry;
          break;
        }
        case 2: {
          bikeEntry.location = entry;
          break;
        }
        case 3: {
          bikeEntry.km = Scalars.fromString(entry);
          break;
        }
        case 4: {
          bikeEntry.incr = Scalars.fromString(entry);
          break;
        }
        case 5: {
          bikeEntry.decr = Scalars.fromString(entry);
          break;
        }
        case 6: {
          bikeEntry.gps0 = entry;
          break;
        }
        case 7: {
          bikeEntry.gps1 = entry;
          break;
        }
        case 8: {
          bikeEntry.link = entry;
          break;
        }
        default:
        }
      }
    }
  }
}
