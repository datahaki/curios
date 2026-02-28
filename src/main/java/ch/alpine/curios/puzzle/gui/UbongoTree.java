// code by jph
package ch.alpine.curios.puzzle.gui;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import ch.alpine.ascony.win.AbstractDemo;
import ch.alpine.bridge.gfx.GeometricLayer;
import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.curios.puzzle.UbongoBoards;
import ch.alpine.curios.puzzle.UbongoLoader;
import ch.alpine.curios.puzzle.UbongoSolution;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

public class UbongoTree extends AbstractDemo {
  @ReflectionMarker
  public static class Param {
    public UbongoBoards ubongoBoards = UbongoBoards.AIRPLAN1;
    private List<UbongoSolution> list;
    @FieldSelectionCallback("index")
    public Integer index = 0;

    public List<Scalar> index() {
      return Objects.isNull(list) //
          ? List.of()
          : IntStream.range(0, list.size()).mapToObj(RealScalar::of).toList();
    }

    public void update() {
      list = UbongoLoader.INSTANCE.load(ubongoBoards);
    }

    public UbongoSolution getSolution() {
      return list.get(index);
    }
  }

  private final Param param;

  public UbongoTree() {
    this(new Param());
  }

  public UbongoTree(Param param) {
    super(param);
    this.param = param;
    param.update();
    fieldsEditor(0).addUniversalListener(param::update);
  }

  @Override
  public void render(GeometricLayer geometricLayer, Graphics2D graphics) {
    if (param.list.size() <= param.index) {
      param.index = 0;
      fieldsEditor(0).updateJComponents();
    }
    StaticHelper.drawBoard(graphics, param.ubongoBoards.board(), param.getSolution().list());
  }

  static void main() {
    new UbongoTree().runStandalone();
  }
}
