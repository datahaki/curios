// code by jph
package ch.alpine.curios.run;

import java.lang.Thread.UncaughtExceptionHandler;

import ch.alpine.bridge.pro.RunProvider;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;
import ch.alpine.tensor.tmp.ResamplingMethod;
import ch.alpine.tensor.tmp.TimeSeries;

/** the demo shows that the 2 synchronizations are both necessary and sufficient.
 * removing either of the two "synchronized (timeSeries)" will immediately cause
 * a ConcurrentModificationException to be thrown
 * 
 * on jans pc the demo runs for 5[s] after which the following lines are printed:
 * 
 * element count=3632
 * iterations=3611 */
/* package */ enum SynchronizationDemo implements RunProvider {
  INSTANCE;

  private static final Scalar SEC = Quantity.of(1, "s");

  private static Scalar spawn() {
    return RandomVariate.of(NormalDistribution.standard());
  }

  public static void launchThread(Timing timing, TimeSeries timeSeries) {
    UncaughtExceptionHandler uncaughtExceptionHandler = (_, e) -> {
      throw new RuntimeException(e);
    };
    Thread thread = new Thread(() -> {
      int iterations = 0;
      while (Scalars.lessThan(timing.seconds(), SEC))
        // removing the following line: "synchronized (timeSeries)"
        // ... causes the demo to immediately throw a ConcurrentModificationException
        synchronized (timeSeries) // comment out line in order for demo to crash immediately
        //
        {
          Scalar sum = RealScalar.ZERO;
          for (Scalar scalar : timeSeries.keySet(timeSeries.domain(), true))
            sum = sum.add(scalar);
          ++iterations;
        }
      System.out.println("iterations=" + iterations);
    });
    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
    thread.start();
  }

  @Override
  public void runStandalone() {
    TimeSeries timeSeries = TimeSeries.empty(ResamplingMethod.HOLD_VALUE_FROM_LEFT);
    Timing timing = Timing.started();
    launchThread(timing, timeSeries);
    while (Scalars.lessThan(timing.seconds(), SEC)) {
      // removing the following line: "synchronized (timeSeries)"
      // ... causes the demo to immediately throw a ConcurrentModificationException
      synchronized (timeSeries) // comment out line in order for demo to crash immediately
      //
      {
        timeSeries.insert(spawn(), spawn());
      }
    }
    System.out.println("element count=" + timeSeries.size());
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
