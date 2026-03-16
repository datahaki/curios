// code by jph
package ch.alpine.curios.geo;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.qty.UnitSystem;
import ch.alpine.tensor.sca.Floor;

public enum EarthCoordinate {
  ;
  public static TileCoordinate from(int z, Scalar lat, Scalar lon) {
    Scalar scalar = RealScalar.of(1 << z + 8);
    lat = UnitSystem.SI().apply(lat).add(Pi.VALUE).divide(Pi.TWO).multiply(scalar);
    lon = Pi.HALF.subtract(UnitSystem.SI().apply(lon)).divide(Pi.VALUE).multiply(scalar);
    return TileCoordinate.of(z, Floor.longValueExact(lat), Floor.longValueExact(lon));
  }
}
