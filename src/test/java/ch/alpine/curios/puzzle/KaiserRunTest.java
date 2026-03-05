// code by jph
package ch.alpine.curios.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.curios.puzzle.KaiserRun.Pair;

class KaiserRunTest {
  @Test
  void test() {
    long count = Pair.all().count();
    assertEquals(count, 12 * 31);
  }

  @ParameterizedTest
  @EnumSource
  void testEmu(CalendarBoards calendarBoards) {
    IO.println(calendarBoards);
    Month month = Month.values()[ThreadLocalRandom.current().nextInt(12)];
    int day = 1 + ThreadLocalRandom.current().nextInt(28);
    KaiserRun kaiserRun = new KaiserRun() {
      @Override
      public Stream<Pair> stream() {
        return Stream.of(month) //
            .flatMap(month -> IntStream.range(day, day + 1).boxed().map(day -> new Pair(month, day)));
      }
    };
    kaiserRun.check(calendarBoards.calendarBoard());
  }
}
