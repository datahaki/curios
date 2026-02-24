// code by jph
package ch.alpine.curios.dev;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import ch.alpine.tensor.ext.HomeDirectory;

enum GroupId {
  INSTANCE;

  private static final String OLD = "<groupId>ch.alpine</groupId>";
  private static final String NEW = "<groupId>io.github.datahaki</groupId>";

  public void visit(Path base) throws IOException {
    for (File file : base.toFile().listFiles()) {
      Path path = file.toPath();
      Path pom = path.resolve("pom.xml");
      if (Files.isDirectory(path) && Files.isRegularFile(pom))
        visit_pom(pom);
    }
  }

  void visit_pom(Path path) throws IOException {
    List<String> list = Files.lines(path).collect(Collectors.toList());
    boolean isDirty = false;
    for (int index = 0; index < list.size(); ++index) {
      String line = list.get(index);
      if (line.contains(OLD)) {
        String replace = line.replace(OLD, NEW);
        list.set(index, replace);
        isDirty = true;
      }
    }
    if (isDirty) {
      IO.println(path);
      Files.write(path, list);
    }
  }

  static void main() throws IOException {
    Path path = HomeDirectory.Projects.resolve();
    INSTANCE.visit(path);
  }
}
