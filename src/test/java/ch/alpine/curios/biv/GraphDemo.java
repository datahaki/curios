// code by jph
package ch.alpine.curios.biv;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class GraphDemo {
  static void main() {
    try (ScanResult scan = new ClassGraph().enableAllInfo().acceptPackages("demo") //
        .scan()) {
      scan.getClassesImplementing(BivariateEvaluation.class.getName()) //
          .loadClasses().forEach(clazz -> {
            // your assertions here
            System.out.println(clazz.getName());
          });
    }
  }
}
