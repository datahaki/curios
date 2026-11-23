// code by jph
package ch.alpine.curios;

import ch.alpine.bridge.pro.RunLaunchPad;

/** entry point to launch miniatures */
enum LocalLaunchPad {
  ;
  static void main() throws Exception {
    RunLaunchPad.create(LocalLaunchPad.class.getPackageName()).runStandalone();
  }
}
