// code by jph
package ch.alpine.curios.puzzle;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/** 12 different pieces */
public enum UbongoPieces {
  A0(new Color(154, 68, 41), "xx"),
  A1(new Color(62, 121, 87), "xx", "x"),
  A2(new Color(219, 96, 28), "xx", "xx"),
  A3(new Color(46, 96, 98), "xx", " xx"),
  A4(new Color(233, 197, 55), "xx", " x", " xx"),
  B0(new Color(61, 150, 147), "xxx"),
  B1(new Color(195, 211, 87), "xxx", "x"),
  B2(new Color(225, 222, 47), "xxx", " x"),
  B3(new Color(56, 100, 39), "xxx", "xx"),
  C0(new Color(204, 148, 25), "xxxx"),
  C1(new Color(245, 137, 90), "xxxx", "x"),
  C2(new Color(247, 174, 59), "xxxx", " x");

  final PuzzlePiece puzzlePiece;

  UbongoPieces(Color color, String... strings) {
    puzzlePiece = PuzzlePiece.of(ordinal(), color, strings);
  }

  private static final List<PuzzlePiece> LIST = Arrays.stream(values()).map(up -> up.puzzlePiece).toList();

  public static final List<PuzzlePiece> list() {
    return LIST;
  }
}
