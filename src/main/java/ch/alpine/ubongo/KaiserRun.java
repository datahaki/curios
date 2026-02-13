// code by jph
package ch.alpine.ubongo;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Put;
import ch.alpine.tensor.qty.Timing;
import ch.alpine.tensor.sca.Round;
import ch.alpine.ubongo.gui.UbongoRender;

public class KaiserRun {
  public record Pair(Month month, int day) {
    public static Stream<Pair> all() {
      return Stream.of(Month.values()) //
          .flatMap(month -> IntStream.rangeClosed(1, 31).boxed().map(day -> new Pair(month, day)));
    }

    @Override
    public final String toString() {
      return month.toString().substring(0, 3) + " " + String.format("%02d", day);
    }
  }

  public static Tensor check(CalendarBoard calendarBoard) {
    Pair.all().forEach(pair -> {
      for (DayOfWeek dayOfWeek : DayOfWeek.values())
        Throw.unless(calendarBoard.isSinglesFree(pair.month, pair.day, dayOfWeek));
    });
    Tensor array = Array.zeros(12, 31, 7);
    Timing timing = Timing.started();
    Pair.all().parallel().forEach(pair -> {
      Month month = pair.month;
      int day = pair.day;
      for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
        UbongoBoard ubongoBoard = calendarBoard.of(month, day, dayOfWeek);
        List<PuzzlePiece> puzzlePieces = CaesarPieces.list();
        List<UbongoSolution> ubongoSolutions = ubongoBoard.perCombo(puzzlePieces.size(), 1);
        if (ubongoSolutions.isEmpty())
          throw new RuntimeException("NO SOLUTION: " + month + " " + day + " " + dayOfWeek);
        else {
          UbongoSolution ubongoSolution = ubongoSolutions.getFirst();
          String string = UbongoRender.string(ubongoBoard.board_size(), ubongoSolution.list());
          IO.println(pair + " " + dayOfWeek.toString().substring(0, 3) + " " + string);
          Tensor insert = UbongoRender.matrix(ubongoBoard.board_size(), ubongoSolution.list());
          array.set(insert, month.ordinal(), day - 1, dayOfWeek.ordinal());
        }
      }
    });
    IO.println(timing.seconds().maps(Round._1));
    try {
      IO.println("storing");
      Put.of(HomeDirectory.Ephemeral.resolve("cheese.mathematica"), array);
      IO.println("stored");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return array;
  }

  static void main() {
    check(CalendarBoards.CHEESY.calendarBoard());
  }
}
