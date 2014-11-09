package com.groupbyinc.injector;

public interface StaticInjector<T> {
    public T get();

    public void set(T pInjectedObject);
}
