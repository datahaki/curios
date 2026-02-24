// code by jph
package ch.alpine.curios.run;

import ch.alpine.bridge.pro.VoidProvider;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.BinningMethods;
import ch.alpine.tensor.pdf.CDF;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.Expectation;
import ch.alpine.tensor.pdf.InverseCDF;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.HistogramDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.N;
import ch.alpine.tensor.sca.Round;

/* package */ enum HistogramDistributionDemo implements VoidProvider {
  INSTANCE;

  @Override
  public Void runStandalone() {
    Distribution gndtruth = NormalDistribution.standard();
    // gndtruth = UniformDistribution.unit();
    // gndtruth = PoissonDistribution.of(RealScalar.of(3));
    Tensor samples = RandomVariate.of(gndtruth, 10000);
    for (BinningMethods binningMethod : BinningMethods.values()) {
      Scalar width = binningMethod.apply(samples);
      System.out.println("width = " + width.maps(Round._4));
      Distribution distribution = HistogramDistribution.of(samples, width);
      System.out.println("mean = " + Expectation.mean(distribution).maps(Round._4));
      System.out.println("variance = " + Expectation.variance(distribution).maps(Round._4));
      InverseCDF inverseCDF = InverseCDF.of(distribution);
      Scalar q50 = inverseCDF.quantile(Rational.of(1, 2));
      System.out.println("q50 = " + q50);
      CDF cdf = CDF.of(distribution);
      Scalar p = cdf.p_lessThan(RealScalar.of(0));
      System.out.println("P[X<0] = " + p + " = " + N.DOUBLE.apply(p));
      PDF pdf = PDF.of(distribution);
      Scalar p0 = pdf.at(RealScalar.ZERO);
      System.out.println("P[0<=X<w] = " + p0 + " = " + N.DOUBLE.apply(p0));
      System.out.println("---");
    }
    return null;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
