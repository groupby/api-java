package com.groupbyinc.api;

import com.groupbyinc.api.model.Sort;
import com.groupbyinc.common.test.util.AssertUtils;
import org.junit.Test;

public class QueryTest {

    private Query test = new Query();

    private void assertQuery(String pExpected, Query pActual) throws Exception {
        AssertUtils.assertJsonEquals(pExpected, pActual.getBridgeJson("aoeu"));
    }

    @Test
    public void testNullSearch() throws Exception {
        test.setPageSize(100);
        String expected = "{'skip':0,'clientKey':'aoeu','pageSize':100,'returnBinary':true}";
        assertQuery(expected, test);
    }

    @Test
    public void testSingleSort() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setSort(new Sort().setField("relevance"));

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs','sort':[{field:'relevance'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'returnBinary':true}";
        assertQuery(expected, test);
    }

    @Test
    public void testMultipleSort() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setSort(new Sort().setField("relevance"), new Sort().setField("brand").setOrder(Sort.Order.Descending));

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs','sort':[{field:'relevance'}, {field:'brand', order:'Descending'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'returnBinary':true}";
        assertQuery(expected, test);
    }

    @Test
    public void testSortScore() throws Exception {
        test.setQuery("boston");
        test.addCustomUrlParamsByString("fromGoogle=true&bigspender=1");
        test.setCollection("docs");
        test.setArea("staging");
        test.addValueRefinement("redsox", "suck");
        test.setSort(Sort.RELEVANCE, new Sort().setField("brand").setOrder(Sort.Order.Descending));

        String expected = "{'skip':0,'area':'staging','clientKey':'aoeu'," +
                          "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                          "'collection':'docs','sort':[{field:'_relevance'}, {field:'brand', order:'Descending'}]," +
                          "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                          "'query':'boston','pageSize':10,'returnBinary':true}";
        assertQuery(expected, test);
    }

}
