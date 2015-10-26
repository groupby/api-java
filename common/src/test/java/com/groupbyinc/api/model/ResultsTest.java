package com.groupbyinc.api.model;

import com.groupbyinc.api.model.refinement.RefinementRange;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.common.jackson.util.Mappers;
import com.groupbyinc.common.test.util.AssertUtils;
import org.junit.Test;

import static java.util.Arrays.asList;

public class ResultsTest {

    private void assertNavigation(String pExpected, Navigation pNavigation) throws Exception {
        AssertUtils.assertJsonEquals(pExpected, Mappers.writeValueAsString(pNavigation));
    }

    @Test
    public void testGetSelectedNavigationJsonOneValue() throws Exception {
        RefinementValue rv = new RefinementValue();
        rv.setValue("Ö'=\"");

        assertNavigation(
                "{'name':'A','refinements':[{'type':'Value'," +
                "'value':\"Ö'=\\\"\"}],'metadata':[],'or':false,'range':false}", //
                new Navigation().setName("A").setRefinements(asList((Refinement) rv)));
    }

    @Test
    public void testGetSelectedNavigationJsonOneRange() throws Exception {
        RefinementRange rr = new RefinementRange();
        rr.setLow("10");
        rr.setHigh("100");

        assertNavigation(
                "{'name':'A','refinements':[{'type':'Range'," +
                "'high':'100','low':'10'}],'metadata':[],'or':false,'range':false}", //
                new Navigation().setName("A").setRefinements(asList((Refinement) rr)));
    }

    @Test
    public void testGetSelectedNavigationJsonTwo() throws Exception {
        RefinementValue rv = new RefinementValue();
        rv.setValue("Ö'=\"");

        RefinementRange rr = new RefinementRange();
        rr.setLow("10");
        rr.setHigh("100");

        assertNavigation(
                "{'name':'A','refinements':[{'type':'Value'," +
                "'value':\"Ö'=\\\"\"},{'type':'Range'," +
                "'high':'100','low':'10'}],'metadata':[],'or':false,'range':false}", //
                new Navigation().setName("A").setRefinements(asList((Refinement) rv, rr)));

        assertNavigation(
                "{'name':'A','refinements':[{'type':'Range'," +
                "'high':'100','low':'10'},{'type':'Value'," +
                "'value':\"Ö'=\\\"\"}],'metadata':[],'or':false,'range':false}", //
                new Navigation().setName("A").setRefinements(asList((Refinement) rr, rv)));
    }

}
