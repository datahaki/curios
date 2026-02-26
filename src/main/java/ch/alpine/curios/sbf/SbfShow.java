// code by jph
package ch.alpine.curios.sbf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import ch.alpine.bridge.awt.WindowClosed;
import ch.alpine.bridge.pro.WindowProvider;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.TensorMap;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.ColorDataIndexed;
import ch.alpine.tensor.img.ImageResize;
import ch.alpine.tensor.img.StrictColorDataIndexed;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.tensor.num.Boole;
import ch.alpine.tensor.num.RandomPermutation;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.CategoricalDistribution;
import ch.alpine.tensor.red.Tally;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Ramp;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Power;

class SbfShow implements WindowProvider {
  public static final int MAX = 3;
  public static final ColorDataIndexed COLOR_DATA_INDEXED = StrictColorDataIndexed.of(Subdivide.of(0, 1, MAX).maps(ColorDataGradients.AVOCADO));
  private final List<SbfItem> sbfItems;
  private final SbfTrack sbfTrack;
  public SbfItem sbfItemCurrent;
  public final JFrame jFrame = new JFrame();
  public final JPanel jPanel = new JPanel(new BorderLayout());
  public final JLabel jLabel = new JLabel();
  public final JToggleButton jToggleButton = new JToggleButton("consec");
  public final JButton jButtonSkip = new JButton("skip");
  public final JButton jButton = new JButton("purify");
  public final JToolBar jToolBar = new JToolBar();
  public final JLabel progress = new JLabel();
  public final JTextPane jTextPane = new JTextPane();
  public final Document document = jTextPane.getDocument();
  private int correct = -1;
  private int INDEX = -1;

  public SbfShow(SbfType sbfType) throws IOException {
    sbfItems = SbfParser.get(sbfType);
    sbfTrack = new SbfTrack(sbfType, sbfItems.size());
    jFrame.setTitle(sbfType.name());
    jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    WindowClosed.runs(jFrame, sbfTrack::store);
    jLabel.setPreferredSize(new Dimension(200, 200));
    JPanel jPanelGrid = new JPanel(new GridLayout(3, 1));
    jPanelGrid.add(jToggleButton);
    jButtonSkip.addActionListener(_ -> shuffle());
    jPanelGrid.add(jButtonSkip);
    jToggleButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (jToggleButton.isSelected())
          INDEX = -1;
        shuffle();
      }
    });
    jPanelGrid.add(jButton);
    {
      JPanel jPanelEast = new JPanel(new BorderLayout());
      jPanelEast.add(BorderLayout.NORTH, jLabel);
      jPanelEast.add(BorderLayout.SOUTH, jPanelGrid);
      jPanel.add(BorderLayout.EAST, jPanelEast);
    }
    jTextPane.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
    jTextPane.setEditable(false);
    jPanel.add(BorderLayout.CENTER, jTextPane);
    for (int c = 0; c < 4; ++c) {
      final int fc = c;
      JButton jButton = new JButton("" + c);
      jButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (correct == fc) {
            sbfTrack.tensor.set(s -> s.append(RealScalar.ONE), INDEX);
          } else {
            sbfTrack.tensor.set(s -> s.append(RealScalar.ZERO), INDEX);
            JOptionPane.showMessageDialog(jToolBar, sbfItemCurrent.answers.getFirst());
          }
          shuffle();
        }
      });
      jToolBar.add(jButton);
      jToolBar.addSeparator();
    }
    jToolBar.add(progress);
    jToolBar.setFloatable(false);
    jPanel.add(BorderLayout.SOUTH, jToolBar);
    jFrame.setContentPane(jPanel);
    shuffle();
    jButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Tensor tensor = sbfTrack.tensor.maps(s -> Boole.of(Scalars.isZero(s)));
        Tensor totals = TensorMap.of(Total::ofVector, tensor, 1);
        System.out.println(Tally.of(totals));
        for (int c = 0; c < totals.length(); ++c)
          if (Sign.isPositive(totals.Get(c)))
            sbfTrack.tensor.set(Tensors.empty(), c);
        shuffle();
      }
    });
    jFrame.setBounds(100, 100, 700, 500);
  }

  public Scalar todo(Tensor vector) {
    return Ramp.FUNCTION.apply(RealScalar.of(MAX).subtract(Total.ofVector(vector)));
  }

  public Scalar prob(Tensor vector) {
    int base = sbfItems.size();
    return Power.of(base, todo(vector));
  }

  private BufferedImage progressImage() {
    Tensor unscaledPDF = Tensors.of(TensorMap.of(this::todo, sbfTrack.tensor, 1));
    Tensor res = unscaledPDF.maps(COLOR_DATA_INDEXED);
    res = ImageResize.nearest(res, 20, 3);
    return ImageFormat.of(res);
  }

  private void shuffle() {
    if (jToggleButton.isSelected()) {
      ++INDEX;
      INDEX %= sbfItems.size();
    } else {
      Tensor unscaledPDF = TensorMap.of(this::prob, sbfTrack.tensor, 1);
      // System.out.println(Total.of(unscaledPDF));
      Distribution distribution = CategoricalDistribution.fromUnscaledPDF(unscaledPDF);
      INDEX = RandomVariate.of(distribution).number().intValue();
    }
    sbfItemCurrent = sbfItems.get(INDEX);
    set(sbfItemCurrent);
  }

  private void set(SbfItem sbfItem) {
    try {
      document.remove(0, document.getLength());
      String s = sbfItem.question;
      int[] index = RandomPermutation.of(sbfItem.answers.size());
      for (int c = 0; c < 4; c++) {
        s += "\n\n" + c + ". " + sbfItem.answers.get(index[c]);
        if (index[c] == 0)
          correct = c;
      }
      document.insertString(0, s, null);
      progress.setIcon(new ImageIcon(progressImage(), "desc"));
      if (sbfItem.withoutImages()) {
        jLabel.setIcon(null);
      } else {
        try (InputStream inputStream = Files.newInputStream(sbfItem.gfx)) {
          BufferedImage bufferedImage = ImageIO.read(inputStream);
          jLabel.setIcon(new ImageIcon(bufferedImage, "desc"));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Window getWindow() {
    return jFrame;
  }

  static void main() throws IOException {
    new SbfShow(SbfType.binnen).runStandalone();
  }
}
