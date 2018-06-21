package com.groupbyinc.api.request;

import com.groupbyinc.api.request.refinement.SelectedRefinementRange;
import com.groupbyinc.api.request.refinement.SelectedRefinementValue;
import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonInclude;
import com.groupbyinc.common.jackson.annotation.JsonProperty;
import com.groupbyinc.common.jackson.annotation.JsonSubTypes;
import com.groupbyinc.common.jackson.annotation.JsonTypeId;
import com.groupbyinc.common.jackson.annotation.JsonTypeInfo;

/**
 * <code>
 * Abstract Refinement class holding common methods for RefinementRange and RefinementValue.
 * </code>
 *
 * @internal
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = SelectedRefinementValue.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SelectedRefinementValue.class, name = "Value"), @JsonSubTypes.Type(value = SelectedRefinementRange.class, name = "Range")})
public abstract class SelectedRefinement<T extends SelectedRefinement<T>> {

  public enum Type {
    Value,
    Range // NOSONAR
  }

  @JsonProperty("_id") private String id;
  private String navigationName;
  @JsonInclude(JsonInclude.Include.NON_DEFAULT) private Boolean exclude = false;

  /**
   * @return The ID is a MD5 of the name and value of the refinement.
   */
  public String getId() {
    return id;
  }

  /**
   * @param id Set the ID
   */
  @SuppressWarnings("unchecked")
  public T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return The navigation name
   */
  public String getNavigationName() {
    return navigationName;
  }

  /**
   * @param navigationName Set the navigation name
   */
  @SuppressWarnings("unchecked")
  public T setNavigationName(String navigationName) {
    this.navigationName = navigationName;
    return (T) this;
  }

  /**
   * @return True if this refinement a range.
   */
  @JsonIgnore
  public boolean isRange() {
    return getType() == Type.Range;
  }

  /**
   * <code>
   * Types are either `Range` or `Value`
   *
   * They represent the objects RefinementRange and RefinementValue
   * </code>
   *
   * @return The type of this refinement
   */
  @JsonTypeId
  public abstract Type getType();

  /**
   * @internal
   */
  public abstract String toTildeString();

  /**
   * @return The boolean to determine if the refinement should be excluded from the result
   */
  public Boolean getExclude() {
    return exclude;
  }

  /**
   * @param exclude Set the exclude
   */
  @SuppressWarnings("unchecked")
  public T setExclude(Boolean exclude) {
    this.exclude = exclude;
    return (T) this;
  }
}
