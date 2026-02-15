// code by jph
package ch.alpine.curios.sbf;

import java.io.IOException;
import java.util.List;

import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Ordering;
import ch.alpine.tensor.alg.TensorMap;
import ch.alpine.tensor.num.Boole;
import ch.alpine.tensor.red.Total;

public class SbfReport {
  private final List<SbfItem> sbfItems;
  private final SbfTrack sbfTrack;

  public SbfReport(SbfType sbfType) throws IOException {
    sbfItems = SbfParser.get(sbfType);
    sbfTrack = new SbfTrack(sbfType, sbfItems.size());
    Tensor tensor = sbfTrack.tensor.maps(s -> Boole.of(Scalars.isZero(s)));
    Tensor totals = TensorMap.of(Total::ofVector, tensor, 1);
    int[] indices = Ordering.DECREASING.of(totals);
    for (int c = 0; c < 10; ++c) {
      int index = indices[c];
      System.out.println(totals.get(index));
      System.out.println(sbfItems.get(index).question);
      System.out.println("---");
    }
  }

  @SuppressWarnings("unused")
  static void main() throws IOException {
    new SbfReport(SbfType.binnen);
  }
}
