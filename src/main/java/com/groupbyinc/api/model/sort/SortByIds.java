package com.groupbyinc.api.model.sort;

import com.groupbyinc.api.model.Sort;
import com.groupbyinc.common.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>
 * Object that specifies sort field and direction
 * </code>
 */
public class SortByIds implements Sort {

  @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<String> ids = new ArrayList<String>();

  public List<String> getIds() {
    return ids;
  }

  /**
   *
   * @param ids
   *      The products ids in the expected order.
   * @return
   */
  public SortByIds setIds(List<String> ids) {
    this.ids = ids;
    return this;
  }
}
