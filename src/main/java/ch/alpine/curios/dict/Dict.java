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

import ch.alpine.tensor.ext.PathName;

class Dict {
  private static final int HEADER = 200;

  /** @param path to index-file
   * @param charset
   * @return
   * @throws IOException */
  public static Dict of(Path path, Charset charset) throws IOException {
    PathName pathName = PathName.of(path);
    List<String> list = Files.readAllLines(path);
    Path base = pathName.withExtension("dict");
    Dict dict = new Dict(Files.readAllBytes(base), charset);
    list.forEach(dict::register);
    return dict;
  }

  public static Dict of(Path path) throws IOException {
    return of(path, StandardCharsets.UTF_8);
  }

  // ---
  final byte[] bytes;
  final Charset charset;
  final Map<String, List<OfsLen>> map = new HashMap<>();
  final List<String> entries = new LinkedList<>();

  Dict(byte[] bytes, Charset charset) {
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

  List<String> lookup(String entry) {
    return map.getOrDefault(entry, List.of()).stream().map(this::extract).toList();
  }

  List<String> findIn(String pat, int limit) {
    return entries.stream().filter(e -> e.contains(pat)).limit(limit).toList();
  }

  String extract(OfsLen ofsLen) {
    return new String(bytes, ofsLen.ofs(), ofsLen.len(), charset);
  }

  public List<String> answer(String search, int limit) {
    List<String> list = lookup(search);
    if (list.isEmpty())
      list = findIn(search, limit);
    return list;
  }
}
