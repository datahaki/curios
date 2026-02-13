// code by jph
package ch.alpine.ubongo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.mat.MatrixQ;

public final class CalendarBoard {
  private final CaesarIndex caesarIndex;

  public CalendarBoard(String string) {
    caesarIndex = new CaesarIndex(string.lines().toArray(String[]::new));
  }

  public UbongoBoard of(LocalDate localDate) {
    Month month = localDate.getMonth();
    int day = localDate.getDayOfMonth();
    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
    return of(month, day, dayOfWeek);
  }

  public UbongoBoard of(Month month, int day, DayOfWeek dayOfWeek) {
    return new UbongoBoard(caesarIndex.occupy(month, day, dayOfWeek), CaesarPieces.list());
  }

  public boolean isSinglesFree(Month month, int day, DayOfWeek dayOfWeek) {
    Tensor prep = caesarIndex.occupy(month, day, dayOfWeek);
    Tensor mask = MatrixQ.require(prep).unmodifiable();
    boolean singleFree = StaticHelper.isSingleFree(mask);
    if (!singleFree)
      System.err.println(month + " " + day + " " + dayOfWeek);
    Throw.unless(singleFree);
    return singleFree;
  }

  public Map<Tensor, String> mapping() {
    return caesarIndex.mapping();
  }
}
