// code by jph
package ch.alpine.curios;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public record ClassGraphUtils<T>(Class<T> cls) {
  @SuppressWarnings("hiding")
  public <T> List<T> getInstances(String... packageNames) {
    List<T> collection = new LinkedList<>();
    for (Class<?> implementation : getImplementations("ch")) {
      List<T> list = getInstances(implementation);
      for (T sp : list) {
        collection.add(sp);
      }
    }
    return collection;
  }

  private List<Class<?>> getImplementations(String... packageNames) {
    try (ScanResult scanResult = new ClassGraph().enableAllInfo() //
        .acceptPackages(packageNames) //
        .scan()) {
      return scanResult.getClassesImplementing(cls.getName()) //
          .loadClasses();
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> List<T> getInstances(Class<?> implementation) {
    if (implementation.isEnum()) {
      return Stream.of(implementation.getEnumConstants()).map(t -> (T) t).toList();
    } else //
    if (implementation.isInterface()) {
      // ---
    } else //
    if (implementation.isRecord()) {
      // ---
    } else //
    if (implementation.isAnonymousClass()) {
      // ---
    } else //
    {
      Constructor<?> constructor = null;
      try {
        constructor = implementation.getDeclaredConstructor();
      } catch (Exception exception) {
        // ---
      }
      if (Objects.nonNull(constructor)) {
        constructor.setAccessible(true);
        Object object = null;
        try {
          object = constructor.newInstance();
        } catch (Exception exception) {
          // ---
        }
        if (Objects.nonNull(object))
          return List.of((T) object);
      }
    }
    return List.of();
  }
}
