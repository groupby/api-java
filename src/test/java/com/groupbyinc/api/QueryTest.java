package com.groupbyinc.api;

import com.groupbyinc.api.model.Sort;
import com.groupbyinc.common.apache.commons.lang3.ArrayUtils;
import com.groupbyinc.common.test.util.AssertUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryTest {

  private Query test = new Query();

  @Test
  public void testNullSearch() throws Exception {
    test.setPageSize(100);
    String expected = "{'skip':0,'clientKey':'aoeu','pageSize':100,'returnBinary':true}";
    assertQuery(expected, test);
  }

  private void assertQuery(String expected, Query actual) throws Exception {
    AssertUtils.assertJsonEquals(expected, actual.getBridgeJson("aoeu"));
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
    test.setSort(new Sort().setField("relevance"), new Sort().setField("brand")
        .setOrder(Sort.Order.Descending));

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
    test.setSort(Sort.RELEVANCE, new Sort().setField("brand")
        .setOrder(Sort.Order.Descending));

    String expected = "{'skip':0,'area':'staging','clientKey':'aoeu'," +
                      "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                      "'collection':'docs','sort':[{field:'_relevance'}, {field:'brand', order:'Descending'}]," +
                      "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                      "'query':'boston','pageSize':10,'returnBinary':true}";
    assertQuery(expected, test);
  }

  @Test
  public void splitTestRange() {
    String[] split = test.splitRefinements("test=bob~price:10..20");
    assertArrayEquals(new String[]{"test=bob", "price:10..20"}, split);
  }

  private void assertArrayEquals(String[] expected, String[] actual) {
    Assert.assertArrayEquals(ArrayUtils.toString(actual), expected, actual);
  }

  @Test
  public void splitTestNoCategory() {
    String[] split = test.splitRefinements("~gender=Women~simpleColorDesc=Pink~product=Clothing");
    assertArrayEquals(new String[]{"gender=Women", "simpleColorDesc=Pink", "product=Clothing"}, split);
  }

  @Test
  public void splitTestCategory() {
    String[] split = test.splitRefinements("~category_leaf_expanded=Category Root~Athletics~Men's~Sneakers");

    assertArrayEquals(new String[]{"category_leaf_expanded=Category Root~Athletics~Men's~Sneakers"}, split);
  }

  @Test
  public void splitTestMultipleCategory() {
    String[] split = test.splitRefinements("~category_leaf_expanded=Category Root~Athletics~Men's~Sneakers~category_leaf_id=580003");

    assertArrayEquals(new String[]{
        "category_leaf_expanded=Category Root~Athletics~Men's~Sneakers", "category_leaf_id=580003"}, split);
  }

  @Test
  public void splitTestRangeAndMultipleCategory() {
    String[] split = test.splitRefinements("test=bob~price:10..20~category_leaf_expanded=Category Root~Athletics~Men's" + "~Sneakers~category_leaf_id=580003~color=BLUE~color=YELLOW~color=GREY");
    assertArrayEquals(new String[]{
        "test=bob", "price:10..20", "category_leaf_expanded=Category Root~Athletics~Men's~Sneakers", "category_leaf_id=580003", "color=BLUE", "color=YELLOW", "color=GREY"}, split);
  }

  @Test
  public void splitTestCategoryLong() {
    String reallyLongString = "~category_leaf_expanded=Category Root~Athletics~Men's~Sneakers~category_leaf_id=580003~" + "color=BLUE~color=YELLOW~color=GREY~feature=Lace Up~feature=Light Weight~brand=Nike";

    String[] split = test.splitRefinements(reallyLongString);

    assertArrayEquals(new String[]{
        "category_leaf_expanded=Category Root~Athletics~Men's~Sneakers", "category_leaf_id=580003", "color=BLUE", "color=YELLOW", "color=GREY", "feature=Lace Up", "feature=Light Weight", "brand=Nike"}, split);
  }

  @Test
  public void testNull() {
    String[] split = test.splitRefinements(null);
    assertArrayEquals(new String[]{}, split);
  }

  @Test
  public void testEmpty() {
    String[] split = test.splitRefinements("");
    assertArrayEquals(new String[]{}, split);
  }

  @Test
  public void testUtf8() {
    String[] split = test.splitRefinements("tëst=bäb~price:10..20");
    assertArrayEquals(new String[]{"tëst=bäb", "price:10..20"}, split);
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

  private void assertQuery(Query expected, Query actual) throws Exception {
    assertQuery(expected.getBridgeJson("aoeu"), actual);
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

  @Test
  public void testGenWithNoLanguage() throws Exception {
    setupGeneratedQuery();
    String expected = "{'skip':0,'biasingProfile':'flange','clientKey':'aoeu'," +
                      "'customUrlParams':[{'key':'fromGoogle','value':'true'},{'key':'bigspender','value':'1'}]," +
                      "'refinements':[{'navigationName':'redsox','type':'Value','value':'suck'}]," +
                      "'query':'boston','pageSize':10}";
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
