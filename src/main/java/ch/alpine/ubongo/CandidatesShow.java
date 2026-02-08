// code by jph
package ch.alpine.ubongo;

import java.awt.Color;
import java.util.List;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Join;

enum CandidatesShow implements ShowProvider {
  UBONGO(UbongoPieces.list()),
  CAESAR(CaesarPieces.list());

  private final List<PuzzlePiece> puzzlePieces;

  CandidatesShow(List<PuzzlePiece> list) {
    this.puzzlePieces = list;
  }

  @Override
  public Show getShow() {
    Show show = new Show();
    Tensor pnts = Tensors.empty();
    int max = puzzlePieces.stream().mapToInt(pp -> pp.count()).sum();
    for (int use = 1; use <= puzzlePieces.size(); ++use) {
      Tensor xy = Tensors.empty();
      for (int count = 2; count <= max; ++count) {
        List<List<PuzzlePiece>> list = Candidates.of(use, count, puzzlePieces);
        if (0 < list.size())
          xy.append(Tensors.vectorInt(count, list.size()));
      }
      show.add(ListLinePlot.of(xy)).setLabel("" + use);
      pnts = Join.of(pnts, xy);
    }
    show.add(ListPlot.of(pnts)).setColor(Color.BLACK);
    show.setPlotLabel("Candidates " + name());
    return show;
  }

  static void main() {
    UBONGO.run();
    CAESAR.run();
  }
}
