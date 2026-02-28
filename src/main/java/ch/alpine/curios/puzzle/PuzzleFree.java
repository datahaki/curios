// code by jph
package ch.alpine.curios.puzzle;

import java.util.stream.Stream;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;

public enum PuzzleFree {
  ;
  public static final Scalar FREE = RealScalar.ONE.negate();

  public static Tensor fromString(String... strings) {
    final int n = Stream.of(strings).mapToInt(String::length).max().orElseThrow();
    return Tensor.of(Stream.of(strings).map(string -> {
      Tensor row = Array.zeros(n);
      for (int index = 0; index < string.length(); ++index)
        if (string.charAt(index) != ' ')
          row.set(FREE, index);
      return row;
    }));
  }
}
