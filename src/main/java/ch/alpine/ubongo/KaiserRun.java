// code by jph
package ch.alpine.ubongo;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.Timing;
import ch.alpine.tensor.io.Put;

public class KaiserRun {
  public record Pair(Month month, int day) {
    public static Stream<Pair> all() {
      return Stream.of(Month.values()) //
          .flatMap(month -> IntStream.range(1, 32).boxed().map(day -> new Pair(month, day)));
    }

    @Override
    public final String toString() {
      return month + " " + day;
    }
  }

  public static Tensor check(CalendarBoard calendarBoard) {
    Pair.all().forEach(pair -> {
      for (DayOfWeek dayOfWeek : DayOfWeek.values())
        Throw.unless(calendarBoard.isSinglesFree(pair.month, pair.day, dayOfWeek));
    });
    Tensor array = Array.zeros(12, 31, 7);
    Pair.all().parallel().forEach(pair -> {
      Month month = pair.month;
      int day = pair.day;
      IO.println(pair);
      for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
        UbongoBoard ubongoBoard = calendarBoard.of(month, day, dayOfWeek);
        List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
        Timing started = Timing.started();
        List<UbongoSolution> ubongoSolutions = ubongoBoard.filter0(puzzlePieces.size(), 1);
        Scalar val = RealScalar.of(started.nanoSeconds());
        if (ubongoSolutions.isEmpty())
          System.err.println("NO SOLUTION: " + month + " " + day + " " + dayOfWeek);
        array.set(val, month.ordinal(), day - 1, dayOfWeek.ordinal());
      }
    });
    try {
      Put.of(HomeDirectory.path("some.mathematica"), array);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return array;
  }

  static void main() {
    check(CalendarBoards.TOWERS.calendarBoard());
  }
}
