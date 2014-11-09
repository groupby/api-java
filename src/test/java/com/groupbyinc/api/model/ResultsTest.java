package com.groupbyinc.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.groupbyinc.api.parser.Mappers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ResultsTest {

    @SuppressWarnings("serial")
    private final ResultsBase test = new ResultsBase() {
    };

    private void assertRefinementsJson(List<String> expected, List<Refinement> actual) throws JsonProcessingException {
        String expectedString = Mappers.JSON.writeValueAsString(expected);

        test.setSelectedRefinements(actual);
        assertEquals(expectedString, test.getSelectedRefinementsJson());
    }

    @Test
    public void testGetSelectedRefinementsJsonEmpty() throws JsonProcessingException {
        assertRefinementsJson(
                new ArrayList<String>(), new ArrayList<Refinement>());
    }

    @Test
    public void testGetSelectedRefinementsJsonOneValue() throws JsonProcessingException {
        RefinementValue rv = new RefinementValue();
        rv.setNavigationName("A");
        rv.setValue("Ö'=\"");

        List<Refinement> refinements = new ArrayList<Refinement>();
        refinements.add(rv);

        assertRefinementsJson(asList("A:Ö'=\""), refinements);
    }

    @Test
    public void testGetSelectedRefinementsJsonOneRange() throws JsonProcessingException {
        RefinementRange rr = new RefinementRange();
        rr.setNavigationName("A");
        rr.setLow("10");
        rr.setHigh("100");

        List<Refinement> refinements = new ArrayList<Refinement>();
        refinements.add(rr);

        assertRefinementsJson(asList("A:10:100"), refinements);
    }

    @Test
    public void testGetSelectedRefinementsJsonTwo() throws JsonProcessingException {
        RefinementValue rv = new RefinementValue();
        rv.setNavigationName("A");
        rv.setValue("Ö'=\"");

        RefinementRange rr = new RefinementRange();
        rr.setNavigationName("A");
        rr.setLow("10");
        rr.setHigh("100");

        List<Refinement> refinements = new ArrayList<Refinement>();

        refinements.add(rv);
        refinements.add(rr);
        assertRefinementsJson(asList("A:Ö'=\"", "A:10:100"), refinements);

        refinements.clear();
        refinements.add(rr);
        refinements.add(rv);
        assertRefinementsJson(asList("A:10:100", "A:Ö'=\""), refinements);
    }
}
