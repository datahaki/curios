// code by jph
package ch.alpine.curios.boat;

import java.awt.Container;
import java.awt.Font;

import javax.swing.JTextArea;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;

@ReflectionMarker
class BoatEditor implements ManipulateProvider {
  public final BoatObject boatObject = new BoatObject(Boats.MONSUN_31.boat);
  private final JTextArea jTextArea = new JTextArea();

  public BoatEditor() {
    jTextArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
  }

  @Override
  public Container getContainer() {
    String string = boatObject.create().textValues();
    jTextArea.setText(string);
    return jTextArea;
  }

  static void main() {
    new BoatEditor().runStandalone();
  }
}
