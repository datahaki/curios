// code by jph
package ch.alpine.curios.ref;

import java.time.LocalTime;

import javax.swing.JDialog;

import ch.alpine.bridge.swing.DialogBuilder;
import ch.alpine.bridge.swing.LocalTimeDialog;
import ch.alpine.bridge.swing.LookAndFeels;

enum LocalTimeDialogDemo {
  ;
  static void main() {
    LookAndFeels.LIGHT.updateComponentTreeUI();
    DialogBuilder<LocalTime> dialogBuilder = new LocalTimeDialog(LocalTime.now()) {
      @Override
      public void selection(LocalTime current) {
        // ---
      }
    };
    JDialog jDialog = DialogBuilder.create(null, dialogBuilder);
    jDialog.setLocation(100, 200);
    jDialog.setVisible(true);
  }
}
