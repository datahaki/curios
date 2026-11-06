// code by jph
package ch.alpine.curios.dev;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import ch.alpine.bridge.io.FileNames;
import ch.alpine.bridge.pro.VoidProvider;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

enum DuplicateNames implements VoidProvider {
  INSTANCE;

  final Set<String> ignore = new HashSet<>();
  final Set<String> set = new HashSet<>();

  private DuplicateNames() {
    ignore.add("package-info");
    ignore.add("StaticHelper");
    ignore.add("TestHelper");
    ignore.add("StaticHelperTest");
    // LocalLaunchPad
    ignore.add("LocalLaunchPad");
    ignore.add("RunProviderTest");
  }

  public void visit(Path path) {
    PathName pathName = PathName.of(path);
    String title = pathName.title();
    if (!ignore.contains(title) && !set.add(title)) {
      IO.println(title);
    }
  }

  @Override
  public Void runStandalone() {
    Path root = HomeDirectory.Projects.resolve();
    FileNames.all(root) //
        .filter(path -> PathName.of(path).hasExtension("java")) //
        .forEach(this::visit);
    return null;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
