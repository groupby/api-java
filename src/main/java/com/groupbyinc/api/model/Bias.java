package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonProperty;
import com.groupbyinc.common.jackson.annotation.JsonSetter;

/**
 * <code>
 * A bias specifies a field and value to match in a search. If a record matches the bias,
 * the position of the record in the page of results is adjusted based on the strength provided.
 * </code>
 */
public class Bias {

  public enum Strength {
    Absolute_Increase,
    Strong_Increase,
    Medium_Increase,
    Weak_Increase,
    Leave_Unchanged,
    Weak_Decrease,
    Medium_Decrease,
    Strong_Decrease,
    Absolute_Decrease
  }

  private String name;
  private String content;

  @JsonProperty private Strength strength;

  /**
   * @return The name of the field to bias on
   */
  public String getName() {
    return name;
  }

  /**
   * <code>
   * Set the field name to bias on
   * </code>
   *
   * @param name
   *         The name of the field to bias on
   */
  public Bias setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return The value of the field to bias on
   */
  public String getContent() {
    return content;
  }

  /**
   * <code>
   * Set the field name to bias on
   * </code>
   *
   * @param content
   *         The value of the field to bias on
   */
  public Bias setContent(String content) {
    this.content = content;
    return this;
  }

  /**
   * @return The strength of the bias
   */
  public Strength getStrength() {
    return strength;
  }

  /**
   * <code>
   * Set the field name to bias on
   * </code>
   *
   * @param strength
   *         The strength of the bias, if applied
   */
  @JsonSetter
  public Bias setStrength(String strength) {
    this.strength = Strength.valueOf(strength);
    return this;
  }

  @JsonIgnore
  public Bias setStrength(Strength strength) {
    this.strength = strength;
    return this;
  }
}
