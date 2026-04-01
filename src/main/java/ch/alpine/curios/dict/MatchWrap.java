// code by jph
package ch.alpine.curios.dict;

import java.util.regex.Matcher;

interface MatchWrap {
  void handle(Matcher matcher);

  void handle(int beg, int end);
}
