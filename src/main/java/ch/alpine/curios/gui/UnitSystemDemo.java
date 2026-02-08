// code by jph
package ch.alpine.curios.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import ch.alpine.bridge.lang.UnicodeString;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.ext.ResourceData;
import ch.alpine.tensor.qty.SimpleUnitSystem;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.qty.UnitSystem;
import ch.alpine.tensor.qty.UnitSystems;

/* package */ class UnitSystemDemo extends JFrame {
  private static final Font FONT = new Font(Font.DIALOG, Font.BOLD, 22);
  private static final Font AREA_FONT = new Font(Font.DIALOG, Font.PLAIN, 22);

  /* package */ static UnitSystem unitSystem() {
    return SimpleUnitSystem.from(ResourceData.properties("/ch/alpine/tensor/qty/si_reduced.properties"));
  }

  private final JTextArea jTextArea = new JTextArea();
  private final JLabel jLabel = new JLabel();
  // ---
  private UnitSystem unitSystem;

  public UnitSystemDemo() {
    super("UnitSystemDemo");
    unitSystem = unitSystem();
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    JPanel jPane = new JPanel(new BorderLayout());
    JPanel jPanel = new JPanel(new BorderLayout());
    {
      JToolBar jToolBar = new JToolBar();
      jToolBar.setFloatable(false);
      JTextField jTextFieldA = new JTextField();
      jTextFieldA.setFont(FONT);
      jTextFieldA.setText("A");
      jToolBar.add(jTextFieldA);
      JLabel arrow = new JLabel(" \u2192 "); // ->
      arrow.setFont(FONT);
      jToolBar.add(arrow);
      JTextField jTextFieldB = new JTextField();
      jTextFieldB.setText("V");
      jTextFieldB.setFont(FONT);
      jToolBar.add(jTextFieldB);
      {
        JButton jButton = new JButton("substitute");
        jButton.addActionListener(_ -> {
          String prev = jTextFieldA.getText().trim();
          String next = jTextFieldB.getText().trim();
          Set<String> base = UnitSystems.base(unitSystem);
          if (base.contains(prev))
            if (!base.contains(next))
              try {
                update(UnitSystems.rotate(unitSystem, prev, next));
                jTextFieldA.setText(next);
                jTextFieldB.setText(prev);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            else
              System.err.println("target is already a base unit: " + next);
          else
            System.err.println("not a base unit: " + prev);
        });
        jToolBar.add(jButton);
      }
      {
        JButton jButton = new JButton("reset");
        jButton.addActionListener(_ -> update(unitSystem()));
        jToolBar.add(jButton);
      }
      jPanel.add(BorderLayout.NORTH, jToolBar);
    }
    update(unitSystem);
    jTextArea.setFont(AREA_FONT);
    jPanel.add(BorderLayout.CENTER, new JScrollPane(jTextArea));
    jPane.add(BorderLayout.CENTER, jPanel);
    jLabel.setFont(FONT);
    jPane.add(BorderLayout.NORTH, jLabel);
    setContentPane(jPane);
    setBounds(100, 100, 600, 900);
  }

  private String format() {
    return unitSystem.map().keySet().stream().sorted(String::compareToIgnoreCase).map(key -> {
      Scalar value = unitSystem.map().get(key);
      Unit unit = Unit.of(key);
      return UnicodeString.of(unit) + '\t' + "" + UnicodeString.of(value) + '\n';
    }).collect(Collectors.joining());
  }

  private void update(UnitSystem unitSystem) {
    this.unitSystem = unitSystem;
    jTextArea.setText(format());
    jTextArea.setCaretPosition(0);
    jLabel.setText("base: " + UnitSystems.base(unitSystem).toString());
  }

  static void main() {
    UnitSystemDemo unitSystemDemo = new UnitSystemDemo();
    unitSystemDemo.setVisible(true);
  }
}
