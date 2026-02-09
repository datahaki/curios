// code by jph
package ch.alpine.ubongo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.bridge.fig.ImagePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowWindow;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.ubongo.gui.UbongoRender;

class CaesarPiecesTest {
  @Test
  void testSpecific() {
    CalendarBoard calendarBoard = CalendarBoards.CAESAR.calendarBoard();
    UbongoBoard ubongoBoard = calendarBoard.of(LocalDate.of(2026, 2, 8));
    List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
    int sum = puzzlePieces.stream().mapToInt(p -> p.count()).sum();
    assertEquals(ubongoBoard.count(), sum);
    List<UbongoSolution> ubongoSolutions = ubongoBoard.filter0(puzzlePieces.size(), 1);
    IO.println(ubongoBoard.message);
    IO.println(ubongoSolutions.size());
    ubongoSolutions.forEach(IO::println);
    if (!ubongoSolutions.isEmpty()) {
      UbongoSolution ubongoSolution = ubongoSolutions.getFirst();
      List<UbongoEntry> solution = ubongoSolution.list();
      Tensor matrix = UbongoRender.matrix(Dimensions.of(ubongoBoard.mask()), solution);
      Show show = new Show();
      show.add(ImagePlot.of(ImageFormat.of(matrix.map(ColorDataLists._097.strict()))));
      ShowWindow.of(show);
    }
  }
}
