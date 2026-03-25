// code by jph
package ch.alpine.curios.dev;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Gatherers;

import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

enum DetectBulkDeactivation {
  INSTANCE;

  int checked = 0;

  public void visit(Path base) throws IOException {
    for (File file : base.toFile().listFiles()) {
      Path path = file.toPath();
      if (Files.isDirectory(path)) {
        visit(path);
      } else {
        PathName pathName = PathName.of(file.toPath());
        if (pathName.hasExtension("java")) {
          try (var lines = Files.lines(path)) {
            long count = lines.gather(Gatherers.windowSliding(3)) //
                .filter(l -> l.stream().map(String::trim).allMatch(s -> s.startsWith("//"))).count();
            if (10 < count)
              IO.println(count + " " + pathName.path());
          }
        }
      }
    }
  }

  static void main() throws IOException {
    Path path = HomeDirectory.Projects.resolve();
    INSTANCE.visit(path);
  }
}
