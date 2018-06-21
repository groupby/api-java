package com.groupbyinc.api.request.sort;

import com.groupbyinc.api.request.Sort;
import com.groupbyinc.common.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>
 *     Object that specifies the order of the result set, by IDs. Any record included in the result set, but not this sort,
 *     will be moved to the end of the result set and will maintain the original ordering.
 * </code>
 */
public class SortByIds implements Sort {

  @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<String> ids = new ArrayList<String>();

  public List<String> getIds() {
    return ids;
  }

  /**
   * <code>
   *     The product IDs in the expected order for the result set.
   * </code>
   * @param ids
   *      The products IDs in the expected order for the result set.
   */
  public SortByIds setIds(List<String> ids) {
    this.ids = ids;
    return this;
  }
}
