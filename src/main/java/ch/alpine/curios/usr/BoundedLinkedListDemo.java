// code by jph
package ch.alpine.curios.usr;

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.ext.BoundedLinkedList;
import ch.alpine.tensor.ext.Timing;

public enum BoundedLinkedListDemo {
  ;
  static void main() {
    Timing timing = Timing.started();
    double timeout = 3;
    BoundedLinkedList<Integer> boundedLinkedList = new BoundedLinkedList<>(12);
    RandomGenerator randomGenerator = ThreadLocalRandom.current();
    new Thread(() -> {
      System.out.println("runA1");
      while (timing.seconds() < timeout)
        synchronized (boundedLinkedList) {
          boundedLinkedList.add(randomGenerator.nextInt());
        }
    }).start();
    new Thread(() -> {
      System.out.println("runA2");
      while (timing.seconds() < timeout) {
        synchronized (boundedLinkedList) {
          boundedLinkedList.add(randomGenerator.nextInt());
        }
      }
    }).start();
    new Thread(() -> {
      System.out.println("runR");
      while (timing.seconds() < timeout) {
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
