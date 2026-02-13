// code by jph
package ch.alpine.curios.usr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.ReadLine;

enum TestSpeed {
  ;
  static void main() throws FileNotFoundException, IOException {
    Path file = HomeDirectory.Ephemeral.resolve("Projects", "log.txt");
    Map<String, Scalar> map = new HashMap<>();
    try (InputStream inputStream = Files.newInputStream(file)) {
      List<String> list = ReadLine.of(inputStream).filter(s -> s.startsWith("[INFO] Tests run: ")).toList();
      for (String line : list) {
        int index = line.indexOf("Time elapsed:");
        String rest = line.substring(index + 13);
        // System.out.println(rest);
        StringTokenizer stringTokenizer = new StringTokenizer(rest);
        Scalar time = Scalars.fromString(stringTokenizer.nextToken());
        String unit = stringTokenizer.nextToken(); // s
        if (!unit.equals("s"))
          throw new IllegalArgumentException(unit);
        stringTokenizer.nextToken(); // --
        stringTokenizer.nextToken(); // in
        String name = stringTokenizer.nextToken();
        map.put(name, time);
      }
    }
    List<String> all = map.keySet().stream().sorted((n1, n2) -> Scalars.compare(map.get(n1), map.get(n2))).toList();
    for (String name : all) {
      System.out.println(name + " " + map.get(name));
    }
  }
}
