// code by jph
package ch.alpine.curios.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class CaesarPiecesTest {
  @Test
  void testSpecific() {
    CalendarBoard calendarBoard = CalendarBoards.CAESAR.calendarBoard();
    UbongoBoard ubongoBoard = calendarBoard.of(LocalDate.of(2026, 2, 8));
    List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
    int sum = puzzlePieces.stream().mapToInt(p -> p.count()).sum();
    assertEquals(ubongoBoard.count(), sum);
    List<UbongoSolution> ubongoSolutions = ubongoBoard.perCombo(puzzlePieces.size(), 1);
    ubongoSolutions.toString();
    assertTrue(!ubongoSolutions.isEmpty());
  }
}
