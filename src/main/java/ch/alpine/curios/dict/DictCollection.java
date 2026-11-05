// code by jph
package ch.alpine.curios.dict;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class DictCollection {
  private final List<Dict> list;

  public DictCollection(List<Dict> list) {
    this.list = list;
  }

  public List<String> filters() {
    Set<String> set = new TreeSet<>();
    set.add("");
    for (Dict dict : list) {
      String string = dict.name;
      set.add(string);
      int index = string.indexOf('-');
      set.add(string.substring(0, index));
      set.add(string.substring(index + 1));
      set.add(string.substring(0, index + 1));
      set.add(string.substring(index));
    }
    return set.stream().toList();
  }

  public Stream<Dict> filter(String string) {
    return list.stream().filter(dict -> dict.name.contains(string));
  }
}
