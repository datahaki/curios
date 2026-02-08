// code by jph
package ch.alpine.ubongo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class UbongoTest {
  @Test
  void testSimple() {
    assertEquals(UbongoPieces.values().length, 12);
    assertEquals(UbongoPieces.C2.puzzlePiece.count(), 5);
  }

  @Test
  void testStampsSpec() {
    assertEquals(UbongoPieces.A0.puzzlePiece.variationCount(), 2);
    assertEquals(UbongoPieces.A1.puzzlePiece.variationCount(), 4);
    assertEquals(UbongoPieces.A2.puzzlePiece.variationCount(), 1);
    assertEquals(UbongoPieces.B1.puzzlePiece.variationCount(), 8);
    assertEquals(UbongoPieces.B2.puzzlePiece.variationCount(), 4);
    assertEquals(UbongoPieces.C0.puzzlePiece.variationCount(), 2);
    assertEquals(UbongoPieces.C2.puzzlePiece.variationCount(), 8);
  }

  @Test
  @Disabled
  void testStamps() {
    for (UbongoPieces ubongo : UbongoPieces.values())
      System.out.println(ubongo + " " + ubongo.puzzlePiece.stamps().size());
  }
}
