package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.Mappers;
import org.junit.Test;

import static com.groupbyinc.common.test.util.AssertUtils.assertJsonEquals;
import static java.util.Arrays.asList;

public class MatchStrategyTest {

  private void assertParse(Results object) {
    String expected = Mappers.writeValueAsString(object);
    assertJsonEquals(expected, Mappers.readValue(expected.getBytes(), Results.class, false));
  }

  @Test
  public void testParse() {
    assertParse(new Results().setMatchStrategy(
        new MatchStrategy().setRules(asList(new PartialMatchRule().setTerms(2).setMustMatch(2), new PartialMatchRule().setTermsGreaterThan(5).setMustMatch(10).setPercentage(true)))));
  }
}
