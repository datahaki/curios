// code by jph
package ch.alpine.curios.dev;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Gatherers;

import ch.alpine.bridge.io.FileNames;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

enum DetectBulkDeactivation {
  INSTANCE;

  public void visit(Path path) {
    PathName pathName = PathName.of(path);
    if (pathName.hasExtension("java"))
      try {
        try (var lines = Files.lines(path)) {
          long count = lines.gather(Gatherers.windowSliding(3)) //
              .filter(l -> l.stream().map(String::trim).allMatch(s -> s.startsWith("//"))).count();
          if (10 < count)
            IO.println(count + " " + pathName.path());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  static void main() {
    Path root = HomeDirectory.Projects.resolve();
    FileNames.all(root).forEach(INSTANCE::visit);
  }
}
