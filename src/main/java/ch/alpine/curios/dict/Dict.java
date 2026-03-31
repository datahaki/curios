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
    if (!map.containsKey(entry))
      map.put(entry, new LinkedList<>());
    map.get(entry).add(ofsLen);
    // {
    String string = extract(ofsLen);
    entries.add(string);
    // Optional<String> optional = string.lines().skip(1).findFirst();
    // if (optional.isPresent()) {
    // String words = optional.orElseThrow();
    // if (words.startsWith("1. ")) {
    // // IO.println(string);
    // List<String> alts = string.lines().filter(Head::numbered) //
    // .map(s -> s.substring(3)).toList();
    // // IO.println(alts);
    // alts.forEach(alt -> regRev(alt, ofsLen));
    // } else {
    // regRev(words, ofsLen);
    // }
    // }
    // }
  }
  // private void regRev(String words, OfsLen ofsLen) {
  // for (String word : words.split(",")) {
  // word = word.trim().toLowerCase();
  // if (!rev.containsKey(word))
  // rev.put(word, new LinkedList<>());
  // rev.get(word).add(ofsLen);
  // }
  // }

  List<String> lookup(String entry) {
    return
    // Stream.concat( //
    // map.getOrDefault(entry, List.of()).stream(), //
    // rev.getOrDefault(entry, List.of()).stream()) //
    map.getOrDefault(entry, List.of()).stream()
        // .distinct() //
        .map(this::extract).toList();
  }

  List<String> findIn(String pat, int limit) {
    return entries.stream().filter(e -> e.contains(pat)).limit(limit).toList();
  }

  String extract(OfsLen ofsLen) {
    return new String(bytes, ofsLen.ofs(), ofsLen.len(), charset);
  }
}
