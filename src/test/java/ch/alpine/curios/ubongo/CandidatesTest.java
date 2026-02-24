// code by jph
package ch.alpine.curios.ubongo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class CandidatesTest {
  @Test
  void testSize() {
    List<List<PuzzlePiece>> list = Candidates.of(10, 47, CaesarPieces.list());
    assertEquals(list.size(), 1);
  }
}
