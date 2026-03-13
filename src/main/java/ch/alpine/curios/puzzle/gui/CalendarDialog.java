// code by jph
package ch.alpine.curios.puzzle.gui;

import java.time.LocalDate;
import java.util.List;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowOption;
import ch.alpine.bridge.fig.plt.ImagePlot;
import ch.alpine.bridge.fig.plt.StringPlot;
import ch.alpine.bridge.fig.plt.StringPlot.StringItem;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.curios.puzzle.CaesarPieces;
import ch.alpine.curios.puzzle.CalendarBoard;
import ch.alpine.curios.puzzle.CalendarBoards;
import ch.alpine.curios.puzzle.PuzzlePiece;
import ch.alpine.curios.puzzle.UbongoBoard;
import ch.alpine.curios.puzzle.UbongoEntry;
import ch.alpine.curios.puzzle.UbongoSolution;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.ext.RomanNumeral;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.img.ImageResize;
import ch.alpine.tensor.io.ImageFormat;

record CalendarDialog(CalendarBoard calendarBoard, LocalDate localDate) implements ShowProvider {
  static String pretty(LocalDate localDate) {
    return RomanNumeral.of(localDate.getYear()) + " " + localDate.getMonth() + " " + localDate.getDayOfMonth() + " " + localDate.getDayOfWeek();
  }

  @Override
  public Show getShow() {
    UbongoBoard ubongoBoard = calendarBoard.of(localDate);
    List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
    List<UbongoSolution> ubongoSolutions = ubongoBoard.perCombo(puzzlePieces.size(), 1);
    UbongoSolution ubongoSolution = ubongoSolutions.getFirst();
    List<UbongoEntry> solution = ubongoSolution.list();
    Tensor matrix = UbongoRender.matrix(Dimensions.of(ubongoBoard.mask()), solution);
    Show show = new Show();
    show.setPlotLabel(pretty(localDate));
    show.set(ShowOption.GRID, false);
    show.add(ImagePlot.of(ImageFormat.of(matrix.maps(ColorDataLists._097.strict())), ImageResize.DEGREE_0));
    List<StringItem> list = calendarBoard.mapping().entrySet().stream().map(e -> StringItem.of(e.getKey(), e.getValue())).toList();
    show.add(StringPlot.of(list));
    return show;
  }

  static void main() {
    new CalendarDialog(CalendarBoards.CHEESY.calendarBoard(), LocalDate.now()).runStandalone();
  }
}
