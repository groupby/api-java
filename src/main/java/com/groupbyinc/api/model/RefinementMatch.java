package com.groupbyinc.api.model;

import java.util.List;

/**
 * Created by groupby on 07/01/15.
 */
public class RefinementMatch {

  public static class Value {

    private String value;
    private Integer count;

    public String getValue() {
      return value;
    }

    public Value setValue(String value) {
      this.value = value;
      return this;
    }

    public Integer getCount() {
      return count;
    }

    public Value setCount(Integer count) {
      this.count = count;
      return this;
    }
  }

  private String name;
  private List<Value> values;

  public String getName() {
    return name;
  }

  public RefinementMatch setName(String name) {
    this.name = name;
    return this;
  }

  public List<Value> getValues() {
    return values;
  }

  public RefinementMatch setValues(List<Value> values) {
    this.values = values;
    return this;
  }
}
