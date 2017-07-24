package com.groupbyinc.api.request;

import static com.groupbyinc.api.model.NumericBoost.DEFAULT_STRENGTH;

/**
 * Created by groupby on 4/1/15.
 */
public class NumericBoost {

  private String name;
  private boolean inverted;
  private double strength;

  public NumericBoost() {
    strength = DEFAULT_STRENGTH;
  }

  public String getName() {
    return name;
  }

  public NumericBoost setName(String name) {
    this.name = name;
    return this;
  }

  public boolean isInverted() {
    return inverted;
  }

  public NumericBoost setInverted(boolean inverted) {
    this.inverted = inverted;
    return this;
  }

  public double getStrength() {
    return strength;
  }

  public NumericBoost setStrength(double strength) {
    this.strength = strength;
    return this;
  }
}
