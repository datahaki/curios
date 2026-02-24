// code by jph
package ch.alpine.curios.ubongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.ArrayPad;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Partition;
import ch.alpine.tensor.ext.Lists;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.mat.MatrixQ;

public class UbongoBoard {
  public static final Scalar FREE = RealScalar.ONE.negate();
  public static final int free = -1;
  public static final int ANY = -1;
  public static final int EXACTLY_ONE = 1;

  public static UbongoBoard of(String... strings) {
    // System.out.println("---");
    Tensor prep = PuzzleFree.fromString(strings);
    return new UbongoBoard(prep, UbongoPieces.list());
  }

  public static UbongoBoard of(Tensor prep) {
    return new UbongoBoard(prep.copy(), UbongoPieces.list());
  }

  /** board */
  private final Tensor mask;
  private final int dim1;
  private final int[] _mask;
  private final List<Integer> board_size;
  private final int count;
  /** all the possible locations of the oriented piece */
  private final Map<OrientedPiece, List<Pnt>> map = new IdentityHashMap<>();
  public String message = "";
  private final List<PuzzlePiece> pieces;

  public UbongoBoard(Tensor prep, List<PuzzlePiece> list) {
    mask = MatrixQ.require(prep).unmodifiable();
    Throw.unless(StaticHelper.isSingleFree(mask));
    this.pieces = list;
    board_size = Dimensions.of(prep);
    dim1 = board_size.get(1);
    count = (int) Flatten.stream(mask, 1).filter(FREE::equals).count();
    _mask = Primitives.toIntArray(Flatten.of(mask, 1));
    // ---
    for (PuzzlePiece puzzlePiece : list)
      for (OrientedPiece orientedPiece : puzzlePiece.stamps()) {
        map.put(orientedPiece, new ArrayList<>());
        Tensor stamp = orientedPiece.stamp();
        List<Integer> size = Dimensions.of(stamp);
        for (int bi = 0; bi <= board_size.get(0) - size.get(0); ++bi)
          for (int bj = 0; bj <= board_size.get(1) - size.get(1); ++bj) {
            boolean status = true;
            for (int si = 0; si < size.get(0); ++si)
              for (int sj = 0; sj < size.get(1); ++sj) {
                boolean occupied = stamp.get(si, sj).equals(RealScalar.ONE);
                if (occupied)
                  status &= prep.get(bi + si, bj + sj).equals(FREE);
              }
            if (status) {
              Tensor board = mask.copy();
              Tensor piece = ArrayPad.of(stamp, List.of(bi, bj), List.of( //
                  board_size.get(0) - size.get(0) - bi, //
                  board_size.get(1) - size.get(1) - bj));
              if (StaticHelper.isSingleFree(board.add(piece)))
                map.get(orientedPiece).add(new Pnt(bi, bj));
            }
          }
      }
  }

  public List<Integer> board_size() {
    return board_size;
  }

  public Tensor mask() {
    return mask;
  }

  public boolean isRunning = true;

  /** @param use how many pieces to use
   * @return */
  public List<UbongoSolution> filter0(int use) {
    List<List<PuzzlePiece>> values = Candidates.of(use, count(), pieces);
    message = "candidates.size = " + values.size();
    List<UbongoSolution> solutions = new LinkedList<>();
    for (List<PuzzlePiece> list : values) {
      List<PuzzlePiece> _list = new ArrayList<>(list);
      // fit "large" pieces first to reduce the search space as quickly as possible
      _list.sort((u1, u2) -> Integer.compare(u2.count(), u1.count()));
      Solve solve = new Solve(_list, 2);
      int size = solve.solutions.size();
      switch (size) {
      case 0: {
        message = _list + " ZERO solutions";
        break;
      }
      case 1: {
        solutions.add(new UbongoSolution(solve.solutions.getFirst(), solve.search));
        // System.out.println("discard=" + solve.discard);
        message = _list + " FOUND!";
        break;
      }
      default:
        message = _list + " TOO MANY solutions";
      }
      if (!isRunning)
        break;
    }
    return solutions;
  }

  public List<UbongoSolution> perCombo(int use, int max_solutions) {
    List<List<PuzzlePiece>> values = Candidates.of(use, count(), pieces);
    message = "candidates.size = " + values.size();
    List<UbongoSolution> solutions = new LinkedList<>();
    for (List<PuzzlePiece> list : values) {
      List<PuzzlePiece> _list = new ArrayList<>(list);
      // fit "large" pieces first to reduce the search space as quickly as possible
      _list.sort((u1, u2) -> Integer.compare(u2.count(), u1.count()));
      Solve solve = new Solve(_list, max_solutions);
      for (List<UbongoEntry> solution : solve.solutions)
        solutions.add(new UbongoSolution(solution, solve.search));
      if (!isRunning)
        break;
    }
    return solutions;
  }

  public int count() {
    return count;
  }

  class Solve {
    private final int max_solutions;
    private final List<List<UbongoEntry>> solutions = new LinkedList<>();
    private int search = 0;
    private boolean continueSearch = true;

    public Solve(List<PuzzlePiece> list, int max_solutions) {
      this.max_solutions = max_solutions;
      solve(_mask.clone(), list, Collections.emptyList());
    }

    private void solve(int[] board, List<PuzzlePiece> list, List<UbongoEntry> entries) {
      Throw.unless(!list.isEmpty());
      ++search;
      if (!StaticHelper.isSingleFree(Partition.of(Tensors.vectorInt(board), dim1))) {
        return;
      }
      final PuzzlePiece puzzlePiece = list.getFirst(); // piece
      for (OrientedPiece orientedPiece : puzzlePiece.stamps()) {
        Tensor stamp = orientedPiece.stamp();
        List<Pnt> points = map.get(orientedPiece); // hash by identity
        for (Pnt point : points) {
          int bi = point.i();
          int bj = point.j();
          int[] nubrd = board.clone();
          boolean status = continueSearch;
          for (Pnt pnt : orientedPiece.deltas()) {
            int index = (bi + pnt.i()) * dim1 + bj + pnt.j();
            if (nubrd[index] == free) {
              nubrd[index] = 0;
            } else {
              status = false;
              break;
            }
          }
          // ---
          if (status) {
            List<UbongoEntry> arrayList = new ArrayList<>(entries); // copy
            arrayList.add(new UbongoEntry(bi, bj, puzzlePiece, stamp));
            List<PuzzlePiece> rest = Lists.rest(list);
            if (rest.isEmpty()) {
              solutions.add(arrayList);
              continueSearch &= solutions.size() < max_solutions;
            } else if (continueSearch)
              solve(nubrd, rest, arrayList);
          }
        }
      }
    }
  }
}
