// code by jph
package ch.alpine.curios.puzzle;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;

public record UbongoEntry(int i, int j, PuzzlePiece ubongoPiece, Tensor stamp) implements Serializable {
}
