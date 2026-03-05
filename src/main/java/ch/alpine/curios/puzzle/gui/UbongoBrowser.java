// code by jph
package ch.alpine.curios.puzzle.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import ch.alpine.bridge.gfx.GeometricComponent;
import ch.alpine.bridge.gfx.GeometricLayer;
import ch.alpine.bridge.gfx.RenderInterface;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.curios.puzzle.UbongoBoard;
import ch.alpine.curios.puzzle.UbongoBoards;
import ch.alpine.curios.puzzle.UbongoLoader;
import ch.alpine.curios.puzzle.UbongoSolution;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

@ReflectionMarker
public class UbongoBrowser implements ManipulateProvider, RenderInterface {
  public static UbongoBrowser create(UbongoBoards ubongoBoards) {
    return new UbongoBrowser(ubongoBoards.board(), UbongoLoader.INSTANCE.load(ubongoBoards));
  }

  public static final UbongoBrowser INSTANCE = create(UbongoBoards.KIRCH06);
  // ---
  private final GeometricComponent geometricComponent = new GeometricComponent();
  private final UbongoBoard ubongoBoard;
  private final List<UbongoSolution> list;
  @FieldSelectionCallback("index")
  public Integer index = 0;

  public List<Scalar> index() {
    return IntStream.range(0, list.size()).mapToObj(RealScalar::of).toList();
  }

  public UbongoBrowser(UbongoBoard ubongoBoard, List<UbongoSolution> list) {
    this.ubongoBoard = Objects.requireNonNull(ubongoBoard);
    this.list = Objects.requireNonNull(list);
    geometricComponent.addRenderInterface(this);
  }

  @Override
  public void render(GeometricLayer geometricLayer, Graphics2D graphics) {
    int read = Math.min(Math.max(0, index), list.size() - 1);
    if (0 <= read) {
      UbongoSolution ubongoSolution = list.get(read);
      StaticHelper.drawBoard(graphics, ubongoBoard, ubongoSolution.list());
      graphics.setColor(Color.DARK_GRAY);
      graphics.drawString("depth=" + ubongoSolution.search(), 100, 12);
    }
  }

  @Override
  public Container getContainer() {
    return geometricComponent;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
