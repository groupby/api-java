package com.groupbyinc.api.request.refinement;

import com.groupbyinc.api.request.SelectedRefinement;

/**
 * <code>
 * A RefinementRange extends Refinement and represents a navigation refinement between two numeric values.
 * Typically, price is a range refinement.
 *
 * - `id`: an MD5 hash of the content of this value.
 * - `low`: The lower bound value.
 * - `high`: The upper bound value.
 * - `count`: the number of records that will be filtered down to by this navigation value.
 * - `displayName`: A never blank combination of the value / low and high properties.
 * - `navigationName`: - the name of the parent navigation.
 * - `isRange`: - whether this is a range refinement or a value refinement
 * - `type`: - the type of refinement, Value or Range
 *
 * </code>
 *
 * @author will
 */
public class SelectedRefinementRange extends SelectedRefinement<SelectedRefinementRange> {

  private String high;
  private String low;

  /**
   * @return Type.Range
   */
  @Override
  public SelectedRefinement.Type getType() {
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
  public SelectedRefinementRange setLow(String low) {
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
  public SelectedRefinementRange setHigh(String high) {
    this.high = high;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SelectedRefinementRange that = (SelectedRefinementRange) o;

    if (high != null ? !high.equals(that.high) : that.high != null) {
      return false;
    } else if (low != null ? !low.equals(that.low) : that.low != null) {
      return false;
    } else if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
      return false;
    } else if (getNavigationName() != null ? !getNavigationName().equals(that.getNavigationName()) : that.getNavigationName() != null) {
      return false;
    }
    return getExclude() != null ? getExclude().equals(that.getExclude()) : that.getExclude() == null;
  }

  @Override
  public int hashCode() {
    int result = high != null ? high.hashCode() : 0;
    result = 31 * result + (low != null ? low.hashCode() : 0);
    result = 31 * result + (getId() != null ? getId().hashCode() : 0);
    result = 31 * result + (getNavigationName() != null ? getNavigationName().hashCode() : 0);
    result = 31 * result + (getExclude() != null ? getExclude().hashCode() : 0);
    return result;
  }
}
