// code by jph
package ch.alpine.ubongo;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.qty.Timing;

class PuzzlePieceTest {
  @Test
  void test() {
    CalendarBoard calendarBoard = CalendarBoards.KAISER.calendarBoard();
    UbongoBoard ubongoBoard = calendarBoard.of(Month.FEBRUARY, 2, DayOfWeek.MONDAY);
    List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
    Timing started = Timing.started();
    List<UbongoSolution> ubongoSolutions = ubongoBoard.perCombo(puzzlePieces.size(), 100);
    Scalar val = started.seconds();
    // IO.println(val);
    // IO.println(ubongoSolutions.size());
  }
}
