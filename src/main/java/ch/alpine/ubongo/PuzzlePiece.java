// code by jph
package ch.alpine.ubongo;

import java.awt.Color;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.NestList;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.img.ColorFormat;
import ch.alpine.tensor.img.ImageRotate;

public record PuzzlePiece(int ordinal, Color color, Tensor mask, int count, Set<OrientedPiece> stamps) implements Serializable {
  public static PuzzlePiece of(int ordinal, Color color, String... strings) {
    final int n = Stream.of(strings).mapToInt(String::length).max().orElseThrow();
    Tensor mask = Tensor.of(Stream.of(strings).map(string -> {
      Tensor row = Array.zeros(n);
      for (int index = 0; index < string.length(); ++index)
        if (string.charAt(index) == 'x')
          row.set(RealScalar.ONE, index);
      return row;
    })).unmodifiable();
    int count = (int) Flatten.stream(mask, -1).filter(RealScalar.ONE::equals).count();
    // ---
    Tensor rotated = NestList.of(ImageRotate.CCW, mask, 4);
    Set<OrientedPiece> set = Stream.concat( //
        rotated.stream(), //
        rotated.stream().map(Reverse::of)) //
        .distinct() //
        .map(OrientedPiece::of) //
        .collect(Collectors.toUnmodifiableSet());
    return new PuzzlePiece(ordinal, color, mask, count, set);
  }

  public int variationCount() {
    return stamps.size();
  }

  public Tensor colorVector() {
    return ColorFormat.toVector(color);
  }
}
