package com.chris.values;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

class LazyFunction<T, R> implements Function<T, R> {

  private final Function<T, R> function;
  private final Map<T, R> cache;

  LazyFunction(Set<T> expectedKeys, Function<T, R> function) {
    this.function = function;
    this.cache = new HashMap<>(expectedKeys.size());
    for (T key : expectedKeys) {
      cache.put(key, null);
    }
  }

  @Override
  public R apply(T key) {
    if (!cache.containsKey(key))
      throw new IllegalArgumentException(key + " is not an expected value");
    R result = cache.get(key);
    if (result == null) {
      result = function.apply(key);
      cache.put(key, result);
    }
    return result;
  }
}
