// code by jph
package ch.alpine.curios.dev;

import java.awt.Desktop;
import java.net.URI;

enum MavenDepBrowse {
  ;
  static void main() {
    for (Dep dep : Dep.values())
      if (!dep.groupId().startsWith("io.github.datahaki")) {
        String url = dep.website();
        if (Desktop.isDesktopSupported())
          try {
            Desktop.getDesktop().browse(new URI(url));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
      }
  }
}
