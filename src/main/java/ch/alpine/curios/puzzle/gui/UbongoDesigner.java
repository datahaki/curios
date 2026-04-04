// code by jph
package ch.alpine.curios.puzzle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import ch.alpine.bridge.gfx.AffineTransforms;
import ch.alpine.bridge.gfx.GeometricComponent;
import ch.alpine.bridge.gfx.GeometricLayer;
import ch.alpine.bridge.gfx.PvmBuilder;
import ch.alpine.bridge.gfx.RenderInterface;
import ch.alpine.bridge.io.ResourceLocator;
import ch.alpine.bridge.pro.WindowProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldFuse;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.bridge.ref.util.FieldsEditor;
import ch.alpine.bridge.ref.util.ToolbarFieldsEditor;
import ch.alpine.curios.puzzle.Candidates;
import ch.alpine.curios.puzzle.PuzzlePiece;
import ch.alpine.curios.puzzle.UbongoBoard;
import ch.alpine.curios.puzzle.UbongoBoards;
import ch.alpine.curios.puzzle.UbongoPieces;
import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ArrayPad;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.img.ImageCrop;
import ch.alpine.tensor.img.ImageRotate;
import ch.alpine.tensor.io.Export;
import ch.alpine.tensor.io.Import;
import ch.alpine.tensor.sca.Floor;

class UbongoDesigner implements WindowProvider, RenderInterface {
  private static final int EXT = 11;
  public static final Scalar FREE = UbongoBoard.FREE;
  static final Collector<CharSequence, ?, String> EMBRACE = //
      Collectors.joining("", "\"", "\"");
  static final Collector<CharSequence, ?, String> EMBRACE2 = //
      Collectors.joining(", ", "", "");

  private static String rowToString(Tensor row) {
    return row.stream().map(s -> s.equals(FREE) ? "o" : " ").collect(EMBRACE);
  }

  @ReflectionMarker
  static class Param {
    @FieldClip(min = "1", max = "12")
    public Integer num = 4;
    @FieldFuse
    public Boolean solve = false;
    @FieldFuse
    public Boolean rotCw = false;
    @FieldFuse
    public Boolean reset = false;
    public String string = "UNTITLED";
    @FieldFuse
    public Boolean print = false;
  }

  @ReflectionMarker
  static class Paran {
    public UbongoBoards ubongoBoards = UbongoBoards.STANDARD;
  }

  private final JFrame jFrame = new JFrame();
  private final JPanel jPanel = new JPanel(new BorderLayout());
  private final JToolBar jToolBar = new JToolBar();
  GeometricComponent geometricComponent = new GeometricComponent();
  private final ResourceLocator resourceLocator = ResourceLocator.of(getClass());
  private final Param param = new Param();
  private final Paran paran = new Paran();
  // private final FixGridRender gridRender;
  private Tensor template = Array.zeros(EXT, EXT);
  private SolveThread solveThread = null;
  private final Path FILE;

