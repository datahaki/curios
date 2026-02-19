// code by jph
package ch.alpine.curios.ref;

import java.awt.Font;

import javax.swing.JDialog;

import ch.alpine.bridge.swing.DialogBuilder;
import ch.alpine.bridge.swing.FontDialog;
import ch.alpine.bridge.swing.LookAndFeels;

enum FontDialogDemo {
  ;
  static void main() {
    LookAndFeels.autoDetect();
    FontDialog fontDialog = new FontDialog(new Font(Font.DIALOG_INPUT, Font.BOLD, 34)) {
      @Override
      public void selection(Font current) {
        // ---
      }
    };
    JDialog jDialog = DialogBuilder.create(null, fontDialog);
    jDialog.setLocation(100, 200);
    jDialog.setVisible(true);
  }
}
