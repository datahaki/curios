// code by jph
package ch.alpine.curios.dev;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ch.alpine.tensor.ext.HomeDirectory;

enum DepMaintenance {
  INSTANCE;

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
    List<Dep> updated = new LinkedList<>();
    for (Dep dep : Dep.values())
      for (int index = 0; index < list.size(); ++index) {
        if (dep.matchGroupId(list.get(index + 0)) && //
            dep.matchArtifactId(list.get(index + 1))) {
          int version_pos = index + 2;
          String versionTag = list.get(version_pos);
          if (dep.containsVersion(versionTag)) {
            if (!dep.matchesVersion(versionTag)) {
              String replace = versionTag.replace(versionTag.trim(), dep.version());
              list.set(version_pos, replace);
              updated.add(dep);
            }
          } else
            throw new IllegalStateException();
        }
      }
    if (!updated.isEmpty()) {
      IO.println(path);
      updated.forEach(IO::println);
      Files.write(path, list);
    }
  }

  static void main() throws IOException {
    Path path = HomeDirectory.Projects.resolve();
    INSTANCE.visit(path);
  }
}
