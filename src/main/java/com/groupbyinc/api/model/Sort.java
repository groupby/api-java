package com.groupbyinc.api.model;

import com.groupbyinc.api.model.sort.FieldSort;
import com.groupbyinc.api.model.sort.SortByIds;
import com.groupbyinc.common.jackson.annotation.JsonSubTypes;
import com.groupbyinc.common.jackson.annotation.JsonTypeInfo;

/**
 * <code>
 * Interface for Sorts
 * </code>
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = FieldSort.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = FieldSort.class, name = "Field"), //
    @JsonSubTypes.Type(value = SortByIds.class, name = "ByIds") //
})
public interface Sort {

  enum Order {
    Ascending,
    Descending
  }
}
