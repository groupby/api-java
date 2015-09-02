package com.groupbyinc.api.tags;

import com.groupbyinc.api.model.AbstractRecord;
import com.groupbyinc.api.model.AbstractResults;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.refinement.RefinementRange;
import com.groupbyinc.api.model.refinement.RefinementValue;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionsTest {

    @Test
    public void testUncamel() throws Exception {
        assertEquals(
                "Java API Reference", Functions.uncamel("javaApiReference"));
        assertEquals(
                "Java API Reference", Functions.uncamel("javaAPIReference"));
        assertEquals("GSA Configuration", Functions.uncamel("gsaConfiguration"));
        assertEquals(
                "Pier 1 Load Balancing", Functions.uncamel("Pier1LoadBalancing"));
        assertEquals(".NET API", Functions.uncamel(".NetApi"));
        assertEquals("Network Settings", Functions.uncamel("NetworkSettings"));
        assertEquals("FAQ", Functions.uncamel("faq"));
        // assertEquals("FAQs", Functions.uncamel("faqS"));
    }

    @Test
    public void testReverse() throws Exception {
        List<String> reverse = Functions.reverse(asList("1", "2", "3"));
        assertEquals("[3, 2, 1]", reverse.toString());
    }

    private static class RecordMock extends AbstractRecord<RecordMock> {

    }

    private static class ResultsMock extends AbstractResults<RecordMock, ResultsMock> {

    }

    @Test
    public void testRefinementSelected() {
        ResultsMock r = new ResultsMock();
        r.setSelectedNavigation(
                asList(
                        new Navigation().setName("a").setOr(true).setRefinements(
                                asList(
                                        new RefinementValue().setValue("1"), //
                                        new RefinementValue().setValue("2"))), //
                        new Navigation().setName("b").setRange(true).setRefinements(
                                singletonList((Refinement) new RefinementRange().setLow("0").setHigh("1"))), //
                        new Navigation().setName("c").setOr(false).setRefinements(
                                asList(
                                        new RefinementValue().setValue("1"), //
                                        new RefinementValue().setValue("2"))) //
                ));

        assertFalse(Functions.isRefinementSelected(r, null, "1"));
        assertFalse(Functions.isRefinementSelected(r, "", "1"));
        assertFalse(Functions.isRefinementSelected(r, "a", null));
        assertFalse(Functions.isRefinementSelected(r, "a", ""));
        assertFalse(Functions.isRefinementSelected(r, "a", "3"));
        assertFalse(Functions.isRefinementSelected(r, "b", "1"));
        assertFalse(Functions.isRefinementSelected(r, "c", "3"));

        assertTrue(Functions.isRefinementSelected(r, "a", "1"));
        assertTrue(Functions.isRefinementSelected(r, "a", "2"));
        assertTrue(Functions.isRefinementSelected(r, "c", "1"));
        assertTrue(Functions.isRefinementSelected(r, "c", "2"));
    }
}
