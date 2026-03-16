// code by jph
package ch.alpine.curios.geo;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.qty.UnitSystem;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Floor;
import ch.alpine.tensor.sca.tri.ArcTan;
import ch.alpine.tensor.sca.tri.ArcTanh;
import ch.alpine.tensor.sca.tri.Sin;
import ch.alpine.tensor.sca.tri.Sinh;

public enum EarthCoordinate {
  ;
  public static final Clip CLIP = Clips.absolute(ArcTan.FUNCTION.apply(Sinh.FUNCTION.apply(Pi.VALUE)));

  /** 38.343373, -0.762800
   * 
   * @param z
   * @param lat
   * @param lon
   * @return */
  public static TileCoordinate from(int z, Scalar lat, Scalar lon) {
    lat = CLIP.apply(UnitSystem.SI().apply(lat));
    lon = UnitSystem.SI().apply(lon);
    Scalar ny = RealScalar.ONE.subtract(ArcTanh.FUNCTION.apply(Sin.FUNCTION.apply(lat)).divide(Pi.VALUE)).multiply(RealScalar.of(1 << z + 7));
    Scalar nx = lon.add(Pi.VALUE).divide(Pi.TWO).multiply(RealScalar.of(1 << z + 8));
    return TileCoordinate.of(z, Floor.longValueExact(nx), Floor.longValueExact(ny));
  }

  public static Tensor of(TileCoordinate tileCoordinate) {
    int z = tileCoordinate.tile().z();
    int ymax = 1 << z + 8;
    Scalar ang = Pi.VALUE.subtract(Rational.of(tileCoordinate.absy(), ymax).multiply(Pi.TWO));
    Scalar lat = ArcTan.FUNCTION.apply(Sinh.FUNCTION.apply(ang));
    // ---
    int xmax = 1 << z + 8;
    Scalar lon = Rational.of(tileCoordinate.absx(), xmax).subtract(Rational.HALF).multiply(Pi.TWO);
    return Tensors.of(lat, lon);
  }
}
