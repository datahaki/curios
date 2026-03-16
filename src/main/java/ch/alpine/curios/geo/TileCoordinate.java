// code by jph
package ch.alpine.curios.geo;

import ch.alpine.tensor.ext.Integers;

public record TileCoordinate(Tile tile, int pix, int piy) {
  public static TileCoordinate of(int z, long nx, long ny) {
    int tx = (int) (nx / 256);
    int ty = (int) (ny / 256);
    return new TileCoordinate(new Tile(z, tx, ty), (int) (nx & 0xff), (int) (ny & 0xff));
  }

  public TileCoordinate {
    Integers.requireEquals(Integers.clip(0, 255).applyAsInt(pix), pix);
    Integers.requireEquals(Integers.clip(0, 255).applyAsInt(piy), piy);
  }

  public long absx() {
    return tile.x() * 256 + pix;
  }

  public long absy() {
    return tile.y() * 256 + piy;
  }

  /** @param dx pixel level
   * @param dy pixel level
   * @return */
  public TileCoordinate shift(int dx, int dy) {
    int z = tile.z();
    long mask = (1 << (z + 8)) - 1;
    long nx = (absx() + dx) & mask;
    long ny = (absy() + dy) & mask;
    return of(z, nx, ny);
  }

  public TileCoordinate zoom(int delta) {
    int z = tile.z();
    int nz = Math.min(Math.max(0, z + delta), 19);
    delta = nz - z;
    long mask = (1 << (z + 8)) - 1;
    long nx = absx() & mask;
    long ny = absy() & mask;
    if (0 <= delta) {
      nx <<= delta;
      ny <<= delta;
    } else {
      nx >>= -delta;
      ny >>= -delta;
    }
    return of(z + delta, nx, ny);
  }
}
