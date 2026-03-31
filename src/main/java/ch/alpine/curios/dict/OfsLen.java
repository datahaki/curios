// code by jph
package ch.alpine.curios.dict;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

record OfsLen(int ofs, int len) {
  static OfsLen of(String ofs, String len) {
    return new OfsLen( //
        Dict64Decoder.INSTANCE.toIntValue(ofs), //
        Dict64Decoder.INSTANCE.toIntValue(len));
  }

  static void main() {
    Pattern pattern = Pattern.compile("\\d\\. ");
    Matcher matcher = pattern.matcher("asd1. abd");
    IO.println(matcher.find());
  }
}
