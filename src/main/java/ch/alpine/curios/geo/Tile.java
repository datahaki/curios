// code by jph
package ch.alpine.curios.geo;

import java.nio.file.Path;
import java.util.HexFormat;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

import ch.alpine.tensor.ext.Integers;

record Tile(int z, int x, int y) {
  Tile {
    IntUnaryOperator clip = Integers.clip(0, (1 << z) - 1);
    x = clip.applyAsInt(x);
    y = clip.applyAsInt(y);
  }

  public final Path path() {
    int hash = Objects.hash(z, x, y);
    hash += (hash >> 12) + (hash >> 24);
    short bytes = (short) hash;
    String string = HexFormat.of().toHexDigits(bytes).substring(1);
    return Path.of("" + string.charAt(0), "" + string.charAt(1), "" + string.charAt(2), String.format("%d_%d_%d.png", z, x, y));
  }

  public Tile zoomIn(int x2, int y2) {
    return new Tile(z + 1, 2 * x + x2, 2 * y + y2);
  }

  public Tile zoomOut() {
    return z == 0 //
        ? this
        : new Tile(z - 1, x / 2, y / 2);
  }

  public Tile add(int i, int j) {
    return new Tile(z, x + i, y + j);
  }
}
