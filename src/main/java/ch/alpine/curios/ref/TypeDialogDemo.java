// code by jph
package ch.alpine.curios.ref;

import java.awt.Container;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.JTextArea;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.bridge.ref.util.ObjectProperties;

@ReflectionMarker
class TypeDialogDemo implements ManipulateProvider {
  public Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
  public LocalDate localDate = LocalDate.now();
  public LocalTime localTime = LocalTime.now();
  public LocalDateTime localDateTime = LocalDateTime.now();
  // ---
  private final JTextArea jTextArea = new JTextArea();

  public TypeDialogDemo() {
    jTextArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
  }

  @Override
  public Container getContainer() {
    jTextArea.setText(ObjectProperties.join(this));
    return jTextArea;
  }

  static void main() {
    new TypeDialogDemo().runStandalone();
  }
}
