// code by jph
package ch.alpine.ubongo.gui;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.ubongo.CalendarBoards;

@ReflectionMarker
public class CaesarManipulate implements ManipulateProvider {
  public CalendarBoards calendarBoards = CalendarBoards.CAESAR;
  public LocalDate localDate = LocalDate.now();

  @Override
  public JComponent getJComponent() {
    CalendarDialog calendarDialog = new CalendarDialog(calendarBoards.calendarBoard(), localDate);
    Show show = calendarDialog.getShow();
    return ShowGridComponent.of(List.of(show));
  }

  static void main() {
    new CaesarManipulate().run();
  }
}
