// code by jph
package ch.alpine.curios;

import java.awt.GridLayout;
import java.awt.Window;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import ch.alpine.bridge.cgr.InstanceDiscovery;
import ch.alpine.bridge.lang.FriendlyFormat;
import ch.alpine.bridge.pro.RunProvider;
import ch.alpine.bridge.pro.WindowProvider;

enum RunDiscovery implements WindowProvider {
  INSTANCE;

  @Override
  public Window getWindow() {
    JFrame jFrame = new JFrame();
    List<Supplier<RunProvider>> list = //
        InstanceDiscovery.of(getClass().getPackageName(), RunProvider.class);
    JPanel jPanel = new JPanel(new GridLayout(list.size(), 1));
    for (Supplier<RunProvider> supplier : list) {
      RunProvider runProvider = supplier.get();
      JButton jButton = new JButton(FriendlyFormat.defaultTitle(runProvider.getClass()));
      jButton.addActionListener(_ -> supplier.get().runStandalone());
      jPanel.add(jButton);
    }
    jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    JScrollPane jScrollPane = new JScrollPane(jPanel);
    jScrollPane.getVerticalScrollBar().setUnitIncrement(25);
    jFrame.setContentPane(jScrollPane);
    return jFrame;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
