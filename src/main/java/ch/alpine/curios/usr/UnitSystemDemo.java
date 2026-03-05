// code by jph
package ch.alpine.curios.usr;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Window;
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

import ch.alpine.bridge.lang.UnicodeString;
import ch.alpine.bridge.pro.WindowProvider;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.ext.ResourceData;
import ch.alpine.tensor.qty.SimpleUnitSystem;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.qty.UnitSystem;
import ch.alpine.tensor.qty.UnitSystems;

class UnitSystemDemo implements WindowProvider {
  private static final Font FONT = new Font(Font.DIALOG, Font.BOLD, 22);
  private static final Font AREA_FONT = new Font(Font.DIALOG, Font.PLAIN, 22);

  /* package */ static UnitSystem unitSystem() {
    return SimpleUnitSystem.from(ResourceData.properties("/ch/alpine/tensor/qty/si_reduced.properties"));
  }

  private final JFrame jFrame = new JFrame();
  private final JTextArea jTextArea = new JTextArea();
  private final JLabel jLabel = new JLabel();
  // ---
  private UnitSystem unitSystem;

  public UnitSystemDemo() {
    unitSystem = unitSystem();
    JPanel jPane = new JPanel(new BorderLayout());
    JPanel jPanel = new JPanel(new BorderLayout());
    {
      JToolBar jToolBar = new JToolBar();
      jToolBar.setFloatable(false);
      JTextField jTextFieldA = new JTextField();
      JTextField jTextFieldB = new JTextField();
      // ---
      {
        jTextFieldA.setFont(FONT);
        jTextFieldA.setText("A");
        jToolBar.add(jTextFieldA);
      }
      {
        JButton jButton = new JButton(" \u2192 ");
        jButton.setFont(FONT);
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
        jTextFieldB.setText("V");
        jTextFieldB.setFont(FONT);
        jToolBar.add(jTextFieldB);
      }
      {
        JButton jButton = new JButton("reset");
        jButton.addActionListener(_ -> {
          update(unitSystem());
          jTextFieldA.setText("A");
          jTextFieldB.setText("V");
        });
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
    jFrame.setContentPane(jPane);
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

  @Override
  public Window getWindow() {
    return jFrame;
  }

  static void main() {
    new UnitSystemDemo().runStandalone();
  }
}
