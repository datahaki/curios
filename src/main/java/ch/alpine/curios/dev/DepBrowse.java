// code by jph
package ch.alpine.curios.dev;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

enum DepBrowse {
  ;
  static void main() throws IOException, URISyntaxException {
    for (Dep dep : Dep.values())
      if (!dep.groupId().startsWith("io.github.datahaki")) {
        String url = dep.website();
        if (Desktop.isDesktopSupported())
          Desktop.getDesktop().browse(new URI(url));
      }
  }
}
