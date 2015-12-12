package com.groupbyinc.injector;

public class ThreadLocalInjector<T> implements StaticInjector<T> {
    private ThreadLocal<T> injectedObject = new ThreadLocal<T>();

    @Override
    public T get() {
        return injectedObject.get();
    }

    @Override
    public void set(T injectedObject) {
        this.injectedObject.set(injectedObject);
    }
}
