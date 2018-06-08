package com.groupbyinc.api.request;

import com.groupbyinc.api.model.CustomUrlParam;
import com.groupbyinc.api.request.refinement.SelectedRefinementValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.groupbyinc.common.test.util.AssertUtils.assertJsonEquals;
import static java.util.Arrays.asList;

public class RequestUtilTest {

  @Test
  public void testNormalizeRequestWithCustomUrlParam() throws Exception {
    Request request = new Request().setCustomUrlParams(
        asList(new CustomUrlParam().setKey("size").setValue("absolute unit"), new CustomUrlParam().setKey("color").setValue("red"), new CustomUrlParam().setKey("color").setValue("blue")));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'customUrlParams':[{'key':'color','value':'blue'},{'key':'color','value':'red'},{'key':'size','value':'absolute unit'}]," + "'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithNullKeysForCustomUrlParam() throws Exception {
    Request request = new Request().setCustomUrlParams(asList(new CustomUrlParam().setKey(null).setValue("red"), new CustomUrlParam().setKey(null).setValue("blue")));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'customUrlParams':[{'value':'blue'},{'value':'red'}],'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithNullValuesCustomUrlParam() throws Exception {
    Request request = new Request().setCustomUrlParams(asList(new CustomUrlParam().setKey("color").setValue(null), new CustomUrlParam().setKey("color").setValue(null)));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'customUrlParams':[{'key':'color'},{'key':'color'}],'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithSelectedRefinementValues() throws Exception {
    List<SelectedRefinement> selectedRefinementValues = new ArrayList<SelectedRefinement>();
    selectedRefinementValues.add(new SelectedRefinementValue().setValue("red").setNavigationName("color"));
    selectedRefinementValues.add(new SelectedRefinementValue().setValue("absolute unit").setNavigationName("size"));
    selectedRefinementValues.add(new SelectedRefinementValue().setValue("blue").setNavigationName("color"));
    Request request = new Request().setRefinements(selectedRefinementValues);
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'pruneRefinements':true,'refinements':[{'navigationName':'size','type':'Value','value':'absolute unit'},{'navigationName':'color',"
                     + "'type':'Value','value':'blue'},{'navigationName':'color','type':'Value','value':'red'}]}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithSelectedRefinementValuesWithNullValues() throws Exception {
    List<SelectedRefinement> selectedRefinementValues = new ArrayList<SelectedRefinement>();
    selectedRefinementValues.add(new SelectedRefinementValue().setValue("red").setNavigationName(null));
    selectedRefinementValues.add(new SelectedRefinementValue().setValue("blue").setNavigationName(null));
    Request request = new Request().setRefinements(selectedRefinementValues);
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'pruneRefinements':true,'refinements':[{'type':'Value','value':'blue'},{'type':'Value','value':'red'}]}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithBiasesWithNullEverything() throws Exception {
    Request request =
        new Request().setBiasing(new Biasing().setBiases(asList(new Bias(), new Bias().setName("boost title").setStrength(Bias.Strength.Absolute_Increase).setContent("title"), new Bias())));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'biasing':{'biases':[{},{},{'content':'title','name':'boost title','strength':'Absolute_Increase'}]},'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithBiases() throws Exception {
    Request request = new Request().setBiasing(new Biasing().setBiases(asList(new Bias().setName("boost title").setContent("desc").setStrength(Bias.Strength.Medium_Decrease),
                                                                              new Bias().setName("boost title").setStrength(Bias.Strength.Absolute_Increase).setContent("title"))));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'biasing':{'biases':[{'content':'desc','name':'boost title','strength':'Medium_Decrease'},{'content':'title','name':'boost title',"
                     + "'strength':'Absolute_Increase'}]},'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithNumericBoostWithNullEverything() throws Exception {
    Request request = new Request().setBiasing(new Biasing().setNumericBoosts(asList(new NumericBoost(), new NumericBoost(), new NumericBoost())));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'biasing':{'numericBoosts':[{'inverted':false,'strength':1},{'inverted':false,'strength':1},{'inverted':false,'strength':1}]}," + "'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithNumericBoost() throws Exception {
    Request request = new Request().setBiasing(new Biasing().setNumericBoosts(
        asList(new NumericBoost().setName("click boost").setStrength(20), new NumericBoost().setName("click boost").setStrength(5),
               new NumericBoost().setName("some other numeric boost").setInverted(true).setStrength(5))));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'biasing':{'numericBoosts':[{'inverted':false,'name':'click boost','strength':5},{'inverted':false,'name':'click boost','strength':20},{'inverted':true,"
                     + "'name':'some other numeric boost','strength':5}]},'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithRestrictToIds() throws Exception {
    Request request = new Request().setBiasing(new Biasing().setRestrictToIds(asList("a", null, "b", null)));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'biasing':{'restrictToIds':[null,null,'a','b']},'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithIncludedNavigations() throws Exception {
    Request request = new Request().setIncludedNavigations(asList("a", null, "b", null));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'includedNavigations':[null,null,'a','b'],'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithExcludedNavigations() throws Exception {
    Request request = new Request().setExcludedNavigations(asList("a", null, "b", null));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'excludedNavigations':[null,null,'a','b'],'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithOrFields() throws Exception {
    Request request = new Request().setOrFields(asList("a", null, "b", null));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'orFields':[null,null,'a','b'],'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithFields() throws Exception {
    Request request = new Request().setFields(asList("a", null, "b", null));
    Request normalizedRequest = RequestUtil.normalizeRequest(request);
    assertJsonEquals("{'fields':[null,null,'a','b'],'pruneRefinements':true}", normalizedRequest);
  }

  @Test
  public void testNormalizeRequestWithNullRequest() {
    Assert.assertNull(RequestUtil.normalizeRequest(null));
  }

  @Test
  public void testNormalizeCacheKeyWithNullRequest() {
    Assert.assertNull(RequestUtil.normalizeCacheKey(null));
  }
}