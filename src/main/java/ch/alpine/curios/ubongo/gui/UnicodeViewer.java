// code by jph
package ch.alpine.curios.ubongo.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;

@ReflectionMarker
public class UnicodeViewer implements ManipulateProvider {
  @FieldSelectionArray({ "27", "28", "29" })
  public String hex = "27";
  @FieldClip(min = "30", max = "50")
  public Integer size = 40;
  private final JComponent jComponent = new JComponent() {
    @Override
    protected void paintComponent(Graphics graphics) {
      // Font font = new Font("Noto Color qwe qweEmoji", Font.PLAIN, 24);
      graphics.setColor(Color.DARK_GRAY);
      graphics.setFont(new Font(Font.DIALOG, Font.PLAIN, size));
      int ofs = Integer.parseInt(hex, 16) * 256;
      for (int i = 0; i < 16; ++i) {
        for (int j = 0; j < 16; ++j) {
          int res = ofs + i * 16 + j;
          char chr = (char) res;
          graphics.drawString("" + chr, j * size, (i + 1) * size);
        }
      }
    };
  };

  @Override
  public Container getContainer() {
    return jComponent;
  }

  static void main() {
    new UnicodeViewer().runStandalone();
  }
}
