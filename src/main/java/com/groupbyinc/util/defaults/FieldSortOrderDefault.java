package com.groupbyinc.util.defaults;

import com.groupbyinc.api.model.sort.FieldSort;

public class FieldSortOrderDefault {

  public boolean equals(Object value) {
    return value != null && (value instanceof FieldSort.Order || value instanceof com.groupbyinc.api.request.sort.FieldSort.Order) && FieldSort.Order.Ascending.toString().equals(value.toString());
  }
}
