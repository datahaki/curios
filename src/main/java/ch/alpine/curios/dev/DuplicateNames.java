// code by jph
package ch.alpine.curios.dev;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

enum DuplicateNames {
  INSTANCE;

  final Set<String> ignore = new HashSet<>();
  final Set<String> set = new HashSet<>();

  private DuplicateNames() {
    ignore.add("StaticHelper");
    ignore.add("TestHelper");
    ignore.add("StaticHelperTest");
  }

  public void visit(Path base) throws IOException {
    for (File file : base.toFile().listFiles()) {
      Path path = file.toPath();
      if (Files.isDirectory(path)) {
        visit(path);
      } else {
        PathName pathName = PathName.of(file.toPath());
        if (pathName.hasExtension("java")) {
          String title = pathName.title();
          if (!ignore.contains(title) && !set.add(title)) {
            IO.println(title);
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
