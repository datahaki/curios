// code by jph
package ch.alpine.curios.puzzle;

import java.util.List;
import java.util.stream.Stream;

import ch.alpine.tensor.img.ColorDataLists;

public enum CaesarPieces {
  C1("xxx", "x"),
  C2("x", "xxx", "  x"),
  C3("xx", " xx"),
  C4("xxx", " x", " x"),
  C5("xxx", "xx"),
  C6("xxx", "x x"),
  C7("xxx", "x", "x"),
  D1("xxxx"),
  D2("xxxx", "x"),
  D3("xxx", "  xx");

  private final PuzzlePiece puzzlePiece;

  CaesarPieces(String... strings) {
    puzzlePiece = PuzzlePiece.of( //
        ordinal(), //
        ColorDataLists._250.strict().getColor(ordinal()), //
        strings);
  }

  private static final List<PuzzlePiece> LIST = Stream.of(values()).map(up -> up.puzzlePiece).toList();

  public static final List<PuzzlePiece> list() {
    return LIST;
  }
}
