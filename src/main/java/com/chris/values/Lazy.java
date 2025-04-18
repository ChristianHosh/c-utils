package com.chris.values;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

public class Lazy<T> {

  private final AtomicReference<T> value = new AtomicReference<>();
  private final Object mutex = new Object();

  public static <T> Lazy<T> of() {
    return new Lazy<>();
  }

  public static <T> Supplier<T> ofSupplier(Supplier<T> supplier) {
    return new LazySupplier<>(supplier);
  }
  
  public static <T, R> Function<T, R> ofFunction(Set<T> expectedKeys, Function<T, R> function) {
    return new LazyFunction<>(expectedKeys, function);
  }

  public T get() {
    synchronized (mutex) {
      T current = getValue();
      if (current == null)
        throw new IllegalStateException("Value not initialized");
      return current;
    }
  }

  public boolean isSet() {
    synchronized (mutex) {
      return getValue() != null;
    }
  }

  public T orElseSet(Supplier<T> supplier) {
    synchronized (mutex) {
      T current;
      if ((current = getValue()) != null)
        return current;

      T newValue = supplier.get();
      return setValue(newValue) ? newValue : value.get();
    }
  }

  private boolean setValue(T newValue) {
    synchronized (mutex) {
      return value.compareAndSet(null, newValue);
    }
  }

  private T getValue() {
    synchronized (mutex) {
      return value.get();
    }
  }


}
