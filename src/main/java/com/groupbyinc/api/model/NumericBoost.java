package com.groupbyinc.api.model;

/**
 * <code>
 * A numeric boost specifies a field to match in a search and a weight
 * to apply to that field. The position of records in the page of results
 * is adjusted by the value of the field multiplied by the weight.
 * </code>
 */
public class NumericBoost {

  public static final double DEFAULT_STRENGTH = 1;

  private String name;
  private boolean inverted;
  private double strength;

  public NumericBoost() {
    strength = DEFAULT_STRENGTH;
  }

  /**
   * @return The name of the field to boost on
   */
  public String getName() {
    return name;
  }

  /**
   * <code>
   * Set the name of the field to boost on
   * </code>
   *
   * @param name The name of the field to boost on
   * @return
   */
  public NumericBoost setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return Whether the numeric boost is inverted
   */
  public boolean isInverted() {
    return inverted;
  }

  /**
   * <code>
   * Set whether the numeric boost is inverted
   * </code>
   *
   * @param inverted if true, the numeric boost will be inverted
   * @return
   */
  public NumericBoost setInverted(boolean inverted) {
    this.inverted = inverted;
    return this;
  }

  /**
   * @return The weight of the numeric boost
   */
  public double getStrength() {
    return strength;
  }

  /**
   * <code>
   * Set the weight of the numeric boost
   * </code>
   *
   * @param strength The weight of the numeric boost
   * @return
   */
  public NumericBoost setStrength(double strength) {
    this.strength = strength;
    return this;
  }
}
