// code by jph
package ch.alpine.curios.run;

import ch.alpine.bridge.pro.RunProviderTimings;
import ch.alpine.bridge.pro.VoidProvider;

enum LocalTimings implements VoidProvider {
  INSTANCE;

  @Override
  public Void runStandalone() {
    try {
      RunProviderTimings.of("ch.alpine");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
