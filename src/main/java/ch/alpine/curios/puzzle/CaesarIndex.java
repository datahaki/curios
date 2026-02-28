// code by jph
package ch.alpine.curios.puzzle;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.api.ScalarUnaryOperator;

public class CaesarIndex {
  private static final ScalarUnaryOperator OCCUPY = s -> {
    Throw.unless(s.equals(RealScalar.ONE.negate()));
    return s.zero();
  };
  final List<Pnt> months = new ArrayList<>();
  final List<Pnt> days = new ArrayList<>();
  final List<Pnt> dayOfWeeks = new ArrayList<>();
  private Tensor mask;

  public CaesarIndex(String... strings) {
    mask = PuzzleFree.fromString(strings);
    for (int i = 0; i < strings.length; ++i) {
      for (int j = 0; j < strings[i].length(); ++j) {
        char chr = strings[i].charAt(j);
        switch (chr) {
        case 'm': {
          months.add(new Pnt(i, j));
          break;
        }
        case 'o': {
          days.add(new Pnt(i, j));
          break;
        }
        case 'w': {
          dayOfWeeks.add(new Pnt(i, j));
          break;
        }
        default:
          break;
        }
      }
    }
    Throw.unless(months.size() == 12);
    Throw.unless(days.size() == 31);
    Throw.unless(dayOfWeeks.size() == 7);
  }

  public Tensor occupy(Month month, int day, DayOfWeek dayOfWeek) {
    Tensor prep = mask.copy();
    {
      Pnt pnt = months.get(month.ordinal());
      prep.set(OCCUPY, pnt.i(), pnt.j());
    }
    {
      Pnt pnt = days.get(day - 1);
      prep.set(OCCUPY, pnt.i(), pnt.j());
    }
    {
      Pnt pnt = dayOfWeeks.get(dayOfWeek.ordinal());
      prep.set(OCCUPY, pnt.i(), pnt.j());
    }
    return prep;
  }

  public Map<Tensor, String> mapping() {
    Map<Tensor, String> map = new HashMap<>();
    for (Month month : Month.values()) {
      Pnt pnt = months.get(month.ordinal());
      map.put(Tensors.vector(pnt.j(), pnt.i()), month.toString().substring(0, 3));
    }
    for (int day = 0; day < 31; ++day) {
      Pnt pnt = days.get(day);
      map.put(Tensors.vector(pnt.j(), pnt.i()), String.valueOf(day + 1));
    }
    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
      Pnt pnt = dayOfWeeks.get(dayOfWeek.ordinal());
      map.put(Tensors.vector(pnt.j(), pnt.i()), dayOfWeek.toString().substring(0, 3));
    }
    return map;
  }
}
