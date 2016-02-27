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
   * <code>
   * Default constructor
   * </code>
   */
  public SelectedRefinementRange() {
    // default constructor
  }

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
}
