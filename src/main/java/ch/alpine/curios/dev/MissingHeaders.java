// code by jph
package ch.alpine.curios.dev;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import ch.alpine.bridge.pro.VoidProvider;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

enum MissingHeaders implements VoidProvider {
  INSTANCE;

  int checked = 0;

  public void visit(Path path) {
    PathName pathName = PathName.of(path);
    if (pathName.hasExtension("java"))
      try {
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
      } catch (Exception e) {
        e.printStackTrace();
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

  @Override
  public Void runStandalone() {
    Path path = HomeDirectory.Projects.resolve();
    try {
      Files.find(path, Integer.MAX_VALUE, (_, bfa) -> !bfa.isDirectory()) //
          .forEach(this::visit);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    IO.println("checked: " + checked);
    return null;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
