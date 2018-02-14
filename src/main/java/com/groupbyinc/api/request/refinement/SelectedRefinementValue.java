package com.groupbyinc.api.request.refinement;

import com.groupbyinc.api.request.SelectedRefinement;

/**
 * <code>
 * A RefinementValue extends Refinement and represents a navigation refinement that holds a value.
 * Typically, brand is a value refinement.
 *
 * - `id`: an MD5 hash of the content of this value.
 * - `value`: The value of the refinement.
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
public class SelectedRefinementValue extends SelectedRefinement<SelectedRefinementValue> {

  private String value;

  /**
   * @return Type.Value
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
  public SelectedRefinementValue setValue(String value) {
    this.value = value;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SelectedRefinementValue that = (SelectedRefinementValue) o;

    if (value != null ? !value.equals(that.value) : that.value != null) {
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
    int result = value != null ? value.hashCode() : 0;
    result = 31 * result + (getId() != null ? getId().hashCode() : 0);
    result = 31 * result + (getNavigationName() != null ? getNavigationName().hashCode() : 0);
    result = 31 * result + (getExclude() != null ? getExclude().hashCode() : 0);
    return result;
  }
}
