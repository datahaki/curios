// code by jph
package ch.alpine.curios.dev;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Gatherers;

import ch.alpine.bridge.pro.VoidProvider;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

enum DetectBulkDeactivation implements VoidProvider {
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

  @Override
  public Void runStandalone() {
    Path root = HomeDirectory.Projects.resolve();
    try {
      Files.find(root, Integer.MAX_VALUE, (_, bfa) -> !bfa.isDirectory()) //
          .forEach(this::visit);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return null;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
