package com.groupbyinc.api;

import com.groupbyinc.util.Util;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryTest {
    private Query test = new Query();

    private void assertQuery(Query pExpected, Query pActual) {
        assertEquals(pExpected.getBridgeJson("aoeu"), pActual.getBridgeJson("aoeu"));
    }

    private void assertQuery(String pExpected, Query pActual) throws Exception {
        Util.assertJsonEquals(pExpected, pActual.getBridgeJson("aoeu"));
    }

    @Test
    public void testFilterString() throws Exception {
        test.addValueRefinement("department", "VIDEO");
        test.addRangeRefinement("regularPrice", "200.000000", "400.000000");
        assertEquals("~department=VIDEO~regularPrice:200.000000..400.000000", test.getRefinementString());
    }

    @Test
    public void testStringSplitter() throws Exception {
        test.addRefinementsByString("~department=VIDEO~regularPrice:200.000000..400.000000");

        Query expected = new Query();
        expected.addValueRefinement("department", "VIDEO");
        expected.addRangeRefinement("regularPrice", "200.000000", "400.000000");
        assertQuery(expected, test);
    }

    @Test
    public void testNullSearch() throws Exception {
        test.setPageSize(100);
        String expected = "{'" + Query.PARAM_SKIP + "':'0','" //
                + Query.PARAM_CLIENT_KEY + "':'aoeu','" //
                + Query.PARAM_PAGE_SIZE + "':'100','" //
                + Query.PARAM_RETURN_BINARY + "':'1','"//
                + Query.PARAM_PRUNE_REFINEMENTS + "':'1'"//
                + "}";
        assertQuery(expected, test);
    }

    @Test
    public void testQuoteInRefinement() throws Exception {
        test.setPageSize(100);
        test.addRefinementsByString("abc=ab\"");
        String expected = "{'" + Query.PARAM_SKIP + "':'0','" //
                + Query.PARAM_CLIENT_KEY + "':'aoeu','" //
                + Query.PARAM_REFINEMENTS + "':'~abc=ab\\\"','" //
                + Query.PARAM_PAGE_SIZE + "':'100','" //
                + Query.PARAM_RETURN_BINARY + "':'1','"//
                + Query.PARAM_PRUNE_REFINEMENTS + "':'1'" //
                + "}";
        assertQuery(expected, test);
    }

    @Test
    public void testMinForRanges() throws Exception {
        Query expected = new Query();
        expected.addValueRefinement("department", "VIDEO");
        expected.addRangeRefinement("regularPrice", "200.000000", "");

        test.addRefinementsByString("~department=VIDEO~regularPrice:200.000000..");
        assertQuery(expected, test);
    }

    @Test
    public void testMaxForRanges() throws Exception {
        Query expected = new Query();
        expected.addValueRefinement("department", "VIDEO");
        expected.addRangeRefinement("regularPrice", "", "100.000000");

        test.addRefinementsByString("~department=VIDEO~regularPrice:..100.000000");
        assertQuery(expected, test);
    }

    private void setupGeneratedQuery() {
        test.setSearchString("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort("relevance");
        test.addValueRefinement("redsox", "suck");
        test.setPartialFields("partialValue");
        test.setRequiredFields("requiredValue");
        test.setBiasingProfile("flange");
        test.setReturnBinary(false);
        test.setAccurateCounts(true);
    }

    @Test
    public void testGenWithNoLanguage() throws Exception {
        setupGeneratedQuery();
        String expected = "{'" //
                + Query.PARAM_SKIP + "':'0','"//
                + Query.PARAM_BIASING_PROFILE + "':'flange','" //
                + Query.PARAM_CLIENT_KEY + "':'aoeu','"//
                + Query.PARAM_CUSTOM_URL_PARAMS //
                + "':'~fromGoogle=true~bigspender=1','" //
                + Query.PARAM_REQUIRED_FIELDS + "':'requiredValue','"//
                + Query.PARAM_PARTIAL_FIELDS + "':'partialValue','"//
                + Query.PARAM_SORT + "':'relevance','" //
                + Query.PARAM_REFINEMENTS + "':'~redsox=suck','" //
                + Query.PARAM_SEARCH_STRING + "':'boston','"//
                + Query.PARAM_PAGE_SIZE + "':'10','"//
                + Query.PARAM_ACCURATE_COUNTS + "':'1','"//
                + Query.PARAM_PRUNE_REFINEMENTS + "':'1'"//
                + "}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithNullLanguage() throws Exception {
        setupGeneratedQuery();
        test.setLanguageRestrict(null);
        test.setInterfaceLanguage(null);
        String expected = "{'" //
                + Query.PARAM_SKIP + "':'0','" //
                + Query.PARAM_BIASING_PROFILE + "':'flange','" //
                + Query.PARAM_CLIENT_KEY + "':'aoeu','"//
                + Query.PARAM_CUSTOM_URL_PARAMS //
                + "':'~fromGoogle=true~bigspender=1','" //
                + Query.PARAM_REQUIRED_FIELDS + "':'requiredValue','"//
                + Query.PARAM_PARTIAL_FIELDS + "':'partialValue','"//
                + Query.PARAM_SORT + "':'relevance','" //
                + Query.PARAM_REFINEMENTS + "':'~redsox=suck','" //
                + Query.PARAM_SEARCH_STRING + "':'boston','"//
                + Query.PARAM_PAGE_SIZE + "':'10','"//
                + Query.PARAM_ACCURATE_COUNTS + "':'1','"//
                + Query.PARAM_PRUNE_REFINEMENTS + "':'1'"//
                + "}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithLanguage() throws Exception {
        setupGeneratedQuery();
        test.setLanguageRestrict("lang_en");
        test.setInterfaceLanguage("en");
        String expected = "{'" //
                + Query.PARAM_SKIP + "':'0','" //
                + Query.PARAM_BIASING_PROFILE + "':'flange','" //
                + Query.PARAM_LANGUAGE_RESTRICT + "':'lang_en','"//
                + Query.PARAM_CLIENT_KEY + "':'aoeu','"//
                + Query.PARAM_CUSTOM_URL_PARAMS //
                + "':'~fromGoogle=true~bigspender=1','" //
                + Query.PARAM_REQUIRED_FIELDS + "':'requiredValue','"//
                + Query.PARAM_INTERFACE_LANGUAGE + "':'en','"//
                + Query.PARAM_PARTIAL_FIELDS + "':'partialValue','"//
                + Query.PARAM_SORT + "':'relevance','" //
                + Query.PARAM_REFINEMENTS + "':'~redsox=suck','" //
                + Query.PARAM_SEARCH_STRING + "':'boston','"//
                + Query.PARAM_PAGE_SIZE + "':'10','"//
                + Query.PARAM_ACCURATE_COUNTS + "':'1','"//
                + Query.PARAM_PRUNE_REFINEMENTS + "':'1'"//
                + "}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenSubCollectionAndFrontEnd() throws Exception {
        test.setSearchString("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort("relevance");
        test.setSubCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");

        String expected = "{'" //
                + Query.PARAM_SKIP + "':'0','" //
                + Query.PARAM_SUB_FRONT_END + "':'staging','"//
                + Query.PARAM_CLIENT_KEY + "':'aoeu','"//
                + Query.PARAM_CUSTOM_URL_PARAMS //
                + "':'~fromGoogle=true~bigspender=1','"//
                + Query.PARAM_RETURN_BINARY + "':'1','"//
                + Query.PARAM_SUB_COLLECTION + "':'docs','" //
                + Query.PARAM_SORT + "':'relevance','" //
                + Query.PARAM_REFINEMENTS + "':'~redsox=suck','" //
                + Query.PARAM_SEARCH_STRING + "':'boston','"//
                + Query.PARAM_PAGE_SIZE + "':'10','"//
                + Query.PARAM_PRUNE_REFINEMENTS + "':'1'"//
                + "}";
        assertQuery(expected, test);
    }

    @Test
    public void testPruneRefinementsFalse() throws Exception {
        test.setSearchString("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort("relevance");
        test.setSubCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setPruneRefinements(false);

        String expected = "{'" //
                + Query.PARAM_SKIP + "':'0','" //
                + Query.PARAM_SUB_FRONT_END + "':'staging','"//
                + Query.PARAM_CLIENT_KEY + "':'aoeu','"//
                + Query.PARAM_CUSTOM_URL_PARAMS //
                + "':'~fromGoogle=true~bigspender=1','"//
                + Query.PARAM_RETURN_BINARY + "':'1','"//
                + Query.PARAM_SUB_COLLECTION + "':'docs','" //
                + Query.PARAM_SORT + "':'relevance','" //
                + Query.PARAM_REFINEMENTS + "':'~redsox=suck','" //
                + Query.PARAM_SEARCH_STRING + "':'boston','"//
                + Query.PARAM_PAGE_SIZE + "':'10'"//
                + "}";
        assertQuery(expected, test);
    }

    @Test
    public void testPruneRefinementsTrue() throws Exception {
        test.setSearchString("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort("relevance");
        test.setSubCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setPruneRefinements(true);

        String expected = "{'" //
                + Query.PARAM_SKIP + "':'0','" //
                + Query.PARAM_SUB_FRONT_END + "':'staging','"//
                + Query.PARAM_CLIENT_KEY + "':'aoeu','"//
                + Query.PARAM_CUSTOM_URL_PARAMS //
                + "':'~fromGoogle=true~bigspender=1','"//
                + Query.PARAM_RETURN_BINARY + "':'1','"//
                + Query.PARAM_SUB_COLLECTION + "':'docs','" //
                + Query.PARAM_SORT + "':'relevance','" //
                + Query.PARAM_REFINEMENTS + "':'~redsox=suck','" //
                + Query.PARAM_SEARCH_STRING + "':'boston','"//
                + Query.PARAM_PAGE_SIZE + "':'10','"//
                + Query.PARAM_PRUNE_REFINEMENTS + "':'1'"//
                + "}";
        assertQuery(expected, test);
    }

    @Test
    public void testRestrictedRefinements() throws Exception {
        test.setSearchString("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort("relevance");
        test.setSubCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setRestrictNavigation("4:category");

        String expected = "{'" //
                + Query.PARAM_SKIP + "':'0','" //
                + Query.PARAM_RESTRICT_NAVIGATION + "':'4:category','"//
                + Query.PARAM_SUB_FRONT_END + "':'staging','"//
                + Query.PARAM_CLIENT_KEY + "':'aoeu','"//
                + Query.PARAM_CUSTOM_URL_PARAMS //
                + "':'~fromGoogle=true~bigspender=1','"//
                + Query.PARAM_RETURN_BINARY + "':'1','"//
                + Query.PARAM_SUB_COLLECTION + "':'docs','" //
                + Query.PARAM_SORT + "':'relevance','" //
                + Query.PARAM_REFINEMENTS + "':'~redsox=suck','" //
                + Query.PARAM_SEARCH_STRING + "':'boston','"//
                + Query.PARAM_PAGE_SIZE + "':'10','"//
                + Query.PARAM_PRUNE_REFINEMENTS + "':'1'"//
                + "}";
        assertQuery(expected, test);
    }
}
