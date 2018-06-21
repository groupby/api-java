package com.groupbyinc.api.model.refinement;

import com.groupbyinc.api.model.Refinement;

/**
 * <code>
 * A RefinementRange extends Refinement and represents a navigation refinement between two numeric values.
 * Typically, price is a range refinement.
 *
 * - `id`: an MD5 hash of the content of this value.
 * - `low`: The lower bound value.
 * - `high`: The upper bound value.
 * - `count`: the number of records that will be filtered down to by this navigation value.
 * - `isRange`: - whether this is a range refinement or a value refinement
 * - `type`: - the type of refinement, Value or Range
 *
 * </code>
 *
 */
public class RefinementRange extends Refinement<RefinementRange> {

  private String high;
  private String low;

  /**
   * @return Type.Range
   * @internal
   */
  @Override
  public Type getType() {
    return Type.Range;
  }

  /**
   * @internal
   */
  @Override
  public String toTildeString() {
    return ":" + low + ".." + high;
  }

  /**
   * @return Returns the lower bound of this range.
   */
  public String getLow() {
    return low;
  }

  /**
   * @param low Set the lower bound.
   * @return
   */
  public RefinementRange setLow(String low) {
    this.low = low;
    return this;
  }

  /**
   * @return Return the upper bound of this range.
   */
  public String getHigh() {
    return high;
  }

  /**
   * @param high Set the uppermost value.
   * @return
   */
  public RefinementRange setHigh(String high) {
    this.high = high;
    return this;
  }

  /**
   * @param o the reference object with which to compare.
   * @return `true` if this object is the same as the o argument; `false` otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    } else if (!super.equals(o)) {
      return false;
    }

    RefinementRange that = (RefinementRange) o;

    if (getCount() != that.getCount()) {
      return false;
    } else if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
      return false;
    } else if (getExclude() != null ? !getExclude().equals(that.getExclude()) : that.getExclude() != null) {
      return false;
    } else if (high != null ? !high.equals(that.high) : that.high != null) {
      return false;
    }
    return low != null ? low.equals(that.low) : that.low == null;
  }

  /**
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (high != null ? high.hashCode() : 0);
    result = 31 * result + (low != null ? low.hashCode() : 0);
    result = 31 * result + getCount();
    result = 31 * result + (getExclude() != null ? getExclude().hashCode() : 0);
    result = 31 * result + (getId() != null ? getId().hashCode() : 0);
    return result;
  }
}
