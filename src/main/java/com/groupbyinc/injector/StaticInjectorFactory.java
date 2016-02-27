package com.groupbyinc.injector;

import com.groupbyinc.util.PropertyUtils;

public class StaticInjectorFactory<T> {

  public StaticInjector<T> create() {
    if (PropertyUtils.getFlag("threadLocal")) {
      return new ThreadLocalInjector<T>();
    } else {
      return new GlobalInjector<T>();
    }
  }
}
