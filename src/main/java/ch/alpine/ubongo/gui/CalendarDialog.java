// code by jph
package ch.alpine.ubongo.gui;

import java.time.LocalDate;
import java.util.List;

import ch.alpine.bridge.fig.ImagePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.StringPlot;
import ch.alpine.bridge.fig.StringPlot.StringItem;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.ubongo.CaesarPieces;
import ch.alpine.ubongo.CalendarBoard;
import ch.alpine.ubongo.CalendarBoards;
import ch.alpine.ubongo.PuzzlePiece;
import ch.alpine.ubongo.UbongoBoard;
import ch.alpine.ubongo.UbongoEntry;
import ch.alpine.ubongo.UbongoSolution;

record CalendarDialog(CalendarBoard calendarBoard, LocalDate localDate) implements ShowProvider {
  @Override
  public Show getShow() {
    UbongoBoard ubongoBoard = calendarBoard.of(localDate);
    List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
    List<UbongoSolution> ubongoSolutions = ubongoBoard.perCombo(puzzlePieces.size(), 1);
    UbongoSolution ubongoSolution = ubongoSolutions.getFirst();
    List<UbongoEntry> solution = ubongoSolution.list();
    Tensor matrix = UbongoRender.matrix(Dimensions.of(ubongoBoard.mask()), solution);
    Show show = new Show();
    show.setPlotLabel(localDate.toString() + " " + localDate.getDayOfWeek());
    show.setGridLines(false);
    show.add(ImagePlot.of(ImageFormat.of(matrix.maps(ColorDataLists._097.strict()))));
    List<StringItem> list = calendarBoard.mapping().entrySet().stream().map(e -> StringItem.of(e.getKey(), e.getValue())).toList();
    show.add(StringPlot.of(list));
    return show;
  }

  static void main() {
    new CalendarDialog(CalendarBoards.KAISER.calendarBoard(), LocalDate.now()).run();
  }
}
