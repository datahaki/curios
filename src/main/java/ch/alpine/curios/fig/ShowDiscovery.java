// code by jph
package ch.alpine.curios.fig;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import ch.alpine.bridge.awt.WindowBounds;
import ch.alpine.bridge.io.ResourceLocator;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.bridge.swing.LookAndFeels;
import ch.alpine.tensor.ext.ref.InstanceDiscovery;

enum ShowDiscovery {
  ;
  static void main() {
    LookAndFeels.autoDetect();
    // ---
    List<ShowProvider> list = InstanceDiscovery.of("ch.alpine", ShowProvider.class);
    JFrame jFrame = new JFrame();
    JPanel jPanel = new JPanel(new GridLayout(list.size(), 1));
    {
      for (ShowProvider showProvider : list) {
        JButton jButton = new JButton(showProvider.getClass().getSimpleName());
        jButton.addActionListener(_ -> showProvider.run());
        jPanel.add(jButton);
      }
    }
    jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    jFrame.setContentPane(new JScrollPane(jPanel));
    ResourceLocator resourceLocator = ResourceLocator.of(ShowDiscovery.class);
    WindowBounds.persistent(jFrame, resourceLocator.properties(WindowBounds.class));
    jFrame.setVisible(true);
  }
}
