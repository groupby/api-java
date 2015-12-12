package com.groupbyinc.injector;

public class GlobalInjector<T> implements StaticInjector<T> {
    private T injectedObject = null;

    @Override
    public T get() {
        return injectedObject;
    }

    @Override
    public void set(T injectedObject) {
        this.injectedObject = injectedObject;
    }
}
