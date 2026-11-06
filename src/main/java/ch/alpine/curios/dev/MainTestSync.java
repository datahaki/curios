// code by jph
package ch.alpine.curios.dev;

import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

import ch.alpine.bridge.io.FileNames;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

class MainTestSync {
  static class JavaIndex {
    private final Path root;
    private final Set<String> set = new TreeSet<>();

    public JavaIndex(Path root) {
      this.root = root;
      FileNames.all(root) //
          .filter(path -> PathName.of(path).hasExtension("java")) //
          .forEach(this::visit);
    }

    public void visit(Path path) {
      PathName pathName = PathName.of(path);
      if (pathName.hasExtension("java")) {
        pathName = PathName.of(root.relativize(path));
        set.add(pathName.truncate().path().toString());
      }
    }
  }

  private final JavaIndex javaIndexMain;
  private final JavaIndex javaIndexTest;

  public MainTestSync(Path base) {
    javaIndexMain = new JavaIndex(base.resolve("src", "main"));
    javaIndexTest = new JavaIndex(base.resolve("src", "test"));
  }

  void showMissingTests() {
    for (String rel : javaIndexMain.set) {
      if (!javaIndexTest.set.contains(rel + "Test"))
        IO.println(rel);
    }
  }

  void showUnassocTests() {
    for (String rel : javaIndexTest.set) {
      if (rel.endsWith("Test")) {
        String end = rel.substring(0, rel.length() - 4);
        if (!javaIndexMain.set.contains(end) && !javaIndexTest.set.contains(end))
          IO.println(rel);
      }
    }
  }

  static void main() {
    // String default1 = ClassPaths.getDefault();
    // IO.println(default1);
    // ClassDiscovery.execute(ClassPaths.getDefault(), new ClassVisitor() {
    // @Override
    // public void accept(String jarfile, Class<?> cls) {
    // IO.println(cls);
    // }
    // });
    Path path = HomeDirectory.Projects.resolve("tensor");
    MainTestSync mainTestSync = new MainTestSync(path);
    // mainTestSync.showMissingTests();
    mainTestSync.showUnassocTests();
  }
}
