// code by jph
package ch.alpine.curios;

import ch.alpine.bridge.io.FileBlock;
import ch.alpine.bridge.io.ResourceLocator;
import ch.alpine.bridge.pro.RunLaunchPad;

/** entry point to launch miniatures */
enum CuriosLaunchPad {
  ;
  static void main() {
    if (!FileBlock.of(ResourceLocator.of(CuriosLaunchPad.class).resolve("")))
      RunLaunchPad.create(CuriosLaunchPad.class.getPackageName()).runStandalone();
  }
}
