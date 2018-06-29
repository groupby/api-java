package com.groupbyinc.api.model;

import com.groupbyinc.api.model.refinement.RefinementRange;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.common.test.util.AssertUtils;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ResultsTest {

  @Test
  public void testGetSelectedNavigationJsonOneValue() {
    RefinementValue rv = new RefinementValue();
    rv.setValue("Ö'=\"");

    assertNavigation(
        "{'name':'A','refinements':[{'type':'Value'," + "'value':\"Ö'=\\\"\"}],'metadata':[],'or':false,'range':false}", new Navigation().setName("A").setRefinements(singletonList((Refinement) rv)));
  }

  private void assertNavigation(String expected, Navigation navigation) {
    AssertUtils.assertJsonEquals(expected, navigation);
  }

  @Test
  public void testGetSelectedNavigationJsonOneRange() {
    RefinementRange rr = new RefinementRange();
    rr.setLow("10");
    rr.setHigh("100");

    assertNavigation("{'name':'A','refinements':[{'type':'Range'," + "'high':'100','low':'10'}],'metadata':[],'or':false,'range':false}",
                     new Navigation().setName("A").setRefinements(singletonList((Refinement) rr)));
  }

  @Test
  public void testGetSelectedNavigationJsonTwo() {
    RefinementValue rv = new RefinementValue();
    rv.setValue("Ö'=\"");

    RefinementRange rr = new RefinementRange();
    rr.setLow("10");
    rr.setHigh("100");

    assertNavigation("{'name':'A','refinements':[{'type':'Value'," + "'value':\"Ö'=\\\"\"},{'type':'Range'," + "'high':'100','low':'10'}],'metadata':[],'or':false,'range':false}",
                     new Navigation().setName("A").setRefinements(asList((Refinement) rv, rr)));

    assertNavigation("{'name':'A','refinements':[{'type':'Range'," + "'high':'100','low':'10'},{'type':'Value'," + "'value':\"Ö'=\\\"\"}],'metadata':[],'or':false,'range':false}",
                     new Navigation().setName("A").setRefinements(asList((Refinement) rr, rv)));
  }
}
