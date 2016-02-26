package com.groupbyinc.util;

import com.groupbyinc.api.Query;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.api.tags.UrlFunctions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

/**
 * @author Ferron Hanse
 */
public class UrlFunctionsBugTest {

  private final String DEFAULT_BEAUTIFIER = "default";

  UrlBeautifier urlBeautifier;

  Query query;

  @Before
  public void setUp() {
    query = new Query();
    UrlBeautifier.INJECTOR.set(new HashMap<String, UrlBeautifier>());
    UrlBeautifier.createUrlBeautifier(DEFAULT_BEAUTIFIER);
    urlBeautifier = UrlBeautifier.getUrlBeautifiers()
        .get(DEFAULT_BEAUTIFIER);
    urlBeautifier.addRefinementMapping('s', "size");
    urlBeautifier.setSearchMapping('q');
    urlBeautifier.setAppend("/index.html");
    urlBeautifier.addReplacementRule('/', ' ');
    urlBeautifier.addReplacementRule('\\', ' ');
  }

  @Test
  public void refinementAdditionWithMapping() throws Exception {
    List<Navigation> navigations = new ArrayList<Navigation>();

    String refinementString = "Category Root~Athletics~Men's~Sneakers";

    String url = UrlFunctions.toUrlAdd(DEFAULT_BEAUTIFIER, "", navigations, "category_leaf_expanded", new RefinementValue().setValue(refinementString)
        .setCount(5483));
    assertEquals("/index.html?refinements=%7Ecategory_leaf_expanded%3DCategory+Root%7EAthletics%7EMen%27s%7ESneakers", url);
  }

  //GB-1811
  @Test
  public void testRefinementAdditionWithMapping() throws Exception {
    List<Navigation> navigations = singletonList(new Navigation().setName("category_leaf_expanded")
                                                     .setRefinements(singletonList(new RefinementValue().setCount(1)
                                                                                       .setValue("Category Root~Chapters" +
                                                                                                 "~030_The Market at Work: Supply and Demand_8ecad9febc35d4f752b4" +
                                                                                                 "~8ecad9febc35d4f752b4_060_Appendix: Changes in Both Demand and Supply"))));

    String url = UrlFunctions.toUrlAdd(DEFAULT_BEAUTIFIER, "", navigations, "category_leaf_expanded", null);
    assertEquals("/index.html?refinements=%7Ecategory_leaf_expanded%3DCategory+Root%7EChapters%7E030_The" +
                 "+Market+at+Work%3A+Supply+and+Demand_8ecad9febc35d4f752b4%7E8ecad9febc35d4f752b4_060_Appendix%3A" +
                 "+Changes+in+Both+Demand+and+Supply", url);
  }
}
