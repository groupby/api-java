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
 * @author will
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
}
