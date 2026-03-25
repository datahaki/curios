// code by jph
package ch.alpine.curios.usr;

import java.util.Arrays;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.ArgMin;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.ply.RootsBounds;

enum RootsBoundsDemo {
  ;
  static void main() {
    for (int deg = 2; deg < 10; ++deg) {
      Distribution distribution = UniformDistribution.of(-10, +10);
      Tensor coeffs = RandomVariate.of(distribution, deg + 1);
      int index = ArgMin.of(Tensor.of(Arrays.stream(RootsBounds.values()).map(s -> s.of(coeffs))));
      System.out.println(deg + " " + RootsBounds.values()[index]);
    }
  }
}
