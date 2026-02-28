// code by jph
package ch.alpine.curios.puzzle;

import java.util.stream.Stream;

public enum UbongoPrep {
  ;
  static void main() {
    Stream.of(UbongoBoards.values()) //
        .filter(ub -> ub.use() >= 9) //
        .parallel() //
        .forEach(UbongoLoader.INSTANCE::load);
  }
}
