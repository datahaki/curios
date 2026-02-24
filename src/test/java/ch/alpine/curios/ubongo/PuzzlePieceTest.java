// code by jph
package ch.alpine.curios.ubongo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.qty.Timing;
import ch.alpine.tensor.sca.Sign;

class PuzzlePieceTest {
  @Test
  void test() {
    CalendarBoard calendarBoard = CalendarBoards.KAISER.calendarBoard();
    UbongoBoard ubongoBoard = calendarBoard.of(LocalDate.now());
    List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
    Timing started = Timing.started();
    List<UbongoSolution> ubongoSolutions = ubongoBoard.perCombo(puzzlePieces.size(), 10);
    Scalar val = started.seconds();
    Sign.requirePositive(val);
    assertTrue(1 <= ubongoSolutions.size());
  }
}
