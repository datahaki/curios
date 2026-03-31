// code by jph
package ch.alpine.curios.dict;

import java.util.HashMap;
import java.util.Map;

enum Dict64Decoder {
  INSTANCE;

  private final Map<Character, Integer> map = new HashMap<>();

  private Dict64Decoder() {
    String INDEX = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    for (int i = 0; i < INDEX.length(); ++i)
      map.put(INDEX.charAt(i), i);
  }

  public long toValue(String string) {
    long result = 0;
    for (char c : string.toCharArray())
      result = (result << 6) | map.get(c); // multiply by 64 and add
    return result;
  }

  public int toIntValue(String string) {
    return Math.toIntExact(toValue(string));
  }
}
