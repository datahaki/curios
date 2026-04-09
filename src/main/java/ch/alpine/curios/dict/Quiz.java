// code by jph
package ch.alpine.curios.dict;

import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.swing.JTextPane;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldFuse;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ext.HomeDirectory;

@ReflectionMarker
class Quiz implements ManipulateProvider {
  private final Dict dict;
  private final List<String> list;
  private final JTextPane jTextPane = new JTextPane();
  public String answer = "";
  @FieldFuse
  public Boolean next = false;
  private Integer index = null;

  public Quiz(Dict dict, List<String> list) {
    this.dict = dict;
    this.list = list;
  }

  @Override
  public Container getContainer() {
    if (next) {
      next = false;
      index = null;
    }
    if (index == null) {
      index = ThreadLocalRandom.current().nextInt(list.size());
      String string = list.get(index);
      String collect = dict.lookup(string).collect(Collectors.joining("\n"));
      jTextPane.setText(string + "\n" + collect);
    }
    return jTextPane;
  }

  static void main() throws IOException {
    Path root = HomeDirectory.Database.resolve("freedict");
    Dict dict = Dict.of(root.resolve("freedict-eng-spa-2025.11.23.dictd", "eng-spa", "eng-spa.index"));
    List<String> list = Files.lines(HomeDirectory.Database.resolve("500_english.vector")) //
        .filter(entry -> dict.lookup(entry.toLowerCase()).count() > 0) //
        .toList();
    new Quiz(dict, list).runStandalone();
  }
}
