// code by jph
package ch.alpine.curios.dev;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

enum RemPackagePrefix {
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
            boolean anyMatch = lines.map(String::trim).anyMatch(s -> s.startsWith("/* package */"));
            if (anyMatch) {
              prepend(path);
            }
          }
        }
      }
    }
  }

  private void prepend(Path path) throws IOException {
    List<String> list = new LinkedList<>();
    try (var lines = Files.lines(path)) {
      for (String line : lines.toList()) {
        int index = line.indexOf("/* package */");
        if (0 <= index) {
          String resu = line.substring(0, index) + line.substring(index + 13);
          list.add(resu);
        } else
          list.add(line);
      }
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
