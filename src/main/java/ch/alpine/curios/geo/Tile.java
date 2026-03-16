// code by jph
package ch.alpine.curios.geo;

import java.nio.file.Path;
import java.util.HexFormat;
import java.util.Objects;

import ch.alpine.tensor.ext.Integers;

record Tile(int z, int x, int y) {
  public static int maxExclusive(int z) {
    return 1 << z;
  }

  public static int maxInclusive(int z) {
    return maxExclusive(z) - 1;
  }

  Tile {
    int max = maxInclusive(z);
    Integers.requireEquals(Math.min(Math.max(0, x), max), x);
    Integers.requireEquals(Math.min(Math.max(0, y), max), y);
  }

  public final Path path() {
    int hash = Objects.hash(z, x, y);
    hash += (hash >> 12) + (hash >> 24);
    short bytes = (short) hash;
    String string = HexFormat.of().toHexDigits(bytes).substring(1);
    return Path.of("" + string.charAt(0), "" + string.charAt(1), "" + string.charAt(2), String.format("%d_%d_%d.png", z, x, y));
  }
}
