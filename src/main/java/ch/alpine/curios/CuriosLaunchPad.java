// code by jph
package ch.alpine.curios;

import ch.alpine.bridge.pro.RunLaunchPad;

enum CuriosLaunchPad {
  ;
  static void main() {
    RunLaunchPad.create(CuriosLaunchPad.class.getPackageName()).runStandalone();
  }
}
