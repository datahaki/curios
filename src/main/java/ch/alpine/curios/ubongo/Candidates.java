// code by jph
package ch.alpine.curios.ubongo;

import java.util.LinkedList;
import java.util.List;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Subsets;
import ch.alpine.tensor.io.Primitives;

public enum Candidates {
  ;
  /** @param use how many pieces
   * @param count free space
   * @return list of list of candidates of size use, that have the sum of
   * number of tiles equals to count */
  public static List<List<PuzzlePiece>> of(int use, int count, List<PuzzlePiece> puzzlePieces) {
    List<List<PuzzlePiece>> values = new LinkedList<>();
    for (Tensor index : Subsets.of(Range.of(0, puzzlePieces.size()), use)) {
      int sum = Primitives.toIntStream(index) //
          .map(i -> puzzlePieces.get(i).count()) //
          .sum();
      if (sum == count) {
        List<PuzzlePiece> list = Primitives.toIntStream(index) //
            .mapToObj(puzzlePieces::get) //
            .toList();
        values.add(list);
      }
    }
    return values;
  }
}
