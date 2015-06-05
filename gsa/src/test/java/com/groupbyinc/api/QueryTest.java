package com.groupbyinc.api;

import com.groupbyinc.api.model.Sort;
import com.groupbyinc.api.request.RestrictNavigation;
import com.groupbyinc.test.util.AssertUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryTest {
    private Query test = new Query();

    private void assertQuery(Query pExpected, Query pActual) throws Exception {
        assertQuery(pExpected.getBridgeJson("aoeu"), pActual);
    }

    private void assertQuery(String pExpected, Query pActual) throws Exception {
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

        Query expected = new Query();
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
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort(new Sort().setField("relevance"));
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
        String expected = "{'skip':0,'biasingProfile':'flange','clientKey':'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'requiredFields':'requiredValue','partialFields':'partialValue'," +
                          "'sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'accurateCounts':'1'}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithNullLanguage() throws Exception {
        setupGeneratedQuery();

        test.setLanguageRestrict(null);
        test.setInterfaceLanguage(null);
        String expected = "{'skip':0,'biasingProfile':'flange','clientKey':'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'requiredFields':'requiredValue','partialFields':'partialValue'," +
                          "'sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'accurateCounts':'1'}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithInterfaceLanguage() throws Exception {
        setupGeneratedQuery();
        test.setInterfaceLanguage("en");
        String expected = "{'skip':0,'biasingProfile':'flange'," +
                          "'clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'requiredFields':'requiredValue','" +
                          "language':'en','partialFields':'partialValue'," +
                          "'sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'" +
                          "accurateCounts':'1'}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithLanguageRestrict() throws Exception {
        setupGeneratedQuery();
        test.setLanguageRestrict("lang_en");
        String expected = "{'skip':0,'biasingProfile':'flange'," +
                          "'clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'requiredFields':'requiredValue','" +
                          "language':'en','partialFields':'partialValue'," +
                          "'sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'" +
                          "accurateCounts':'1'}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithInvalidLanguageRestrict() throws Exception {
        setupGeneratedQuery();
        test.setLanguageRestrict("lan_en");
        String expected = "{'skip':0,'biasingProfile':'flange'," +
                          "'clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'requiredFields':'requiredValue','" +
                          "language':'lan_en','partialFields':'partialValue'," +
                          "'sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'" +
                          "accurateCounts':'1'}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenWithLanguage() throws Exception {
        setupGeneratedQuery();
        test.setLanguage("en");
        String expected = "{'skip':0,'biasingProfile':'flange'," +
                          "'clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'requiredFields':'requiredValue','" +
                          "language':'en','partialFields':'partialValue'," +
                          "'sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'" +
                          "accurateCounts':'1'}";
        assertQuery(expected, test);
    }

    @Test
    public void testGenSubCollectionAndFrontEnd() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort(new Sort().setField("relevance"));
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs','sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'returnBinary':true}";
        assertQuery(expected, test);
    }

    @Test
    public void testPruneRefinementsFalse() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort(new Sort().setField("relevance"));
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setPruneRefinements(false);

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu','customUrlParams'" +
                          ":[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs','sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'returnBinary':true,'pruneRefinements':false,'query':'boston','pageSize':10}";
        assertQuery(expected, test);
    }

    @Test
    public void testPruneRefinementsTrue() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort(new Sort().setField("relevance"));
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setPruneRefinements(true);

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs','sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'returnBinary':true,'query':'boston','pageSize':10}";
        assertQuery(expected, test);
    }

    @Test
    public void testRestrictedRefinements() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setSort(new Sort().setField("relevance").setOrder(Sort.Order.Descending));
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setRestrictNavigation(new RestrictNavigation().setName("category").setCount(4));

        String expected = "{'skip':0,'restrictNavigation':{name:'category',count: 4},'area':'staging'," +
                          "clientKey:'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}],'" +
                          "collection':'docs','sort':[{field:'relevance', order:Descending}],query:'boston'," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'returnBinary':true,'pageSize':10}";
        assertQuery(expected, test);
    }

    @Test
    public void testSortScoreDoesNotSetSort() throws Exception {
        setupGeneratedQuery();
        test.setSort(Sort.RELEVANCE);
        test.setLanguage("en");
        String expected = "{'skip':0,'biasingProfile':'flange'," +
                          "'clientKey':'aoeu','customUrlParams" +
                          "':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'requiredFields':'requiredValue','" +
                          "language':'en','partialFields':'partialValue'," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'" +
                          "accurateCounts':'1'}";
        assertQuery(expected, test);
    }
}
