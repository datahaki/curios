// code by jph
package ch.alpine.curios.sea;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

class BikeEntry {
  String date = "";
  String location = "";
  Scalar km = RealScalar.ZERO;
  Scalar incr = RealScalar.ZERO;
  Scalar decr = RealScalar.ZERO;
  String gps0 = "";
  String gps1 = "";
  String link = "";

  public boolean isComplete() {
    return !location.isBlank() //
        && !gps0.isBlank() //
        && !gps1.isBlank();
  }
}
