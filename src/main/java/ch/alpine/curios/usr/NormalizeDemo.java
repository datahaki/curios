// code by jph
package ch.alpine.curios.usr;

import java.util.stream.Stream;

import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.red.ScalarSummaryStatistics;
import ch.alpine.tensor.sca.Clip;

/* package */ enum NormalizeDemo {
  ;
  static void main() {
    Distribution distribution = NormalDistribution.standard();
    Clip clip = Stream.generate(() -> RandomVariate.of(distribution, 1000)) //
        .limit(10000) //
        .map(Vector2Norm.NORMALIZE) //
        .map(Vector2Norm::of) //
        .collect(ScalarSummaryStatistics.collector()).getClip();
    System.out.println(clip);
  }
}
