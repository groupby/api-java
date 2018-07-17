package com.groupbyinc.api.request;

public class RestrictNavigation {

  private String name;
  private int count;

  public String getName() {
    return name;
  }

  public RestrictNavigation setName(String name) {
    this.name = name;
    return this;
  }

  public int getCount() {
    return count;
  }

  public RestrictNavigation setCount(int count) {
    this.count = count;
    return this;
  }

  @Override
  public String toString() {
    return "[" + name + ", " + count + "]";
  }
}
