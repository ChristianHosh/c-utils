package com.chris.values;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

class LazySupplier<T> implements Supplier<T> {

  private final AtomicReference<T> reference = new AtomicReference<>();
  private final Supplier<T> supplier;

  public LazySupplier(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  @Override
  public synchronized T get() {
    T value = reference.get();
    if (value == null) {
      value = supplier.get();
      reference.compareAndSet(null, value);
    }

    return value;
  }
}
