// code by jph
package ch.alpine.curios.dict;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import ch.alpine.tensor.ext.PathName;
import ch.alpine.tensor.io.MathematicaFormat;

class Dict {
  private static final int HEADER = 200;

  /** @param path to index-file
   * @param charset
   * @return
   * @throws IOException */
  public static Dict of(Path root, Charset charset) throws IOException {
    String name = root.getFileName().toString();
    Path path = root.resolve(name + ".index");
    PathName pathName = PathName.of(path);
    List<String> list = Files.readAllLines(path);
    Path base = pathName.withExtension("dict");
    if (!Files.exists(base)) {
      System.err.println("need to extract DZ file first!");
    }
    Dict dict = new Dict(name, Files.readAllBytes(base), charset);
    list.forEach(dict::register);
    return dict;
  }

  public static Dict of(Path path) throws IOException {
    return of(path, StandardCharsets.UTF_8);
  }

  final String name;
  private final byte[] bytes;
  private final Charset charset;
  final Map<String, List<OfsLen>> map = new HashMap<>();
  private final List<String> entries = new LinkedList<>();

  Dict(String name, byte[] bytes, Charset charset) {
    this.name = name;
    this.bytes = bytes;
    this.charset = charset;
  }

  private void register(String line) {
    String[] splits = line.split("\t");
    String entry = splits[0];
    OfsLen ofsLen = OfsLen.of(splits[1], splits[2]);
    if (HEADER <= ofsLen.ofs()) {
      if (!map.containsKey(entry))
        map.put(entry, new LinkedList<>());
      map.get(entry).add(ofsLen);
      String string = extract(ofsLen);
      entries.add(string);
    }
  }

  Stream<String> lookup(String search) {
    return map.getOrDefault(search, List.of()).stream().map(this::extract);
  }

  Stream<String> findIn(String search) {
    return entries.stream().filter(e -> e.contains(search));
  }

  String extract(OfsLen ofsLen) {
    return new String(bytes, ofsLen.ofs(), ofsLen.len(), charset);
  }

  public Set<String> keySet() {
    return map.keySet();
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(name, map.size());
  }
}
