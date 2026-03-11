// code by jph
package ch.alpine.curios.puzzle.gui;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.curios.puzzle.CalendarBoards;

@ReflectionMarker
public class CalendarBoardsDemo implements ManipulateProvider {
  public CalendarBoards calendarBoards = CalendarBoards.CAESAR;
  public LocalDate localDate = LocalDate.now();

  @Override
  public JComponent getContainer() {
    CalendarDialog calendarDialog = new CalendarDialog(calendarBoards.calendarBoard(), localDate);
    Show show = calendarDialog.getShow();
    return ShowGridComponent.of(List.of(show));
  }

  static void main() {
    new CalendarBoardsDemo().runStandalone();
  }
}
