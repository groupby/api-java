package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonInclude;

/**
 * <code>
 * Object that specifies sort field and direction
 * </code>
 * Created by groupby on 11/11/14.
 */
public class Sort {

  public static final Sort RELEVANCE = new Sort().setField("_relevance");

  public enum Order {
    Ascending,
    Descending
  }

  private String field;

  @JsonInclude(JsonInclude.Include.NON_DEFAULT) private Order order = Sort.Order.Ascending;

  public String getField() {
    return field;
  }

  /**
   *
   * @param field
   *      The source field name to sort by.
   * @return
   */
  public Sort setField(String field) {
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
  public Sort setOrder(Order order) {
    this.order = order;
    return this;
  }
}
