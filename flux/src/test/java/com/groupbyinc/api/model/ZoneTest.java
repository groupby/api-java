package com.groupbyinc.api.model;

import com.groupbyinc.api.model.zone.RecordZone;
import com.groupbyinc.utils.Mappers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.fail;

public class ZoneTest {
    @Test
    public void testRecordZone() throws Exception {
        try {
            List<RefinementMatch> refinementMatches = new ArrayList<RefinementMatch>();
            refinementMatches.add(
                    new RefinementMatch().setName("a").setValues(
                            asList(
                                    new RefinementMatch.Value().setValue("c").setCount(
                                            2), new RefinementMatch.Value().setValue("b").setCount(
                                            1))));

            Record record = new Record().setId("abc").setUrl("abc").setTitle("abc").setSnippet("abc")
                                        .setRefinementMatches(refinementMatches);
            System.out.println("record:     " + Mappers.writeValueAsString(record));

            RecordZone<Record> zone = new RecordZone<Record>().setId("abc").setName("abc").setQuery("abc").setRecords(
                    asList(record));
            System.out.println("zone:       " + Mappers.writeValueAsString(zone));

            Map<String, Zone> zones = new HashMap<String, Zone>();
            zones.put("abc", zone);
            Template template = new Template().setName("abc").setZones(zones);
            System.out.println("template:   " + Mappers.writeValueAsString(template));

            Results results = new Results().setTemplate(template);
            System.out.println("results:    " + Mappers.writeValueAsString(results));
        } catch (StackOverflowError e) {
            fail("should be able to serialize");
        }
    }
}