// code by jph
package ch.alpine.curios.puzzle.gui;

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
import ch.alpine.curios.puzzle.UbongoBoards;
import ch.alpine.curios.puzzle.UbongoLoader;
import ch.alpine.curios.puzzle.UbongoSolution;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

@ReflectionMarker
class UbongoTree implements ManipulateProvider, RenderInterface {
  public UbongoBoards ubongoBoards = UbongoBoards.AIRPLAN1;
  private List<UbongoSolution> list;
  @FieldSelectionCallback("index")
  public Integer index = 0;
  private final GeometricComponent geometricComponent = new GeometricComponent();

  public UbongoTree() {
    geometricComponent.addRenderInterface(this);
  }

  public List<Scalar> index() {
    return Objects.isNull(list) //
        ? List.of()
        : IntStream.range(0, list.size()).mapToObj(RealScalar::of).toList();
  }

  @Override
  public void render(GeometricLayer geometricLayer, Graphics2D graphics) {
    int i = Math.min(Math.max(0, index), list.size() - 1);
    UbongoSolution ubongoSolution = list.get(i);
    StaticHelper.drawBoard(graphics, ubongoBoards.board(), ubongoSolution.list());
  }

  @Override
  public Container getContainer() {
    list = UbongoLoader.INSTANCE.load(ubongoBoards);
    return geometricComponent.jComponent;
  }

  static void main() {
    new UbongoTree().runStandalone();
  }
}
