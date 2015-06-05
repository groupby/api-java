package com.groupbyinc.api.tags;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

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
}
