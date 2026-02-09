// code by jph
package ch.alpine.ubongo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CaesarBoardTest {
  @ParameterizedTest
  @EnumSource
  void testSingleFree(CalendarBoards caesarBoard) {
    CalendarBoard calendarBoard = caesarBoard.calendarBoard();
    LocalDate.of(2020, 1, 1).datesUntil(LocalDate.of(2030, 1, 1)) //
        .filter(_ -> ThreadLocalRandom.current().nextDouble() < 0.02) //
        .map(calendarBoard::of).forEach(cb -> {
          assertTrue(StaticHelper.isSingleFree(cb.mask()));
        });
  }

  @ParameterizedTest
  @EnumSource
  void testNonEmpty(CalendarBoards caesarBoard) {
    CalendarBoard calendarBoard = caesarBoard.calendarBoard();
    LocalDate.of(2025, 1, 1).datesUntil(LocalDate.of(2026, 1, 1)) //
        .filter(_ -> ThreadLocalRandom.current().nextDouble() < 0.03) //
        .forEach(ld -> {
          // IO.println(ld);
          UbongoBoard ubongoBoard = calendarBoard.of(ld);
          List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
          List<UbongoSolution> ubongoSolutions = ubongoBoard.perCombo(puzzlePieces.size(), 1);
          assertFalse(ubongoSolutions.isEmpty());
        });
  }
}
