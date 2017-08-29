package com.groupbyinc.util;

import com.groupbyinc.api.Query;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.common.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class UrlBeautifierTest {

  private UrlBeautifier test;

  @Before
  public void before() {
    UrlBeautifier.INJECTOR.set(new HashMap<String, UrlBeautifier>());
    UrlBeautifier.createUrlBeautifier("default");
    test = UrlBeautifier.getUrlBeautifiers().get("default");
    test.clearSavedFields();
  }

  @Test
  public void testStopVowels() throws Exception {
    try {
      test.addRefinementMapping('u', "test");
      fail("Should throw exception for vowels");
    } catch (Exception e) {
      // expected
    }
    try {
      test.setSearchMapping('e');
      fail("Should throw exception for vowels");
    } catch (Exception e) {
      // expected
    }
  }

  @Test
  public void testMultipleBeautifiers() throws Exception {
    UrlBeautifier.createUrlBeautifier("default2");
    UrlBeautifier test2 = UrlBeautifier.getUrlBeautifiers().get("default2");
    test.addRefinementMapping('t', "test");
    Map<String, Navigation> navigations = new Query().addValueRefinement("test", "value").getNavigations();
    assertEquals("/value/t", test.toUrl(null, navigations));
    assertEquals("?refinements=%7Etest%3Dvalue", test2.toUrl(null, navigations));
  }

  @Test
  public void testQueryUrl() throws Exception {
    test.setSearchMapping('q');
    String url = test.toUrl("this is a test", (Map<String, Navigation>) null);
    assertEquals("/this+is+a+test/q", url);
  }

  @Test
  public void testRefinementsUrl() throws Exception {
    test.addRefinementMapping('t', "test");
    String url = test.toUrl(null, new Query().addValueRefinement("test", "value").getNavigations());
    assertEquals("/value/t", url);
  }

  @Test
  public void testMultipleRefinements() throws Exception {
    setUpTestHeightAndCategoryRefinements();
    String url = test.toUrl("", new Query().addValueRefinement("test", "value").addValueRefinement("height", "20in").addValueRefinement("category", "computer accessories").getNavigations());
    assertEquals("/value/20in/computer+accessories/thc", url);
  }

  private void setUpTestHeightAndCategoryRefinements() {
    test.addRefinementMapping('t', "test");
    test.addRefinementMapping('h', "height");
    test.addRefinementMapping('c', "category");
  }

  @Test
  public void testFullSearchUrl() throws UrlBeautifier.UrlBeautificationException {
    test.setSearchMapping('q');
    test.addRefinementMapping('t', "test");
    String url = test.toUrl("this is a test", new Query().addValueRefinement("test", "value").getNavigations());
    assertEquals("/this+is+a+test/value/qt", url);
  }

  @Test
  public void testDetailQuery() throws Exception {
    Query query = test.fromUrl("http://example.com/details?id=243478931");
    assertEquals("~id=243478931", query.getRefinementString());
  }

  @Test
  public void testFromUrlEmptySuffix() throws Exception {
    test.setSearchMapping('q');
    test.addRefinementMapping('d', "department");
    test.setAppend("/index.html");
    Query query = test.fromUrl("http://example.com/aoeu/laptop/this+is+a+test/qd/");
    List<Navigation> navigations = toList(query.getNavigations());
    assertEquals("this is a test", ((RefinementValue) navigations.get(0).getRefinements().get(0)).getValue());
  }

  private List<Navigation> toList(Map<String, Navigation> map) {
    return new ArrayList<Navigation>(map.values());
  }

  @Test
  public void testDeepDetailQuery() throws Exception {
    Query query = test.fromUrl("http://example.com/details?p=4&id=243478931&b=test");
    assertEquals("~id=243478931", query.getRefinementString());
  }

  @Test
  public void testSearchQuery() throws Exception {
    test.setSearchMapping('q');
    test.addRefinementMapping('t', "test");
    Query query = test.fromUrl("http://example.com/this%20is%20a%20test/value/qt");
    assertEquals("this is a test", query.getQuery());
    assertEquals("~test=value", query.getRefinementString());
  }

  @Test
  public void testInvalidReferenceBlock() throws Exception {
    Query query = test.fromUrl("http://example.com/this%20is%20a%20test/value/qtrs", null);

    assertEquals(null, query);
  }

  @Test
  public void testRange() throws Exception {
    test.addRefinementMapping('t', "test");
    assertEquals("/bob/t?refinements=%7Eprice%3A10..20", test.toUrl(null, new Query().addValueRefinement("test", "bob").addRangeRefinement("price", "10", "20").getNavigations()));
    test.addRefinementMapping('p', "price");
    try {
      assertEquals("/bob/t?refinements=%7Eprice%3A10..20", test.toUrl(null, new Query().addValueRefinement("test", "bob").addRangeRefinement("price", "10", "20").getNavigations()));
      fail("Should throw exception if trying to map range");
    } catch (UrlBeautifier.UrlBeautificationException e) {
      //expected
    }
  }

  @Test
  public void testDeepSearchQuery() throws Exception {
    test.setSearchMapping('q');
    test.addRefinementMapping('t', "test");
    Query query = test.fromUrl("http://example.com/path/to/search/this%20is%20a%20test/value/qt");

    assertEquals("this is a test", query.getQuery());
    assertEquals("~test=value", query.getRefinementString());
  }

  @Test
  public void testSearchUrlBackAndForth() throws Exception {
    test.setSearchMapping('q');
    test.addRefinementMapping('t', "test");
    String url = "/this%20is%20a%20test/value/qt";
    Query query = test.fromUrl(url);

    List<Navigation> navigations = toList(query.getNavigations());
    assertEquals("test", navigations.get(0).getName());
    assertEquals("value", ((RefinementValue) navigations.get(0).getRefinements().get(0)).getValue());
    assertEquals("this is a test", query.getQuery());
  }

  @Test
  public void testExistingMapping() throws Exception {
    try {
      test.setSearchMapping('q');
      test.addRefinementMapping('q', "quasorLightLevel");
      fail("should error out");
    } catch (Exception e) {
      // Expected
      assertEquals("This token: q is already mapped to: search", e.getMessage());
    }
  }

  @Test
  public void testEmptyQueryString() throws Exception {
    Query actualQuery = test.fromUrl("/value/20in/computer%20accessories/thc?");
    assertNull(actualQuery.getQuery());

    actualQuery = test.fromUrl("");
    assertNull(actualQuery.getQuery());
  }

  @Test
  public void testAddSameRefinementMultipleTimes() throws Exception {
    test.setAppend(".html");
    setUpTestHeightAndCategoryRefinements();
    String url = test.toUrl(
        "", new Query().addValueRefinement("test", "value")
            .addValueRefinement("test", "value2")
            .addValueRefinement("height", "20in")
            .addValueRefinement("category", "computer accessories")
            .getNavigations());
    assertEquals("/value/value2/20in/computer+accessories/tthc.html", url);
  }

  @Test
  public void testAppend() throws Exception {
    test.setAppend(".html");
    setUpTestHeightAndCategoryRefinements();
    String url = test.toUrl("", new Query().addValueRefinement("test", "value").addValueRefinement("height", "20in").addValueRefinement("category", "computer accessories").getNavigations());
    assertEquals("/value/20in/computer+accessories/thc.html", url);
  }

  @Test
  public void testAppendWithSlash() throws Exception {
    test.setAppend("/index.html");
    setUpTestHeightAndCategoryRefinements();
    String url = test.toUrl("", new Query().addValueRefinement("test", "value").addValueRefinement("height", "20in").addValueRefinement("category", "computer accessories").getNavigations());

    assertEquals("/value/20in/computer+accessories/thc/index.html", url);
  }

  @Test
  public void testUnappend() throws Exception {
    test.setAppend(".html");
    test.addRefinementMapping('t', "test");
    test.addRefinementMapping('h', "height");
    Query query = test.fromUrl("/value/20in/th.html");

    List<Navigation> navigations = toList(query.getNavigations());
    assertEquals(2, navigations.size());
    assertNavigation("test", "=value", navigations.get(0));
    assertNavigation("height", "=20in", navigations.get(1));
  }

  private void assertNavigation(String expectedNavigationName, String expectedValue, Navigation navigation) {
    assertEquals(expectedNavigationName, navigation.getName());
    assertEquals(expectedValue, navigation.getRefinements().get(0).toTildeString());
  }

  @Test
  public void testUnappendWithSlash() throws Exception {
    test.setAppend("/index.html");
    test.addRefinementMapping('t', "test");
    test.addRefinementMapping('h', "height");
    Query query = test.fromUrl("/value/20in/th/index.html");

    List<Navigation> navigations = toList(query.getNavigations());
    assertEquals(2, navigations.size());
    assertNavigation("test", "=value", navigations.get(0));
    assertNavigation("height", "=20in", navigations.get(1));
  }

  @Test
  public void testUnmappedToUrl() throws Exception {
    setUpTestHeightAndCategoryRefinements();
    String url = test.toUrl(
        "", new Query().addValueRefinement("test", "value")
            .addValueRefinement("height", "20in")
            .addValueRefinement("category2", "mice")
            .addValueRefinement("cat3", "wireless mice")
            .addValueRefinement("category", "computer accessories")
            .getNavigations());
    assertEquals("/value/20in/computer+accessories/thc?refinements=%7Ecategory2%3Dmice%7Ecat3%3Dwireless+mice", url);
  }

  @Test
  public void testUnmappedToUrlWithModifiedName() throws Exception {
    setUpTestHeightAndCategoryRefinements();
    test.setRefinementsQueryParameterName("r");
    String url = test.toUrl(
        "", new Query().addValueRefinement("test", "value")
            .addValueRefinement("height", "20in")
            .addValueRefinement("category2", "mice")
            .addValueRefinement("cat3", "wireless mice")
            .addValueRefinement("category", "computer accessories")
            .getNavigations());
    assertEquals("/value/20in/computer+accessories/thc?r=%7Ecategory2%3Dmice%7Ecat3%3Dwireless+mice", url);
  }

  @Test
  public void testUnmappedFromUrl() throws Exception {
    setUpTestHeightAndCategoryRefinements();
    Query query = test.fromUrl("/value/20in/computer%20accessories/thc?refinements=%7Ecategory2%3Dmice%7Ecat3%3Dwireless%20mice");

    List<Navigation> navigations = toList(query.getNavigations());
    assertEquals(5, navigations.size());
    assertNavigation("test", "=value", navigations.get(0));
    assertNavigation("height", "=20in", navigations.get(1));
    assertNavigation("category", "=computer accessories", navigations.get(2));
    assertNavigation("category2", "=mice", navigations.get(3));
    assertNavigation("cat3", "=wireless mice", navigations.get(4));
  }

  @Test
  public void testCanonical() throws Exception {
    setUpTestHeightAndCategoryRefinements();
    assertEquals(test.toUrl(
        null, new Query().addValueRefinement("height", "20in")
            .addValueRefinement("category2", "mice")
            .addValueRefinement("cat3", "wireless mice")
            .addValueRefinement("test", "value")
            .addValueRefinement("category", "computer accessories")
            .getNavigations()), test.toUrl(
        null, new Query().addValueRefinement("height", "20in")
            .addValueRefinement("category", "computer accessories")
            .addValueRefinement("test", "value")
            .addValueRefinement("category2", "mice")
            .addValueRefinement("cat3", "wireless mice")
            .getNavigations()));
  }

  @Test
  public void testSearchWithSlash() throws Exception {
    test.setSearchMapping('q');
    assertEquals("/photo%252Fcommodity/q", test.toUrl("photo/commodity", (Map<String, Navigation>) null));
  }

  @Test
  public void testRefinementWithSlash() throws Exception {
    test.addRefinementMapping('t', "test");
    assertEquals("/photo%252Fcommodity/t", test.toUrl(null, new Query().addValueRefinement("test", "photo/commodity").getNavigations()));
  }

  @Test
  public void testUnencodePlus() throws Exception {
    test.setSearchMapping('q');
    test.addRefinementMapping('d', "department");
    test.setAppend("/index.html");
    assertFromUrl("/aoeu/laptop/MAGNOLIA+HOME+THEATR/qd/index.html", null, "MAGNOLIA HOME THEATR");
  }

  private void assertFromUrl(String purl, String searchString, String... refinements) throws Exception {
    Query query = test.fromUrl(purl);
    if (StringUtils.isNotEmpty(searchString)) {
      assertEquals(searchString, query.getQuery());
    }

    List<Navigation> navigations = toList(query.getNavigations());
    for (int i = 0; i < refinements.length; i++) {
      assertEquals(refinements[i], ((RefinementValue) navigations.get(i).getRefinements().get(0)).getValue());
    }
  }

  @Test
  public void testFromUrlWithSlash() throws Exception {
    test.setSearchMapping('q');
    test.addRefinementMapping('d', "department");
    test.setAppend("/index.html");
    assertFromUrl("/taylor/PHOTO%252FCOMMODITIES/qd/index.html", null, "PHOTO/COMMODITIES");
  }

  @Test
  public void testFromUrlWithOneReplace() throws Exception {
    setSearchAndIndex();
    test.addReplacementRule('&', ' ');
    assertToAndFromUrl("black&decker", null);
  }

  private void setSearchAndIndex() {
    test.setSearchMapping('q');
    test.setAppend("/index.html");
  }

  private void assertToAndFromUrl(String searchString, Map<String, Navigation> navigations, String... expectedRefinementsValues) throws URISyntaxException, UrlBeautifier.UrlBeautificationException {
    String url = test.toUrl(searchString, navigations);
    Query query = test.fromUrl(url);
    assertEquals(searchString, query.getQuery());

    List<Navigation> actualNavigations = toList(query.getNavigations());
    for (int i = 0; i < expectedRefinementsValues.length; i++) {
      String refinement = expectedRefinementsValues[i];
      assertEquals(refinement, ((RefinementValue) actualNavigations.get(i).getRefinements().get(0)).getValue());
    }
  }

  @Test
  public void testFromUrlWithMultipleReplace() throws Exception {
    setSearchAndIndex();
    test.addReplacementRule('&', ' ');
    test.addReplacementRule('B', 'b');
    test.addReplacementRule('D', 'd');
    assertToAndFromUrl("Black&Decker", null);
  }

  @Test
  public void testFromUrlWithOneInsert() throws Exception {
    setSearchAndIndex();
    assertFromUrl("/black+decker/q/index.html?z=i1-1", "1black decker");
  }

  @Test
  public void testFromUrlWithReplaceAndInsertionsOrderMatters() throws Exception {
    setSearchAndIndex();
    test.addReplacementRule('d', 'D');
    test.addReplacementRule('1', null);
    test.addReplacementRule('2', null);
    test.addReplacementRule('3', null);
    test.addReplacementRule('&', ' ');
    test.addReplacementRule('b', 'B');
    String searchString = "123black&decker";
    String expected = "/Black+Decker/q";
    assertEquals(expected, test.toUrl(searchString, (Map<String, Navigation>) null).substring(0, expected.length()));
    assertToAndFromUrl(searchString, null);
  }

  @Test
  public void testFromUrlBadReplace() throws Exception {
    setSearchAndIndex();
    assertFailingQuery("/black+decker/q/index.html?z=2-B--");
  }

  private void assertFailingQuery(String pUri) throws Exception {
    try {
      test.fromUrl(pUri);
      fail("Expected an exception");
    } catch (UrlBeautifier.UrlBeautificationException e) {
      //expected
    }
  }

  @Test
  public void testFromUrlBadInsert() throws Exception {
    setSearchAndIndex();
    assertFailingQuery("/black+decker/q/index.html?z=c2-B");
  }

  @Test
  public void testFromUrlBadInsert2() throws Exception {
    setSearchAndIndex();
    assertFailingQuery("/black+decker/q/index.html?z=ii2-B");
  }

  @Test
  public void testFromUrlReplaceBadIndex() throws Exception {
    setSearchAndIndex();
    assertFromUrl("/black+decker/q/index.html?z=26-R", "black decker");
  }

  @Test
  public void testFromUrlReplaceBadReplacementString() throws Exception {
    setSearchAndIndex();
    assertFailingQuery("/black+decker/q/index.html?z=-1-R");
  }

  @Test
  public void testFromUrlReplaceBadIndex3() throws Exception {
    setSearchAndIndex();
    assertFromUrl("/black+decker/q/index.html?z=0-R", "black decker");
  }

  @Test
  public void testFromUrlReplaceBadIndex4() throws Exception {
    setSearchAndIndex();
    assertFromUrl("/black+decker/q/index.html?z=13-R", "black decker");
  }

  @Test
  public void testFromUrlReplaceNoIndex() throws Exception {
    setSearchAndIndex();
    assertFailingQuery("/black+decker/q/index.html?z=-R");
  }

  @Test
  public void testFromUrlReplaceValidEdgeIndex() throws Exception {
    setSearchAndIndex();
    assertFromUrl("/black+decker/q/index.html?z=12-R", "black deckeR");
  }

  @Test
  public void testFromUrlInsertBadIndex() throws Exception {
    setSearchAndIndex();
    assertFromUrl("/black+decker/q/index.html?z=i26-R", "black decker");
  }

  @Test
  public void testFromUrlInsertMalformedIndex() throws Exception {
    setSearchAndIndex();
    assertFailingQuery("/black+decker/q/index.html?z=i-1-R");
  }

  @Test
  public void testFromUrlInsertBadIndex3() throws Exception {
    setSearchAndIndex();
    assertFromUrl("/black+decker/q/index.html?z=i0-R", "black decker");
  }

  @Test
  public void testFromUrlInsertNoIndex() throws Exception {
    setSearchAndIndex();
    assertFailingQuery("/black+decker/q/index.html?z=i-R");
  }

  @Test
  public void testFromUrlInsertValidEdgeIndex() throws Exception {
    setSearchAndIndex();
    assertFromUrl("/black+decker/q/index.html?z=i13-R-6-%26", "black&deckerR");
  }

  @Test
  public void testFromUrlWithReplace() throws Exception {
    test.setSearchMapping('q');
    test.addRefinementMapping('d', "department");
    test.addRefinementMapping('c', "category");
    test.setAppend("/index.html");
    assertFromUrl("/mice/wireless/dell/cdq/index.html?z=1-M-i14-123-18-D", "Dell", "Mice", "wireless123");
  }

  @Test
  public void testFromUrlWithReplaceFullUrl() throws Exception {
    test.setSearchMapping('q');
    test.addRefinementMapping('d', "department");
    test.addRefinementMapping('c', "category");
    test.setAppend("/index.html");
    assertFromUrl("www.example.com/mice/wireless/dell/cdq/index.html?z=1-M-i14-123-18-D", "Dell", "Mice", "wireless123");
  }

  @Test
  public void testSimpleToUrlOneReplace() throws Exception {
    test.setSearchMapping('q');
    test.addReplacementRule('/', '-');
    String searchString = "this is/a test";
    assertEquals("/this+is-a+test/q?z=8-%2F", test.toUrl(searchString, (Map<String, Navigation>) null));
    assertToAndFromUrl(searchString, null);
  }

  @Test
  public void testSimpleToUrlMultipleReplace() throws Exception {
    test.setSearchMapping('q');
    test.addReplacementRule('/', '-');
    test.addReplacementRule('T', 't');
    String searchString = "This is/a Test";
    assertEquals("/this+is-a+test/q?z=8-%2F-1-T-11-T", test.toUrl(searchString, (Map<String, Navigation>) null));
    assertToAndFromUrl(searchString, null);
  }

  @Test
  public void testSimpleToUrlReplaceWithEmpty() throws Exception {
    test.setSearchMapping('q');
    test.addReplacementRule('/', null);
    String searchString = "this is/a test";
    assertEquals("/this+isa+test/q?z=i8-%2F", test.toUrl("this is/a test", (Map<String, Navigation>) null));
    assertToAndFromUrl(searchString, null);
  }

  @Test
  public void testSimpleToUrlMultipleReplaceWithEmpty() throws Exception {
    test.setSearchMapping('q');
    test.addReplacementRule('/', null);
    test.addReplacementRule('_', null);
    String searchString = "this _is/a _test";
    assertEquals("/this+isa+test/q?z=i9-%2F-i6-_-i10-_", test.toUrl(searchString, (Map<String, Navigation>) null));
    assertToAndFromUrl(searchString, null);
  }

  @Test
  public void testSimpleToUrlMultipleReplaceOrderMatters() throws Exception {
    test.setSearchMapping('q');
    test.addReplacementRule('a', null);
    test.addReplacementRule('/', '-');
    test.addReplacementRule('_', null);
    assertToAndFromUrl("this _is/a _test", null);
  }

  @Test
  public void testToUrlWithReplace() throws Exception {
    setUpTestHeightAndCategoryRefinements();
    test.setSearchMapping('q');
    test.addReplacementRule('/', '-');
    test.addReplacementRule('&', null);
    String searchString = "test&query";
    Map<String, Navigation> navigations = new Query().addValueRefinement("test", "value").addValueRefinement("height", "20/in").addValueRefinement("category", "computer accessories").getNavigations();
    String url = test.toUrl(searchString, navigations);
    assertEquals("/value/20-in/computer+accessories/testquery/thcq?z=9-%2F-i38-%26", url);
    assertToAndFromUrl(searchString, navigations, "value", "20/in", "computer accessories");
  }

  @Test
  public void testToUrlWithReplaceDash() throws Exception {
    setUpTestHeightAndCategoryRefinements();
    test.setSearchMapping('q');
    test.addReplacementRule('-', ' ');
    test.addReplacementRule('&', null);
    String searchString = "test&query";
    Map<String, Navigation> navigations = new Query().addValueRefinement("test", "value").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    String url = test.toUrl(searchString, navigations);
    assertEquals("/value/20+in/computer+accessories/testquery/thcq?z=9---i38-%26", url);
    assertToAndFromUrl(searchString, navigations, "value", "20-in", "computer accessories");
  }

  @Test
  public void testToUrlWithReplaceWithRefinement() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.addReplacementRule('/', ' ');
    test.addReplacementRule('&', ' ', UrlBeautifier.SEARCH_NAVIGATION_NAME);
    Map<String, Navigation> navigations =
        new Query().addValueRefinement("test", "val&ue").addValueRefinement("height", "20/in").addValueRefinement("category", "computer accessories").getNavigations();
    assertToAndFromUrl("test&query", navigations, "val&ue", "20/in", "computer accessories");
  }

  private void setUpTestHeightCategoryAndSearch() {
    test.setSearchMapping('q');
    test.addRefinementMapping('t', "test");
    test.addRefinementMapping('h', "height");
    test.addRefinementMapping('c', "category");
  }

  @Test
  public void testToUrlWithUnmappedRefinements() throws Exception {
    test.addRefinementMapping('h', "height");
    test.addRefinementMapping('c', "category");
    test.setSearchMapping('q');
    test.addReplacementRule('-', ' ');
    test.addReplacementRule('&', null);
    String searchString = "test&query";
    Map<String, Navigation> navigations = new Query().addValueRefinement("test", "value").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    String url = test.toUrl(searchString, navigations);
    assertEquals("/20+in/computer+accessories/testquery/hcq?z=3---i32-%26&refinements=%7Etest%3Dvalue", url);
    assertToAndFromUrl(searchString, navigations, "20-in", "computer accessories", "value");
  }

  @Test
  public void testToAndFromUrlWithRefinementSpecificReplacements() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.setAppend("/index.html");
    test.addReplacementRule('&', ' ', UrlBeautifier.SEARCH_NAVIGATION_NAME);
    String searchString = "test&query";
    Map<String, Navigation> navigations =
        new Query().addValueRefinement("test", "val&ue").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    String url = test.toUrl(searchString, navigations);
    String expected = "/test+query/val%2526ue";
    assertEquals(expected, url.substring(0, expected.length()));
    assertToAndFromUrl(searchString, navigations, "val&ue", "20-in", "computer accessories");
  }

  @Test
  public void testToAndFromUrlWithMultipleRefinementSpecificReplacements() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.setAppend("/index.html");
    test.addReplacementRule('&', ' ', UrlBeautifier.SEARCH_NAVIGATION_NAME);
    test.addReplacementRule('i', 'm', "height");
    test.addReplacementRule('e', 'a', "category");
    String searchString = "test&query";
    Map<String, Navigation> navigations =
        new Query().addValueRefinement("test", "val&ue").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    String url = test.toUrl(searchString, navigations);
    String expected = "/test+query/val%2526ue/20-mn/computar+accassorias";
    assertEquals(expected, url.substring(0, expected.length()));
    assertToAndFromUrl(searchString, navigations, "val&ue", "20-in", "computer accessories");
  }

  @Test
  public void testToAndFromUrlWithReplaceWithSpecialChar() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.setAppend("/index.html");
    test.addReplacementRule('e', '/');
    test.addReplacementRule('a', '\\');
    Map<String, Navigation> navigations =
        new Query().addValueRefinement("test", "val&ue").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    assertToAndFromUrl("test&query", navigations, "val&ue", "20-in", "computer accessories");
  }

  @Test
  public void testToAndFromUrlWithReplaceWithSpecialChar2() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.setAppend("/index.html");
    test.addReplacementRule('e', '%');
    Map<String, Navigation> navigations =
        new Query().addValueRefinement("test", "val&ue").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    assertToAndFromUrl("test&qu%ery", navigations, "val&ue", "20-in", "computer accessories");
  }

  @Test
  public void testToAndFromUrlWithReplaceWithRegexSpecialChar() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.setAppend("/index.html");
    test.addReplacementRule('.', '%');
    Map<String, Navigation> navigations =
        new Query().addValueRefinement("test", "val&ue").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    assertToAndFromUrl("test&qu%ery", navigations, "val&ue", "20-in", "computer accessories");
  }

  @Test
  public void testToAndFromUrlWithReplaceWithRegexSpecialChar2() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.setAppend("/index.html");
    test.addReplacementRule('e', '.');
    Map<String, Navigation> navigations =
        new Query().addValueRefinement("test", "val&ue").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    assertToAndFromUrl("test&qu%ery", navigations, "val&ue", "20-in", "computer accessories");
  }

  @Test
  public void testToAndFromUrlWithReplaceWithSameChar() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.setAppend("/index.html");
    test.addReplacementRule('e', 'e');
    Map<String, Navigation> navigations =
        new Query().addValueRefinement("test", "val&ue").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    assertToAndFromUrl("test&qu%ery", navigations, "val&ue", "20-in", "computer accessories");
  }

  @Test
  public void testToAndFromUrlWithNullSearchString() throws Exception {
    setUpTestHeightCategoryAndSearch();
    test.setAppend("/index.html");
    test.addReplacementRule('e', 'e');
    Map<String, Navigation> navigations = new Query().addValueRefinement("test", "").addValueRefinement("height", "20-in").addValueRefinement("category", "computer accessories").getNavigations();
    assertToAndFromUrl(null, navigations, "", "20-in", "computer accessories");
  }
}