  public UbongoDesigner() {
    jToolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    jToolBar.setFloatable(false);
    FILE = resourceLocator.resolve(UbongoDesigner.class.getSimpleName() + ".csv");
    FieldsEditor fieldsEditor0 = ToolbarFieldsEditor.addToComponent(param, jToolBar);
    fieldsEditor0.addUniversalListener(this::run2);
    {
      FieldsEditor fieldsEditor = ToolbarFieldsEditor.addToComponent(paran, jToolBar);
      fieldsEditor.addUniversalListener(() -> {
        center(paran.ubongoBoards.board().mask());
        param.num = paran.ubongoBoards.use();
        fieldsEditor0.updateJComponents();
        geometricComponent.repaint();
      });
    }
    try {
      template = Import.of(FILE);
    } catch (Exception e) {
      System.err.println("does not exist: " + FILE);
    }
    center(template);
    // ---
    Tensor pvm = PvmBuilder.rot().setOffset(100, 100).setPerPixel(30).digest();
    geometricComponent.setModel2Pixel(pvm);
    int row_max = template.length();
    int col_max = Unprotect.dimension1(template);
    // gridRender = new FixGridRender(Subdivide.of(0, row_max, row_max), Subdivide.of(0, col_max, col_max));
    geometricComponent.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1) {
          Tensor xya = geometricComponent.getMouseSe2CState().maps(Floor.FUNCTION);
          int row = xya.Get(0).number().intValue();
          int col = xya.Get(1).number().intValue();
          if (0 <= row && row < row_max)
            if (0 <= col && col < col_max) {
              boolean free = template.get(row, col).equals(FREE);
              template.set(free ? RealScalar.ZERO : FREE, row, col);
            }
          geometricComponent.repaint();
        }
      }
    });
    geometricComponent.addRenderInterface(this);
    jPanel.add(jToolBar, BorderLayout.NORTH);
    jPanel.add(geometricComponent, BorderLayout.CENTER);
    jFrame.setContentPane(jPanel);
  }

  @Override
  public void render(GeometricLayer geometricLayer, Graphics2D graphics) {
    // new GridRender(() -> geometricComponent.jComponent.getSize()).render(geometricLayer, graphics);
    graphics.setColor(Color.DARK_GRAY);
    int dimension1 = Unprotect.dimension1(template);
    for (int row = 0; row < template.length(); ++row) {
      for (int col = 0; col < dimension1; ++col) {
        Scalar scalar = template.Get(row, col);
        if (!scalar.equals(FREE)) {
          geometricLayer.pushMatrix(Se2Matrix.translation(Tensors.vector(row, col)));
          Path2D path2d = geometricLayer.toPath2D(Tensors.fromString("{{0,0},{1,0},{1,1},{0,1}}"));
          graphics.fill(path2d);
          geometricLayer.popMatrix();
        }
      }
    }
    // gridRender.render(geometricLayer, graphics);
    {
      int count = (int) Flatten.stream(template, -1).filter(FREE::equals).count();
      graphics.setColor(Color.DARK_GRAY);
      graphics.drawString("free=" + count, 0, 12);
      List<List<PuzzlePiece>> candidates = Candidates.of(param.num, count, UbongoPieces.list());
      if (candidates.isEmpty()) {
        graphics.setColor(Color.RED);
        graphics.drawString("CANDIDATE SET EMPTY", 0, 50);
      } else {
        graphics.drawString("comb=" + candidates.size(), 0, 50);
      }
    }
    SolveThread _solveThread = solveThread;
    if (Objects.nonNull(_solveThread)) {
      if (_solveThread.isAlive()) {
        graphics.drawString("computing: " + _solveThread.getMessage(), 0, 80);
      } else {
        // System.out.println("finished thread");
        solveThread = null;
      }
    }
    if (false) {
      FontRenderContext fontRenderContext = graphics.getFontRenderContext();
      Font font = new Font("Helvetica", 1, 60);
      String string = new String("Java Source and Support.");
      TextLayout textLayout = new TextLayout(string, font, fontRenderContext);
      AffineTransform transform = AffineTransforms.of(Se2Matrix.of(Tensors.vector(0, 100, 0)));
      Shape shape = textLayout.getOutline(null);
      graphics.transform(transform);
      graphics.setColor(Color.RED);
      graphics.fill(shape);
      graphics.setColor(Color.BLUE);
      graphics.draw(shape);
    }
  }

  public void run2() {
    if (param.reset) {
      param.reset = false;
      template.set(Scalar::zero, Tensor.ALL, Tensor.ALL);
    }
    if (param.rotCw) {
      param.rotCw = false;
      template = ImageRotate.CW.apply(template);
    }
    if (param.solve) {
      param.solve = false;
      if (Objects.isNull(solveThread)) {
        try {
          Export.of(FILE, template);
        } catch (IOException ioException) {
          throw new UncheckedIOException(ioException);
        }
        Tensor result = ImageCrop.eq(RealScalar.ZERO).apply(template);
        solveThread = new SolveThread(UbongoBoard.of(result), param.num);
      } else {
        solveThread.cancel();
        System.out.println("cancel issued");
      }
    }
    if (param.print) {
      param.print = false;
      System.out.println("=".repeat(32));
      int use = param.num;
      Tensor result = ImageCrop.eq(RealScalar.ZERO).apply(template);
      String collect = result.stream().map(UbongoDesigner::rowToString).collect(EMBRACE2);
      System.out.printf("%s(%d, %s),\n", param.string, use, collect);
    }
  }

  private void center(Tensor mask) {
    List<Integer> list = Dimensions.of(mask);
    final int def0 = EXT - list.get(0);
    final int def1 = EXT - list.get(1);
    final int beg0 = def0 / 2;
    final int beg1 = def1 / 2;
    final int end0 = def0 - beg0;
    final int end1 = def1 - beg1;
    template = ArrayPad.of(mask, List.of(beg0, beg1), List.of(end0, end1));
  }

  @Override
  public Window getWindow() {
    return jFrame;
  }

  static void main() {
    new UbongoDesigner().runStandalone();
  }
}
