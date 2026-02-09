// code by jph
package ch.alpine.curios.usr;

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.ext.BoundedLinkedList;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Timing;

public enum BoundedLinkedListDemo {
  ;
  static void main() {
    Timing timing = Timing.started();
    Scalar timeout = Quantity.of(3, "s");
    BoundedLinkedList<Integer> boundedLinkedList = new BoundedLinkedList<>(12);
    RandomGenerator randomGenerator = ThreadLocalRandom.current();
    new Thread(() -> {
      System.out.println("runA1");
      while (Scalars.lessThan(timing.seconds(), timeout))
        synchronized (boundedLinkedList) {
          boundedLinkedList.add(randomGenerator.nextInt());
        }
    }).start();
    new Thread(() -> {
      System.out.println("runA2");
      while (Scalars.lessThan(timing.seconds(), timeout)) {
        synchronized (boundedLinkedList) {
          boundedLinkedList.add(randomGenerator.nextInt());
        }
      }
    }).start();
    new Thread(() -> {
      System.out.println("runR");
      while (Scalars.lessThan(timing.seconds(), timeout)) {
        if (!boundedLinkedList.isEmpty()) {
          int poll;
          synchronized (boundedLinkedList) {
            poll = boundedLinkedList.poll();
          }
          System.out.println(poll);
        }
      }
    }).start();
  }
}
