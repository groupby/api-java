package com.groupbyinc.api.model.refinement;

import com.groupbyinc.api.model.Refinement;

/**
 * <code>
 * A RefinementValue extends Refinement and represents a navigation refinement that holds a value.
 * Typically, brand is a value refinement.
 *
 * - `id`: an MD5 hash of the content of this value.
 * - `value`: The value of the refinement.
 * - `count`: the number of records that will be filtered down to by this navigation value.
 * - `isRange`: - whether this is a range refinement or a value refinement
 * - `type`: - the type of refinement, Value or Range
 *
 * </code>
 *
 */
public class RefinementValue extends Refinement<RefinementValue> {

  private String value;

  /**
   * @return Type.Value
   * @internal
   */
  @Override
  public Type getType() {
    return Type.Value;
  }

  /**
   * @internal
   */
  @Override
  public String toTildeString() {
    return "=" + value;
  }

  /**
   * @return the value of this refinement
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value Set the value
   * @return
   */
  public Refinement setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * @param obj the reference object with which to compare.
   * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RefinementValue that = (RefinementValue) o;
    if (getCount() != that.getCount()) {
      return false;
    } else if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
      return false;
    } else if (getExclude() != null ? !getExclude().equals(that.getExclude()) : that.getExclude() != null) {
      return false;
    }

    return value != null ? value.equals(that.value) : that.value == null;
  }

  /**
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int result = value != null ? value.hashCode() : 0;
    result = 31 * result + getCount();
    result = 31 * result + (getExclude() != null ? getExclude().hashCode() : 0);
    result = 31 * result + (getId() != null ? getId().hashCode() : 0);
    return result;
  }
}
