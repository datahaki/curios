// code by jph
package ch.alpine.curios.ubongo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import ch.alpine.bridge.io.ResourceLocator;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Export;
import ch.alpine.tensor.io.Import;

public enum UbongoLoader {
  INSTANCE;

  private final ResourceLocator resourceLocator = new ResourceLocator(HomeDirectory.Ephemeral.resolve("ubongo"));
  private final Function<UbongoBoards, List<UbongoSolution>> cache = Cache.of(this::of, 200);

  public List<UbongoSolution> load(UbongoBoards ubongoBoards) {
    return cache.apply(ubongoBoards);
  }

  private List<UbongoSolution> of(UbongoBoards ubongoBoards) {
    Path file = resourceLocator.resolve(ubongoBoards.name());
    if (Files.isRegularFile(file))
      try {
        return Import.object(file);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    System.out.println("compute");
    List<UbongoSolution> list = ubongoBoards.solve();
    try {
      if (!list.isEmpty())
        Export.object(file, list);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return list;
  }
}
