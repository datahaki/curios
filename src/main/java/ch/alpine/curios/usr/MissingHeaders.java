// code by jph
package ch.alpine.curios.usr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

public enum MissingHeaders {
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
            Optional<String> optional = lines.findFirst();
            if (optional.isPresent()) {
              ++checked;
              String header = optional.orElseThrow();
              if (!header.startsWith("/")) {
                prepend(path);
              }
            } else {
              System.err.println("FILE EMPTY " + path);
            }
          }
        }
      }
    }
  }

  private void prepend(Path path) throws IOException {
    List<String> list = new LinkedList<>();
    list.add("// code by jph");
    try (var lines = Files.lines(path)) {
      lines.forEach(list::add);
    }
    Files.write(path, list);
    IO.println(path);
  }

  static void main() throws IOException {
    Path path = HomeDirectory.Projects.resolve();
    INSTANCE.visit(path);
    IO.println("checked: " + INSTANCE.checked);
  }
}
