// code by omk
package ch.alpine.curios.ref;

import java.time.LocalDateTime;

import javax.swing.JDialog;

import ch.alpine.bridge.swing.DialogBuilder;
import ch.alpine.bridge.swing.LocalDateTimeDialog;
import ch.alpine.bridge.swing.LookAndFeels;

enum LocalDateTimeDialogDemo {
  ;
  static void main() {
    LookAndFeels.LIGHT.updateComponentTreeUI();
    LocalDateTimeDialog localDateTimeDialog = new LocalDateTimeDialog(LocalDateTime.now()) {
      @Override
      public void selection(LocalDateTime current) {
        // ---
      }
    };
    JDialog jDialog = DialogBuilder.create(null, localDateTimeDialog);
    jDialog.setLocation(100, 200);
    jDialog.setVisible(true);
  }
}
