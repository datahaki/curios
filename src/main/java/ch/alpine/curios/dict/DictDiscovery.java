// code by jph
package ch.alpine.curios.dict;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.tensor.ext.HomeDirectory;

class DictDiscovery {
  public static DictCollection of(Path root) {
    return new DictCollection(new DictDiscovery(root).list);
  }

  private final List<Dict> list = new LinkedList<>();

  private DictDiscovery(Path root) {
    try {
      Files.find(root, 1, (_, bfa) -> bfa.isDirectory()).skip(1).forEach(this::visit);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void visit(Path path) {
    try {
      Dict dict = Dict.of(path);
      list.add(dict);
    } catch (IOException e) {
      System.err.println(path);
      throw new UncheckedIOException(e);
    }
  }

  static void main() {
    Path root = HomeDirectory.Database.resolve("freedict");
    new DictDiscovery(root);
  }
}
