// code by jph
package ch.alpine.curios.puzzle.gui;

import java.util.List;

import ch.alpine.curios.puzzle.UbongoBoard;
import ch.alpine.curios.puzzle.UbongoSolution;

class SolveThread extends Thread {
  public final UbongoBoard ubongoBoard;

  public SolveThread(UbongoBoard ubongoBoard, int use) {
    super(() -> {
      List<UbongoSolution> list = ubongoBoard.filter0(use);
      if (list.isEmpty()) {
        System.err.println("no solutions");
      } else {
        UbongoBrowser ubongoBrowser = new UbongoBrowser(ubongoBoard, list);
        ubongoBrowser.runStandalone();
      }
    });
    this.ubongoBoard = ubongoBoard;
    start();
  }

  public String getMessage() {
    return ubongoBoard.message;
  }

  public void cancel() {
    ubongoBoard.isRunning = false;
  }
}
