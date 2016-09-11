package com.groupbyinc.api.model;

import com.groupbyinc.api.model.zone.RecordZone;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.groupbyinc.common.jackson.Mappers.writeValueAsString;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ZoneTest {

  public static final String TYPE_RECORD = "\"type\" : \"Record\",";

  @Test
  public void testRecordZone() throws Exception {
    try {
      List<RefinementMatch> refinementMatches = new ArrayList<RefinementMatch>();
      refinementMatches.add(new RefinementMatch().setName("a")
                                .setValues(asList(new RefinementMatch.Value().setValue("c")
                                                      .setCount(2), new RefinementMatch.Value().setValue("b")
                                                      .setCount(1))));

      Record record = new Record().setId("abc")
          .setUrl("abc")
          .setTitle("abc")
          .setSnippet("abc")
          .setRefinementMatches(refinementMatches);
      String actual = writeValueAsString(record, true);

      RecordZone zone = new RecordZone().setId("abc")
          .setName("abc")
          .setQuery("abc")
          .setRecords(singletonList(record));
      actual = writeValueAsString(zone, true);
      assertCountMatches(1, TYPE_RECORD, actual, "zone");

      Map<String, Zone> zones = new HashMap<String, Zone>();
      zones.put("abc", zone);
      Template template = new Template().setName("abc")
          .setZones(zones);
      actual = writeValueAsString(template, true);
      assertCountMatches(1, TYPE_RECORD, actual, "template");

      Results results = new Results().setTemplate(template);
      actual = writeValueAsString(results, true);
      assertCountMatches(1, TYPE_RECORD, actual, "results");
    } catch (StackOverflowError e) {
      fail("should be able to serialize");
    }
  }

  private void assertCountMatches(int expectedCount, String search, String actual, String type) {
    Matcher m = Pattern.compile(search)
        .matcher(actual);

    int count = 0;
    while (m.find()) {
      count++;
    }
    assertEquals(expectedCount, count);
  }
}
