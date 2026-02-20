// code by jph
package ch.alpine.curios.run;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import ch.alpine.bridge.pro.RunProvider;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.ext.BoundedLinkedList;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;

/* package */ enum BoundedLinkedListDemo implements RunProvider {
  INSTANCE;

  @Override
  public Void runStandalone() {
    UncaughtExceptionHandler uncaughtExceptionHandler = (_, e) -> {
      throw new RuntimeException(e);
    };
    Timing timing = Timing.started();
    Scalar timeout = Quantity.of(1, "s");
    BoundedLinkedList<Integer> boundedLinkedList = new BoundedLinkedList<>(12);
    RandomGenerator randomGenerator = ThreadLocalRandom.current();
    {
      Thread thread = new Thread(() -> {
        System.out.println("runA1");
        while (Scalars.lessThan(timing.seconds(), timeout))
          synchronized (boundedLinkedList) {
            boundedLinkedList.add(randomGenerator.nextInt());
          }
      });
      thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
      thread.start();
    }
    {
      Thread thread = new Thread(() -> {
        System.out.println("runA2");
        while (Scalars.lessThan(timing.seconds(), timeout)) {
          synchronized (boundedLinkedList) {
            boundedLinkedList.add(randomGenerator.nextInt());
          }
        }
      });
      thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
      thread.start();
    }
    {
      Thread thread = new Thread(() -> {
        System.out.println("runR");
        while (Scalars.lessThan(timing.seconds(), timeout)) {
          synchronized (boundedLinkedList) {
            if (!boundedLinkedList.isEmpty())
              boundedLinkedList.poll();
          }
        }
      });
      thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
      thread.start();
    }
    return null;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
