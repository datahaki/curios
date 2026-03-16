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

  public TileCoordinate shift(int dx, int dy) {
    int z = tile.z();
    long max = (1 << (z + 8)) - 1;
    long nx = (tile.x() * 256 + pix + dx) & max;
    long ny = (tile.y() * 256 + piy + dy) & max;
    return of(z, nx, ny);
  }

  public TileCoordinate zoom(int delta) {
    int nz = Math.min(Math.max(0, tile.z() + delta), 19);
    delta = nz - tile.z();
    IO.println("delta=" + delta);
    int z = tile.z();
    long max = (1 << (z + 8)) - 1;
    long nx = (tile.x() * 256 + pix) & max;
    long ny = (tile.y() * 256 + piy) & max;
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
