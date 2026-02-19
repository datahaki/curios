// code by omk
package ch.alpine.curios.ref;

import java.time.LocalDate;

import javax.swing.JDialog;

import ch.alpine.bridge.swing.DialogBuilder;
import ch.alpine.bridge.swing.LocalDateDialog;
import ch.alpine.bridge.swing.LookAndFeels;

enum LocalDateDialogDemo {
  ;
  static void main() {
    LookAndFeels.autoDetect();
    LocalDateDialog fontDialog = new LocalDateDialog(LocalDate.now()) {
      @Override
      public void selection(LocalDate current) {
        // ---
      }
    };
    JDialog jDialog = DialogBuilder.create(null, fontDialog);
    jDialog.setLocation(100, 200);
    jDialog.setVisible(true);
  }
}
