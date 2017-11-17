package com.groupbyinc.api.model;

import com.groupbyinc.api.model.sort.FieldSort;
import com.groupbyinc.api.model.sort.SortByIds;
import com.groupbyinc.common.jackson.Mappers;
import org.junit.Test;

import java.io.IOException;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

/**
 * @author groupby
 */
public class SortTest {

  private Sort parse(String value) throws IOException {
    return Mappers.getStrictReader(false).forType(Sort.class).readValue(value.getBytes());
  }

  @Test
  public void testFieldSortIsDefault() throws Exception {
    Sort actual = parse("{'field':'dummy'}");
    assertEquals(FieldSort.class, actual.getClass());

    FieldSort actualFieldSort = (FieldSort) actual;
    assertEquals("dummy", actualFieldSort.getField());
    assertEquals(Sort.Order.Ascending, actualFieldSort.getOrder());
  }

  @Test
  public void testFieldSortOrder() throws Exception {
    Sort actual = parse("{'field':'dummy','order':'Descending'}");
    assertEquals(FieldSort.class, actual.getClass());

    FieldSort actualFieldSort = (FieldSort) actual;
    assertEquals("dummy", actualFieldSort.getField());
    assertEquals(Sort.Order.Descending, actualFieldSort.getOrder());
  }

  @Test
  public void testSortByOneId() throws Exception {
    Sort actual = parse("{'type':'ByIds','ids':'dummy'}");
    assertEquals(SortByIds.class, actual.getClass());

    SortByIds actualSortByIds = (SortByIds) actual;
    assertEquals(singletonList("dummy"), actualSortByIds.getIds());
  }

  @Test
  public void testSortByIds() throws Exception {
    Sort actual = parse("{'type':'ByIds','ids':['id1','id2']}");
    assertEquals(SortByIds.class, actual.getClass());

    SortByIds actualSortByIds = (SortByIds) actual;
    assertEquals(asList("id1", "id2"), actualSortByIds.getIds());
  }
}