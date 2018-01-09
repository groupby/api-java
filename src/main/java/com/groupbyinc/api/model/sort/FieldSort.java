package com.groupbyinc.api.model.sort;

import com.groupbyinc.api.model.Sort;
import com.groupbyinc.common.jackson.annotation.JsonInclude;
import com.groupbyinc.util.defaults.FieldSortOrderDefault;

/**
 * <code>
 * Object that specifies sort field and direction
 * </code>
 */
public class FieldSort implements Sort {

  public static final FieldSort RELEVANCE = new FieldSort().setField("_relevance");

  private String field;

  @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = FieldSortOrderDefault.class) private Order order = Sort.Order.Ascending;

  public String getField() {
    return field;
  }

  /**
   *
   * @param field
   *      The source field name to sort by.
   * @return
   */
  public FieldSort setField(String field) {
    this.field = field;
    return this;
  }

  /**
   *
   * @return The order Ascending or Descending
   */
  public Order getOrder() {
    return order;
  }

  /**
   * <code>
   *     Order in which the field will be applied.  Takes either
   *     `Ascending` or `Descending`
   * </code>
   * @param order
   * @return
   */
  public FieldSort setOrder(Order order) {
    this.order = order;
    return this;
  }
}
