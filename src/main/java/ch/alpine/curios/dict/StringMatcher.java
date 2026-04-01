// code by jph
package ch.alpine.curios.dict;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StringMatcher {
  ;
  public static void stream(Matcher matcher, MatchWrap matchWrap) {
    int pos = 0;
    while (matcher.find()) {
      matchWrap.handle(pos, matcher.start());
      matchWrap.handle(matcher);
      pos = matcher.end();
    }
    // matchWrap.handle(pos, input.length());
    matchWrap.handle(pos, matcher.regionEnd());
  }

  static void main() {
    // String input = "Hello [b]world[/b], this is [i]Java[/i]!";
    // Pattern pattern = Pattern.compile("\\[(b|i)](.*?)\\[/\\1]");
    String input = "Hello world, this is world!";
    Pattern pattern = Pattern.compile("world");
    Matcher matcher = pattern.matcher(input);
    MatchWrap matchWrap = new MatchWrap() {
      @Override
      public void handle(int beg, int end) {
        IO.println("HANDLE: " + input.substring(beg, end));
      }

      @Override
      public void handle(Matcher matcher) {
        IO.println("MATCHR: " + input.substring(matcher.start(), matcher.end()));
      }
    };
    stream(matcher, matchWrap);
  }
}
