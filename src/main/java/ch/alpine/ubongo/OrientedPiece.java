// code by jph
package ch.alpine.ubongo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.red.Total;

public record OrientedPiece(Tensor stamp, Tensor rows, Tensor cols, int size0, int size1, List<Pnt> deltas) implements Serializable {
  public static OrientedPiece of(Tensor stamp) {
    List<Integer> list = Dimensions.of(stamp);
    int size0 = list.get(0);
    int size1 = list.get(1);
    List<Pnt> deltas = new LinkedList<>();
    for (int si = 0; si < size0; ++si)
      for (int sj = 0; sj < size1; ++sj)
        if (stamp.get(si, sj).equals(RealScalar.ONE))
          deltas.add(new Pnt(si, sj));
    return new OrientedPiece( //
        stamp, //
        Total.of(stamp), //
        Tensor.of(stamp.stream().map(Total::of)), //
        size0, //
        size1, //
        deltas);
  }
}
