package com.groupbyinc.api;

import com.groupbyinc.common.test.util.AssertUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseQueryTest {

    private BaseQuery test = new BaseQuery();

    private void assertQuery(BaseQuery pExpected, BaseQuery pActual) throws Exception {
        assertQuery(pExpected.getBridgeJson("aoeu"), pActual);
    }

    private void assertQuery(String pExpected, BaseQuery pActual) throws Exception {
        AssertUtils.assertJsonEquals(pExpected, pActual.getBridgeJson("aoeu"));
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
        BaseQuery expected = new BaseQuery();
        expected.addValueRefinement("department", "VIDEO");
        expected.addRangeRefinement("regularPrice", "200.000000", "400.000000");
        assertQuery(expected, test);
    }

    @Test
    public void testNullSearch() throws Exception {
        test.setPageSize(100);
        String expected = "{'skip':0,'clientKey':'aoeu','pageSize':100,'returnBinary':true}";
        assertQuery(expected, test);
    }

    @Test
    public void testQuoteInRefinement() throws Exception {
        test.setPageSize(100);
        test.addRefinementsByString("abc=ab'");
        String expected = "{'skip':0,'clientKey':'aoeu'," +
                          "'refinements':[{'navigationName':'abc','type':'Value','value':'ab\\''}]," +
                          "'pageSize':100,'returnBinary':true}";
        assertQuery(expected, test);
    }

    @Test
    public void testMinForRanges() throws Exception {
        BaseQuery expected = new BaseQuery();
        expected.addValueRefinement("department", "VIDEO");
        expected.addRangeRefinement("regularPrice", "200.000000", "");

        test.addRefinementsByString("~department=VIDEO~regularPrice:200.000000..");
        assertQuery(expected, test);
    }

    @Test
    public void testMaxForRanges() throws Exception {
        BaseQuery expected = new BaseQuery();
        expected.addValueRefinement("department", "VIDEO");
        expected.addRangeRefinement("regularPrice", "", "100.000000");

        test.addRefinementsByString("~department=VIDEO~regularPrice:..100.000000");
        assertQuery(expected, test);
    }

    private void setupGeneratedQuery() {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.addValueRefinement("redsox", "suck");
        test.setBiasingProfile("flange");
        test.setReturnBinary(false);
    }

    @Test
    public void testGenWithNoLanguage() throws Exception {
        setupGeneratedQuery();
        String expected = "{'skip':0,'biasingProfile':'flange','clientKey':'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithNullLanguage() throws Exception {
        setupGeneratedQuery();
        test.setLanguage(null);
        String expected = "{'skip':0,'biasingProfile':'flange','clientKey':'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithLanguage() throws Exception {
        setupGeneratedQuery();
        test.setLanguage("lang_en");
        String expected = "{'skip':0,'biasingProfile':'flange','" +
                          "clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'language':'lang_en'," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenSubCollectionAndFrontEnd() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs'," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'returnBinary':true}";
        assertQuery(expected, test);
    }

    @Test
    public void testPruneRefinementsFalse() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setPruneRefinements(false);

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu','customUrlParams'" +
                          ":[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs'," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'pruneRefinements':false,'returnBinary':true,'query':'boston','pageSize':10}";
        assertQuery(expected, test);
    }

    @Test
    public void testPruneRefinementsTrue() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setPruneRefinements(true);

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs'," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'returnBinary':true,'query':'boston','pageSize':10}";
        assertQuery(expected, test);
    }

    @Test
    public void testRestrictedRefinements() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");

        String expected = "{'skip':0,'area':'staging'," +
                          "clientKey:'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}],'" +
                          "collection':'docs',query:'boston'," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'returnBinary':true,'pageSize':10}";
        assertQuery(expected, test);
    }

}
