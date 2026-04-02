// code by jph
package ch.alpine.curios.puzzle.gui;

import java.util.List;
import java.util.stream.Collectors;

import ch.alpine.curios.puzzle.PuzzlePiece;
import ch.alpine.curios.puzzle.UbongoEntry;
import ch.alpine.curios.puzzle.UbongoPieces;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.chq.DeterminateScalarQ;
import ch.alpine.tensor.col.ColorDataIndexed;
import ch.alpine.tensor.col.CyclicColorDataIndexed;
import ch.alpine.tensor.col.StrictColorDataIndexed;
import ch.alpine.tensor.img.ImageRotate;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

public enum UbongoRender {
  ;
  private static final ColorDataIndexed INSTANCE = //
      StrictColorDataIndexed.of(Tensor.of(UbongoPieces.list().stream().map(PuzzlePiece::colorVector)));
  private static final ColorDataIndexed MONOCHROME = //
      CyclicColorDataIndexed.of(Tensors.of(Tensors.vector(160, 160, 160, 255)));

  /** @param list
   * @param solution
   * @return */
  public static Tensor matrix(List<Integer> list, List<UbongoEntry> solution) {
    Tensor image = Array.same(DoubleScalar.INDETERMINATE, list);
    for (UbongoEntry ubongoEntry : solution) {
      List<Integer> size = Dimensions.of(ubongoEntry.stamp());
      for (int si = 0; si < size.get(0); ++si)
        for (int sj = 0; sj < size.get(1); ++sj)
          if (Scalars.nonZero(ubongoEntry.stamp().Get(si, sj)))
            image.set(RealScalar.of(ubongoEntry.ubongoPiece().ordinal()), ubongoEntry.i() + si, ubongoEntry.j() + sj);
    }
    return image;
  }

  public static String string(List<Integer> list, List<UbongoEntry> solution) {
    return stringMatrix(matrix(list, solution));
  }

  public static String stringMatrix(Tensor matrix) {
    return matrix.stream().map(UbongoRender::string).collect(Collectors.joining("|", "|", "|"));
  }

  /** @param list
   * @param solution
   * @return */
  public static Tensor of(List<Integer> list, List<UbongoEntry> solution) {
    return matrix(list, solution).maps(INSTANCE);
  }

  private static final String HEX = "0123456789ABCDEF";

  private static String string(Tensor vector) {
    Clip clip = Clips.positive(15);
    return vector.stream() //
        .map(Scalar.class::cast) //
        .map(s -> DeterminateScalarQ.of(s) //
            ? "" + HEX.charAt(Scalars.intValueExact(clip.requireInside(s)))
            : ".") //
        .collect(Collectors.joining());
  }

  /** @param list
   * @param solution
   * @return */
  public static Tensor gray(List<Integer> list, List<UbongoEntry> solution) {
    return matrix(list, solution).maps(MONOCHROME);
  }

  // ---
  public static Tensor matrix(PuzzlePiece ubongoPiece) {
    Scalar ord = RealScalar.of(ubongoPiece.ordinal());
    return ImageRotate.CW.apply(ubongoPiece.mask()) //
        .maps(s -> Scalars.isZero(s) //
            ? DoubleScalar.INDETERMINATE
            : ord);
  }

  public static Tensor of(PuzzlePiece ubongoPiece) {
    return matrix(ubongoPiece).maps(INSTANCE);
  }
}
