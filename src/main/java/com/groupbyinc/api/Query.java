package com.groupbyinc.api;

import com.groupbyinc.api.model.Bias;
import com.groupbyinc.api.model.Biasing;
import com.groupbyinc.api.model.CustomUrlParam;
import com.groupbyinc.api.model.MatchStrategy;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.NumericBoost;
import com.groupbyinc.api.model.PartialMatchRule;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.Sort;
import com.groupbyinc.api.model.refinement.RefinementRange;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.api.model.sort.FieldSort;
import com.groupbyinc.api.model.sort.SortByIds;
import com.groupbyinc.api.request.RefinementsRequest;
import com.groupbyinc.api.request.Request;
import com.groupbyinc.api.request.RestrictNavigation;
import com.groupbyinc.api.request.SelectedRefinement;
import com.groupbyinc.api.request.refinement.SelectedRefinementRange;
import com.groupbyinc.api.request.refinement.SelectedRefinementValue;
import com.groupbyinc.common.apache.commons.collections4.CollectionUtils;
import com.groupbyinc.common.apache.commons.lang3.StringUtils;
import com.groupbyinc.common.jackson.Mappers;
import com.groupbyinc.common.jregex.Pattern;
import com.groupbyinc.common.security.AesContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

public class Query {

  private static final Pattern REFINEMENTS_SPLITTER_PATTERN = new Pattern("~((?=[\\w.]*[=:]))");
  private static final String[] EMPTY_REFINEMENTS = new String[]{};
  private static final Logger LOG = Logger.getLogger(Query.class.getName());
  private static final String DOTS = "\\.\\.";

  private List<Sort> sort = new ArrayList<Sort>();
  private MatchStrategy matchStrategy;
  private boolean wildcardSearchEnabled;
  private List<String> includedNavigations = new ArrayList<String>();
  private List<String> excludedNavigations = new ArrayList<String>();
  private String sessionId;
  private String visitorId;
  private String query;
  private AesContent securedPayload;
  private int skip = 0;
  private int pageSize = 10;
  private String collection;
  private String area;
  private String biasingProfile;
  private String matchStrategyName;
  private String language;
  private Map<String, String> queryUrlParams = new HashMap<String, String>();
  private List<CustomUrlParam> customUrlParams = new ArrayList<CustomUrlParam>();
  private LinkedHashMap<String, Navigation> navigations = new LinkedHashMap<String, Navigation>();
  private List<String> fields = new ArrayList<String>();
  private List<String> orFields = new ArrayList<String>();
  private List<String> subsetIds = new ArrayList<String>();
  private boolean pruneRefinements = true;
  private boolean returnBinary = true;
  private boolean disableAutocorrection = false;
  private RestrictNavigation restrictNavigation;
  private Biasing biasing = new Biasing();

  /**
   * <code>
   * Used internally by the bridge object to generate the JSON that is sent to the search service.
   * </code>
   *
   * @param clientKey
   *         The client key used to authenticate this request.
   *
   * @return A JSON representation of this query object.
   */
  public String getBridgeJson(String clientKey) {
    return requestToJson(populateRequest(clientKey));
  }

  private static String requestToJson(Request request) {
    try {
      return Mappers.writeValueAsString(request);
    } catch (IllegalArgumentException e) {
      return "{}";
    }
  }

  private Request populateRequest(String clientKey) {
    Request request = new Request();

    request.setSessionId(sessionId);
    request.setVisitorId(visitorId);
    request.setIncludedNavigations(includedNavigations);
    request.setExcludedNavigations(excludedNavigations);
    request.setClientKey(clientKey);
    request.setArea(area);
    request.setCollection(collection);
    request.setQuery(query);
    request.setFields(fields);
    request.setOrFields(orFields);
    request.setLanguage(language);
    request.setBiasingProfile(biasingProfile);
    request.setMatchStrategyName(matchStrategyName);
    request.setPageSize(pageSize);
    request.setSkip(skip);
    request.setBiasing(convertBiasing(biasing));
    request.setCustomUrlParams(getCustomUrlParams());
    request.setRefinements(generateSelectedRefinements(navigations));
    request.setNavigations(generateNavigations(navigations));
    request.setRestrictNavigation(convertRestrictNavigation());
    request.setWildcardSearchEnabled(isWildcardSearchEnabled());
    request.setSecuredPayload(securedPayload);
    if (CollectionUtils.isNotEmpty(sort)) {
      for (Sort s : sort) {
        request.setSort(convertSort(s));
      }
    }
    request.setMatchStrategy(convertPartialMatchStrategy(matchStrategy));

    if (!pruneRefinements) {
      request.setPruneRefinements(false);
    }
    if (returnBinary) {
      request.setReturnBinary(true);
    }
    if (disableAutocorrection) {
      request.setDisableAutocorrection(true);
    }
    return request;
  }

  protected static com.groupbyinc.api.request.Biasing convertBiasing(Biasing biasing) {
    com.groupbyinc.api.request.Biasing convertedBiasing = new com.groupbyinc.api.request.Biasing();
    boolean hasData = false;
    if (biasing != null) {
      convertedBiasing.setAugmentBiases(biasing.isAugmentBiases());
      if (CollectionUtils.isNotEmpty(biasing.getRestrictToIds())) {
        convertedBiasing.setRestrictToIds(new ArrayList<String>(biasing.getRestrictToIds()));
        hasData = true;
      }
      if (CollectionUtils.isNotEmpty(biasing.getBringToTop())) {
        convertedBiasing.setBringToTop(new ArrayList<String>(biasing.getBringToTop()));
        hasData = true;
      }
      if (CollectionUtils.isNotEmpty(biasing.getBiases())) {
        convertedBiasing.setBiases(new ArrayList<com.groupbyinc.api.request.Bias>(convertBiases(biasing.getBiases())));
        hasData = true;
      }
      if (biasing.getInfluence() != null) {
        convertedBiasing.setInfluence(biasing.getInfluence());
        hasData = true;
      }
      if (CollectionUtils.isNotEmpty(biasing.getNumericBoosts())) {
        convertedBiasing.setNumericBoosts(convertNumericBoosts(biasing.getNumericBoosts()));
        hasData = true;
      }
    }
    return hasData ? convertedBiasing : null;
  }

  /**
   * @return A list of custom url params
   */
  public List<CustomUrlParam> getCustomUrlParams() {
    return customUrlParams;
  }

  private List<SelectedRefinement> generateSelectedRefinements(LinkedHashMap<String, Navigation> navigations) {
    List<SelectedRefinement> refinements = new ArrayList<SelectedRefinement>();
    for (Navigation n : navigations.values()) {
      for (Refinement r : n.getRefinements()) {
        switch (r.getType()) {
          case Range: {
            RefinementRange rr = (RefinementRange) r;
            refinements.add(new SelectedRefinementRange().setNavigationName(n.getName()).setLow(rr.getLow()).setHigh(rr.getHigh()).setExclude(rr.getExclude()));
            break;
          }
          case Value: {
            RefinementValue rv = (RefinementValue) r;
            refinements.add(new SelectedRefinementValue().setNavigationName(n.getName()).setValue(rv.getValue()).setExclude(rv.getExclude()));
            break;
          }
          default:
            break;
        }
      }
    }
    return refinements;
  }

  private List<com.groupbyinc.api.request.Navigation> generateNavigations(LinkedHashMap<String, Navigation> navigations) {
    List<com.groupbyinc.api.request.Navigation> overrides = new ArrayList<com.groupbyinc.api.request.Navigation>();
    for (Navigation n : navigations.values()) {
      if (CollectionUtils.isNotEmpty(n.getPinnedRefinements())) {
        overrides.add(new com.groupbyinc.api.request.Navigation().setName(n.getName()).setPinnedRefinements(n.getPinnedRefinements()));
      }
    }
    return overrides;
  }

  private RestrictNavigation convertRestrictNavigation() {
    return restrictNavigation == null ? null : new RestrictNavigation().setName(restrictNavigation.getName()).setCount(restrictNavigation.getCount());
  }

  public boolean isWildcardSearchEnabled() {
    return wildcardSearchEnabled;
  }

  protected static com.groupbyinc.api.request.Sort convertSort(Sort sort) {
    com.groupbyinc.api.request.Sort convertedSort = null;
    if (sort != null) {
      if (sort instanceof FieldSort) {
        FieldSort fieldSort = (FieldSort) sort;
        com.groupbyinc.api.request.sort.FieldSort converted = new com.groupbyinc.api.request.sort.FieldSort().setField(fieldSort.getField());
        switch (fieldSort.getOrder()) {
          case Ascending:
            converted.setOrder(com.groupbyinc.api.request.Sort.Order.Ascending);
            break;
          case Descending:
            converted.setOrder(com.groupbyinc.api.request.Sort.Order.Descending);
            break;
          default:
            break;
        }
        convertedSort = converted;
      } else if (sort instanceof SortByIds) {
        SortByIds sortByIds = (SortByIds) sort;
        convertedSort = new com.groupbyinc.api.request.sort.SortByIds().setIds(sortByIds.getIds());
      }
    }
    return convertedSort;
  }

  protected static com.groupbyinc.api.request.MatchStrategy convertPartialMatchStrategy(MatchStrategy strategy) {
    com.groupbyinc.api.request.MatchStrategy convertedStrategy = null;
    if (strategy != null && CollectionUtils.isNotEmpty(strategy.getRules())) {
      convertedStrategy = new com.groupbyinc.api.request.MatchStrategy();
      for (PartialMatchRule r : strategy.getRules()) {
        convertedStrategy.addRule(r);
      }
    }
    return convertedStrategy;
  }

  private static List<com.groupbyinc.api.request.Bias> convertBiases(List<Bias> biases) {
    List<com.groupbyinc.api.request.Bias> convertedBiases = new ArrayList<com.groupbyinc.api.request.Bias>();
    for (Bias bias : biases) {
      convertedBiases.add(convertBias(bias));
    }
    return convertedBiases;
  }

  private static com.groupbyinc.api.request.Bias convertBias(Bias bias) {
    return new com.groupbyinc.api.request.Bias().setName(bias.getName()).setContent(bias.getContent()).setStrength(convertStrength(bias.getStrength()));
  }

  private static List<com.groupbyinc.api.request.NumericBoost> convertNumericBoosts(List<NumericBoost> numericBoosts) {
    List<com.groupbyinc.api.request.NumericBoost> convertedBiases = new ArrayList<com.groupbyinc.api.request.NumericBoost>();
    for (NumericBoost numericBoost : numericBoosts) {
      convertedBiases.add(convertNumericBoost(numericBoost));
    }
    return convertedBiases;
  }

  private static com.groupbyinc.api.request.NumericBoost convertNumericBoost(NumericBoost numericBoost) {
    return new com.groupbyinc.api.request.NumericBoost().setName(numericBoost.getName()).setStrength(numericBoost.getStrength()).setInverted(numericBoost.isInverted());
  }

  private static com.groupbyinc.api.request.Bias.Strength convertStrength(Bias.Strength strength) {
    com.groupbyinc.api.request.Bias.Strength convertedStrength;

    try {
      convertedStrength = com.groupbyinc.api.request.Bias.Strength.valueOf(strength.name());
    } catch (IllegalArgumentException e) {
      LOG.warning("Could not convert bias strength: " + strength.name());
      convertedStrength = com.groupbyinc.api.request.Bias.Strength.Leave_Unchanged;
    }

    return convertedStrength;
  }

  /**
   * <code>
   * Indicate if the *(star) character in the search string should be treated as a wildcard prefix search.
   * For example, `sta*` will match `star` and `start`.
   *
   * JSON Reference:
   *
   *     { "wildcardSearchEnabled" : true }
   *
   * </code>
   *
   * @param wildcardSearchEnabled true to enable wildcard search, false otherwise.
   * @return the Query object itself
   */
  public Query setWildcardSearchEnabled(boolean wildcardSearchEnabled) {
    this.wildcardSearchEnabled = wildcardSearchEnabled;
    return this;
  }

  /**
   * <code>
   * Used internally by the bridge object to generate the JSON that is sent to the search service.
   * </code>
   *
   * @param clientKey
   *         The client key used to authenticate this request.
   *
   * @return A JSON representation of this query object.
   */
  public String getBridgeRefinementsJson(String clientKey, String navigationName) {
    RefinementsRequest request = new RefinementsRequest();
    request.setOriginalQuery(populateRequest(clientKey));
    request.setNavigationName(navigationName);
    return requestToJson(request);
  }

  private static String requestToJson(RefinementsRequest request) {
    try {
      return Mappers.writeValueAsString(request);
    } catch (IllegalArgumentException e) {
      return "{}";
    }
  }

  /**
   * @return The current search string.
   */
  public String getQuery() {
    return query;
  }

  /**
   * <code>
   * Set a search string. If query is blank all records are considered. There are some limits enforced on the search string, it:
   *
   *          - must not exceed 60 characters
   *          - must not exceed 10 terms.
   *
   * If the limits are exceeded, the search string is truncated until all limits are satisfied. For example, the following search string
   *
   *      The quick brown fox jumps over the colorful wide bridge into the cold river.
   *
   * will get truncated to:
   *
   *      The quick brown fox jumps over the colorful wide bridge
   *
   * The terms `the`, `cold`, and `river` were truncated because the term limit was exceed, and `into` was also removed because the
   * resulting string exceeded the character limit. Stop words are included in the string when determining if limits are exceeded. If
   * there is only one term and it exceeds the character limit, the query will fail.
   *
   * JSON Reference:
   *
   *     { "query": "gloves" }
   *
   * </code>
   *
   * @param query
   *         The search string to fire against the engine.
   */
  public Query setQuery(String query) {
    this.query = query;
    return this;
  }

  /**
   * @return The data collection
   *
   * @deprecated since 2.0, use getCollection instead.
   */
  public String getSubCollection() {
    return collection;
  }

  /**
   * @param subCollection
   *         The string representation of a collection query.
   *
   * @deprecated since 2.0, use setCollection instead.
   */
  public Query setSubCollection(String subCollection) {
    collection = subCollection;
    return this;
  }

  /**
   * @return The data collection
   */
  public String getCollection() {
    return collection;
  }

  /**
   * <code>
   * The collection to use.  If you have uploaded additional data into collections apart from the default
   * collection using the stream tool, you can access them by specifying them here.
   * You can also search across multiple collections. It is important to note that relevancy is affected across
   * collections and it is recommended that collections be modeled so that cross-collection searching is not required.
   * As an example, to search across FAQs and Manuals you would use "FAQs|Manuals".
   *
   * JSON Reference:
   *
   *     { "collection": "FAQs" }
   *     { "collection": "FAQs|Manuals" }
   * </code>
   *
   * @param collection
   *         The string representation of a collection query.
   */
  public Query setCollection(String collection) {
    this.collection = collection;
    return this;
  }

  /**
   * @return The area name
   */
  public String getArea() {
    return area;
  }

  /**
   * <code>
   * The area you wish to fire against, production, staging, etc...
   * If blank, the default production area will be used.
   *
   * JSON Reference:
   *
   *     { "area": "Development" }
   *
   * </code>
   *
   * @param area
   *         The area name.
   */
  public Query setArea(String area) {
    this.area = area;
    return this;
  }

  /**
   * @return A string representation of all of the currently set refinements
   */
  public String getRefinementString() {
    if (CollectionUtils.isNotEmpty(navigations.values())) {
      StringBuilder result = new StringBuilder();
      for (Navigation n : navigations.values()) {
        for (Refinement r : n.getRefinements()) {
          result.append("~").append(n.getName()).append(r.toTildeString());
        }
      }
      if (result.length() > 0) {
        return result.toString();
      }
    }
    return null;
  }

  /**
   * @return A string representation of all of the currently set custom url
   * parameters
   */
  public String getCustomUrlParamsString() {
    if (CollectionUtils.isEmpty(customUrlParams)) {
      return null;
    }
    StringBuilder result = new StringBuilder();
    for (CustomUrlParam customUrlParam : customUrlParams) {
      result.append("~").append(customUrlParam.getKey()).append("=").append(customUrlParam.getValue());
    }
    return result.toString();
  }

  /**
   * @param clientKey
   *         Your client key
   *
   * @internal
   */
  protected String getBridgeJsonRefinementSearch(String clientKey) {
    Request request = new Request();
    request.setIncludedNavigations(includedNavigations);
    request.setExcludedNavigations(excludedNavigations);
    request.setClientKey(clientKey);
    request.setCollection(collection);
    request.setArea(area);
    request.setRefinementQuery(query);
    request.setWildcardSearchEnabled(isWildcardSearchEnabled());
    request.setSecuredPayload(securedPayload);
    if (CollectionUtils.isNotEmpty(sort)) {
      for (Sort s : sort) {
        request.setSort(convertSort(s));
      }
    }
    request.setMatchStrategy(convertPartialMatchStrategy(matchStrategy));
    return requestToJson(request);
  }

  /**
   * <code>
   * A helper method to parse and set refinements.
   * If you pass in refinements of the format
   *
   *     Brand=Bose~price:20..80
   *
   * The query object will correctly parse out the refinements.
   * </code>
   *
   * @param refinementString
   *         A tilde separated list of refinements
   */
  public Query addRefinementsByString(String refinementString) {
    if (refinementString == null) {
      return this;
    }
    String[] filterStrings = splitRefinements(refinementString);
    for (String filterString : filterStrings) {
      if (StringUtils.isBlank(filterString) || "=".equals(filterString)) {
        continue;
      }
      int colon = filterString.indexOf(":");
      int equals = filterString.indexOf("=");
      boolean isRange = colon != -1 && equals == -1;
      String[] nameValue = filterString.split("[:=]", 2);
      Refinement refinement;
      if (isRange) {
        RefinementRange rr = new RefinementRange();
        if (nameValue[1].endsWith("..")) {
          rr.setLow(nameValue[1].split(DOTS)[0]);
          rr.setHigh("");
        } else if (nameValue[1].startsWith("..")) {
          rr.setLow("");
          rr.setHigh(nameValue[1].split(DOTS)[1]);
        } else {
          String[] lowHigh = nameValue[1].split(DOTS);
          rr.setLow(lowHigh[0]);
          rr.setHigh(lowHigh[1]);
        }
        refinement = rr;
      } else {
        refinement = new RefinementValue();
        ((RefinementValue) refinement).setValue(nameValue[1]);
      }
      if (StringUtils.isNotBlank(nameValue[0])) {
        addRefinement(nameValue[0], refinement);
      }
    }
    return this;
  }

  protected String[] splitRefinements(String refinementString) {
    return StringUtils.isBlank(refinementString) ? EMPTY_REFINEMENTS : REFINEMENTS_SPLITTER_PATTERN.tokenizer(refinementString).split();
  }

  /**
   * <code>
   * Add a refinement.  Please note that refinements are case-sensitive
   *
   * JSON Reference:
   *
   * Value and range refinements are both appended to an array on the refinements field.
   * Note the 'type' field, which marks the refinement as either a value or range refinement.
   *
   *     { "refinements": [ {"type": "Range", "navigationName": "price", "low": "1.0", "high": "2.0"},
   *     {"type": "Value", "navigationName": "brand", "value": "Nike" } ] }
   *
   * Refinements can be negated by setting the exclude property. An excluded refinement will return
   * results that do not match the value or fall into the range specified in the refinement.
   *
   *     { "refinements": [ {"type": "Range", "navigationName": "price", "low": "1.0", "high": "2.0", "exclude": true},
   *     {"type": "Value", "navigationName": "brand", "value": "Nike", "exclude": true } ] }
   *
   * </code>
   *
   * @param navigationName
   *         The name of the refinement
   * @param refinement
   *         The refinement to add
   */
  private Query addRefinement(String navigationName, Refinement refinement) {
    Navigation navigation = navigations.get(navigationName);
    if (navigation == null) {
      navigation = new Navigation().setName(navigationName);
      navigation.setRange(refinement instanceof RefinementRange);
      navigations.put(navigationName, navigation);
    }
    navigation.getRefinements().add(refinement);
    return this;
  }

  /**
   * <code>
   * Sets any additional parameters that can be used to trigger rules.
   * Takes a CustomUrlParam object.
   * </code>
   *
   * @param customUrlParam
   *         The parameter to add
   */
  public Query addCustomUrlParam(CustomUrlParam customUrlParam) {
    customUrlParams.add(customUrlParam);
    return this;
  }

  /**
   * <code>
   * Sets any additional parameters that can be used to trigger rules.
   * Takes a name and a value.
   *
   * JSON Reference:
   *
   * Custom URL parameters separated by ~ in the form:
   *
   *     { "customUrlParams": [ { "key": "region", "value": "east" } ] }
   *
   * </code>
   *
   * @param key
   *         The parameter key
   * @param value
   *         The parameter value
   */
  public Query addCustomUrlParam(String key, String value) {
    customUrlParams.add(new CustomUrlParam().setKey(key).setValue(value));
    return this;
  }

  /**
   * <code>
   * Helper method that takes a ~ separated string of additional parameters that can be
   * used to trigger rules. Takes ~ separated name/value list
   * </code>
   *
   * @param values
   *         The list of name/values
   */
  public Query addCustomUrlParamsByString(String values) {
    if (values == null) {
      return this;
    }
    String[] params = values.split("&");
    for (String value : params) {
      if (StringUtils.isNotBlank(value)) {
        String[] keyValue = value.split("=");
        if (keyValue.length == 2 && StringUtils.isNotBlank(keyValue[0]) && StringUtils.isNotBlank(keyValue[1])) {
          customUrlParams.add(new CustomUrlParam().setKey(keyValue[0]).setValue(keyValue[1]));
        }
      }
    }
    return this;
  }

  /**
   * @return A list of fields that will be returned by the engine.
   */
  public List<String> getFields() {
    return fields;
  }

  /**
   * <code>
   * Specify which fields should be returned on each record that comes back from the engine. You may specify more
   * than one field, if you specify <b>\\*</b> all fields will be returned.
   * If this parameter is blank, the search service will return an error.
   * If this parameter is omitted, the search service will return only the `title` field. 
   * The `title` field is always returned.
   * You can exclude fields from being returned using `-`. Exclusion will take precedence over inclusion.
   *
   *
   *
   * JSON Reference:
   *
   *     { "fields": [ "width", "brand", "height" ] }
   *     { "fields" : [ "*", "-height", "-price" ] }
   *
   * </code>
   *
   * @param name
   *         The case-sensitive name of the attribute to return
   */
  public Query addFields(String... name) {
    return addField(fields, name);
  }

  protected Query addField(List<String> fields, String... name) {
    if (name == null) {
      return this;
    }
    Collections.addAll(fields, name);
    return this;
  }

  /**
   * @return A list of the fields that the search service will treat as OR-able.
   */
  public List<String> getOrFields() {
    return orFields;
  }

  /**
   * <code>
   * Specify which fields should be queried with 'OR' instead of the default 'AND'.
   * This behavior is typically defined in command center on a per navigation basis.  However,
   * you can set which fields should be treated as an OR field at the query level if desired.
   * As with normal refinement selections, once you have refined, the list of refinements for
   * that selected navigation will no longer be returned.
   *
   * JSON Reference:
   *
   *     { "orFields": [ "field1", "field2" ] }
   *
   * </code>
   *
   * @param name
   *         The field that should be treated as OR by the search service before
   *         being executed.
   */
  public Query addOrField(String... name) {
    return addField(orFields, name);
  }

  /**
   * <code>
   * Add a range refinement.  Takes a refinement name, a lower and upper bounds.
   * </code>
   *
   * @param navigationName
   *         The name of the refinement
   * @param low
   *         The low value
   * @param high
   *         The high value
   */
  public Query addRangeRefinement(String navigationName, String low, String high) {
    return addRangeRefinement(navigationName, low, high, false);
  }

  /**
   * <code>
   * Add a range refinement.  Takes a refinement name, a lower and upper bounds, and whether or not to exclude
   * this refinement.
   * </code>
   *
   * @param navigationName
   *         The name of the refinement
   * @param low
   *         The low value
   * @param high
   *         The high value
   * @param exclude
   *         True if the results should exclude this range refinement, false otherwise
   */
  public Query addRangeRefinement(String navigationName, String low, String high, boolean exclude) {
    return addRefinement(navigationName, new RefinementRange().setLow(low).setHigh(high).setExclude(exclude));
  }

  /**
   * <code>
   * Add a value refinement.  Takes a refinement name and a value.
   * </code>
   *
   * @param navigationName
   *         The name of the navigation
   * @param value
   *         The refinement value
   */
  public Query addValueRefinement(String navigationName, String value) {
    return addValueRefinement(navigationName, value, false);
  }

  /**
   * <code>
   * Add a value refinement.  Takes a refinement name, a value, and whether or not to exclude this refinement.
   * </code>
   *
   * @param navigationName
   *         The name of the navigation
   * @param value
   *         The refinement value
   * @param exclude
   *         True if the results should exclude this value refinement, false otherwise
   */
  public Query addValueRefinement(String navigationName, String value, boolean exclude) {
    return addRefinement(navigationName, new RefinementValue().setValue(value).setExclude(exclude));
  }

  /**
   * <code>
   * Add pinned value refinement.  Takes a refinement name and a set of values.
   * </code>
   *
   * @param navigationName
   *         The name of the navigation
   * @param values
   *         The refinement values
   *
   * @return
   */
  public Query setPinnedRefinements(String navigationName, String... values) {
    Navigation navigation = navigations.get(navigationName);
    if (navigation == null) {
      navigation = new Navigation().setName(navigationName);
      navigations.put(navigationName, navigation);
    }
    navigation.setPinnedRefinements(asList(values));
    return this;
  }

  /**
   * <code>
   * 
   * By default, the engine will return up to twenty refinements for a navigation. These refinements are ordered by either count or value. However, there are cases where the business may require a particular refinement to be always returned at the top of the list regardless of count or value (e.g. a promoted or 'house' brand.) 
   * 
   * These refinements can be defined as `pinnedRefinements` within the `navigations` array, so that they are always returned at the top of the list in the Search API Response. There is a limit of 20 `pinnedRefinements` per navigation.
   * 
   * To define `pinnedRefinements`, you must always include the navigation name within the array, as shown below:
   * 
   * 
   * <b>JSON Reference</b>:
   * 
   *     { "navigations": [ {"name": "brand", "pinnedRefinements": "Apple", "Bose", "Sennheiser"} ] }
   *  
   * </code>
   *
   * @param navigationName
   *         The name of the navigation
   * @param values
   *         The refinement values
   *
   * @return
   */
  public Query setPinnedRefinements(String navigationName, List<String> values) {
    Navigation navigation = navigations.get(navigationName);
    if (navigation == null) {
      navigation = new Navigation().setName(navigationName);
      navigations.put(navigationName, navigation);
    }
    navigation.setPinnedRefinements(new ArrayList<String>(values));
    return this;
  }

  /**
   * @return The number of documents to skip
   */
  public int getSkip() {
    return skip;
  }

  /**
   * <code>
   * Tell the search service to offset by N records. For example, if N is 10, the records returned will start at 11.
   *
   * JSON Reference:
   *
   *     { "skip": 400 }
   *
   * </code>
   *
   * @param skip
   *         The number of documents to skip
   */
  public Query setSkip(int skip) {
    this.skip = skip;
    return this;
  }

  /**
   * @return The current page size
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * <code>
   * Page size.  Default is 10.
   *
   * JSON Reference:
   *
   *     { "pageSize": 8 }
   *
   * </code>
   *
   * @param pageSize
   *         The number of records to return with the query.
   */
  public Query setPageSize(int pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * @return A map of the currently set refinements
   */
  public Map<String, Navigation> getNavigations() {
    return navigations;
  }

  /**
   * @return Is return JSON set to true.
   */
  public boolean isReturnBinary() {
    return returnBinary;
  }

  /**
   * <code>
   * Tells the search service to return binary data. This is enabled by default in the APIs for more efficient transport.
   * To disable this in an API, set this to `false`.
   *
   * JSON Reference:
   *
   * If passed true, informs the search service to return binary data rather than JSON.
   *
   *     { "returnBinary": true }
   *
   * </code>
   *
   * @param returnBinary
   *         Whether to tell the search service to return binary data rather than JSON.
   */
  public Query setReturnBinary(boolean returnBinary) {
    this.returnBinary = returnBinary;
    return this;
  }

  /**
   * @return The current biasing profile name.
   */
  public String getBiasingProfile() {
    return biasingProfile;
  }

  /**
   * <code>
   * Override the biasing profile used for this query - takes precedence over any
   * biasing profile set in the command center.
   *
   * JSON Reference:
   *
   *     { "biasingProfile": "PopularityBias" }
   *
   * </code>
   *
   * @param biasingProfile The name of the biasing profile
   */
  public Query setBiasingProfile(String biasingProfile) {
    this.biasingProfile = biasingProfile;
    return this;
  }

  /**
   * @return The current match strategy name.
   */
  public String getMatchStrategyName() {
    return matchStrategyName;
  }

  /**
   * <code>
   * Override the match strategy used for this query - takes precedence over any
   * match strategy set in the command center.
   *
   * JSON Reference:
   *
   *     { "matchStrategyName": "RelaxedMatch" }
   *
   * </code>
   *
   * @param matchStrategyName The name of the match strategy
   */
  public Query setMatchStrategyName(String matchStrategyName) {
    this.matchStrategyName = matchStrategyName;
    return this;
  }

  /**
   * @return The current language filter on the query.
   */
  public String getLanguage() {
    return language;
  }

  /**
   * <code>
   * Sets the language filter on the query and restricts the results to a certain language. If you do not specify a
   * language, english ("lang_en") will be considered the default. An unrecognized language will result in an error.
   *
   * Currently supported languages are:
   *
   *     lang_en
   *
   * JSON Reference:
   *
   *     { "language": "lang_en" }
   *
   * </code>
   *
   * @param language
   *         The value for language restrict
   */
  public Query setLanguage(String language) {
    this.language = language;
    return this;
  }

  /**
   * @return Are refinements with zero counts being removed.
   *
   * @internal
   */
  public boolean isPruneRefinements() {
    return pruneRefinements;
  }

  /**
   * <code>
   * | Parameter | Default State |
   * |----|----|
   * |`pruneRefinements` |  `true` |
   *
   * By default, the engine will only return refinements that make a difference in the returned results. This is called <b>pruning</b>.
   *
   * For example, let's say you search for "Nike Red Shoes", and 15 results come back. If we have refinements on Brand and Color, and they show:
   *
   * - `brand: Nike (15)`
   * - `color: red (15)`
   *
   * ... the engine will <i>not</i> show those refinements by default, as they make no difference.
   *
   * However, if you set `pruneRefinements` to `false`, the engine will return navigations even if they make no difference in the returned set of results.
   *
   *
   * JSON Reference:
   *
   *     { pruneRefinements: false }
   *
   * </code>
   *
   * @param pruneRefinements true to prune refinements, false other
   */
  public Query setPruneRefinements(boolean pruneRefinements) {
    this.pruneRefinements = pruneRefinements;
    return this;
  }

  /**
   * @return Is the auto-correction behavior disabled
   *
   * @internal
   */
  public boolean isAutocorrectionDisabled() {
    return disableAutocorrection;
  }

  /**
   * <code>
   * Specifies whether the auto-correction behavior should be disabled. By default, when no results are returned
   * for the given query (and there is a did-you-mean available), the first did-you-mean is automatically queried
   * instead.
   * Defaults to false
   *
   * JSON Reference:
   *
   *     { "disableAutocorrection": false }
   *
   * </code>
   *
   * @param disableAutocorrection true to disable autocorrection, false otherwise
   */
  public Query setDisableAutocorrection(boolean disableAutocorrection) {
    this.disableAutocorrection = disableAutocorrection;
    return this;
  }

  /**
   * <code>
   * <b>Warning</b>  This will count as two queries against your search index.
   * Typically, this feature is used when you have a large number of navigation items that will overwhelm the end
   * user. It works by using one of the existing navigation items to decide what the query is about and fires a second
   * query to restrict the navigation to the most relevant set of navigation items for this search term.
   * For example, if you pass in a search of `paper` and a restrict navigation of `category:2`
   * The bridge will find the category navigation refinements in the first query and fire a second query for the top 2
   * most populous categories.  Therefore, a search for something generic like "paper" will bring back top category
   * matches like copy paper (1,030), paper pads (567).  The bridge will fire off the second query with the search
   * term, plus an OR refinement with the most likely categories.  The navigation items in the first query are
   * entirely replaced with the navigation items in the second query, except for the navigation that was used for the
   * restriction so that users still have the ability to navigate by all category types.
   *
   * JSON Reference:
   *
   *     { "restrictNavigation": { "name": "category", "count": 2 } }
   *
   * </code>
   *
   * @param restrictNavigation
   *         Restriction criteria
   *
   * @return this query
   */
  public Query setRestrictNavigation(RestrictNavigation restrictNavigation) {
    this.restrictNavigation = restrictNavigation;
    return this;
  }

  /**
   * <code>
   * <b>Warning</b> See {@link Query#setRestrictNavigation(RestrictNavigation)}. This is a convenience method.
   * </code>
   *
   * @param name
   *         the name of the field should be used in the navigation restriction in the second query.
   * @param count
   *         the number of fields matches
   *
   * @return this query
   */
  public Query setRestrictNavigation(String name, int count) {
    this.restrictNavigation = new RestrictNavigation().setName(name).setCount(count);
    return this;
  }

  /**
   * @return The current list of sort parameters
   */
  public List<Sort> getSort() {
    return sort;
  }

  /**
   * <code>
   * Specifies the sort order applied to the fields in the order specified. If no sort criteria are specified, the
   * default is to sort by relevance. There is a special sort field `_relevance`, which also specifies sorting by
   * relevance. It is possible to specify multiple sort criteria. The criteria order matters, as the records will be
   * sorted by the first criteria and then any matches will be tie-broken using the next criteria. Given an example
   * where the sort is specified as `category` then `_relevance`, results will be sorted first by `category` and
   * relevance will only affect the order between records that have the same category. Records can also be sorted by
   * a specific ID as well when you want to return items in a specific order. If a record ID is included as a sort,
   * but that record not a part of the result set, that item will not be included (unlike push to top). There is a limit
   * of 1000 id's that you can sort by. Any ID's beyond this limit will be ignored. ID sort can also be used with
   * other types of sorts.
   *
   * Please note, sorting is based on the actual value in the record. For example, if sorting on `price`, and
   * `price` is a `Range` navigation, the records will be sorted according to the actual price value in the record
   * and not the bucket value.
   *
   * The order field can be set to either `Ascending` or `Descending`. When sorting by relevance, the order is always
   * `Descending`. For any other field, the default order is `Ascending`.
   *
   * JSON Reference:
   *
   *     { "sort": { "field": "price", "order": "Descending" } }
   *     { "sort": [{ "field": "_relevance" }, { "field": "price", "order": "Descending" }] }
   *     { "sort": [{ "field": "brand", "order":"Ascending" }, { "field": "_relevance" }, { "field": "price" }] }
   *     { "sort": [{ "type": "ByIds", "ids": ["1234"," 5678"]}] }
   *     { "sort": [{ "type": "ByIds", "ids": ["1234"," 5678"]}, { "field": "price", "order": "Descending" }] }
   *
   * </code>
   *
   * @param sort Any number of sort criteria.
   */
  public Query setSort(Sort... sort) {
    CollectionUtils.addAll(this.sort, sort);
    return this;
  }

  /**
   * @return The current match strategy.
   */
  public MatchStrategy getMatchStrategy() {
    return this.matchStrategy;
  }

  /**
   * <code>
   * A match strategy allows you to explicitly manage recall on a per query basis. There must always be one term
   * matching in a query, thus `termsGreaterThan` can only be defined from 1 upwards and `terms` can only be defined
   * from 2 upwards. It is not possible to match more terms than passed into the query. Relative `mustMatch` values
   * can be used in conjunction with `termsGreaterThan`. A `"percentage": true` flag denotes a relative `mustMatch`
   * to the portion of the terms and will always round down (i.e. 50% must match of 3 terms, means that 1 term must
   * match).
   *
   * The following is the default match strategy:
   *
   * ```
   * { "matchStrategy": { "rules":[{ "terms": 2, "mustMatch": 2 },
   *                               { "terms": 3, "mustMatch": 2 },
   *                               { "terms": 4, "mustMatch": 3 },
   *                               { "terms": 5, "mustMatch": 3 },
   *                               { "terms": 6, "mustMatch": 4 },
   *                               { "terms": 7, "mustMatch": 4 },
   *                               { "terms": 8, "mustMatch": 5 },
   *                               { "termsGreaterThan": 8, "mustMatch": 60, "percentage": true }] } }
   * ```
   *
   * An exact matching strategy would be:
   *
   * ```
   * { "matchStrategy": { "rules": { "termsGreaterThan": 1, "mustMatch": 100, "percentage": true } } }
   * ```
   *
   * Please note, it is highly recommended that the highest rule is defined with `termsGreaterThan`
   * and a relative `mustMatch` as that guarantees that the number of matches required grows with the number of terms
   * passed into the query.
   *
   * JSON Reference:
   *
   *     { "matchStrategy": { "rules":[{ "terms": 2, "mustMatch": 2 },
   *                                   { "terms": 3, "mustMatch": 2 },
   *                                   { "terms": 4, "mustMatch": 3 },
   *                                   { "terms": 5, "mustMatch": 3 },
   *                                   { "terms": 6, "mustMatch": 4 },
   *                                   { "terms": 7, "mustMatch": 4 },
   *                                   { "terms": 8, "mustMatch": 5 },
   *                                   { "termsGreaterThan": 8, "mustMatch": 60, "percentage": true }] } }
   *     { "matchStrategy": { "rules": { "termsGreaterThan": 1, "mustMatch": 100, "percentage": true } } }
   *     { "matchStrategy": { "rules":[{ "terms": 2, "mustMatch": 1 },
   *                                   { "termsGreaterThan": 2, "mustMatch": 75, "percentage": true }] } }
   *
   * </code>
   *
   * @param matchStrategy A match strategy composed of partial matching rules.
   */
  public Query setMatchStrategy(MatchStrategy matchStrategy) {
    this.matchStrategy = matchStrategy;
    return this;
  }

  /**
   * @return A list of navigations that will be included with the response.
   */
  public List<String> getIncludeNavigations() {
    return includedNavigations;
  }

  /**
   * <code>
   * An array that specifies which navigations should be returned. 
   *
   * If set, this overrides the navigations defined in Command Center and only returns the navigations specified.
   * If this parameter is blank the Dynamic Navigations from Command Center are returned.
   *
   * The values here must be defined via Command Center or Bulk Upload. If a navigation is specified that has not been defined,
   * it will be ignored. 
   *
   * This means, if this parameter uses a `dummy` navigation that is not real, this will both override any Command Center definitions, and will return nothing, as the navigation does not exist.
   *
   *
   * The field name supports two types of wildcard characters: '?' and '\*'.
   * The '?' wildcard will match one character. For example "????_price" will match "sale_price",
   * but not "sales_price". The '\*' wildcard will match any number of characters. For example, a
   * name of "\*_price" will match both "sale_price and "sales_price", but not "sale_prices".
   *
   *
   *
   * JSON Reference:
   *
   *     { "includedNavigations": [ "width", "brand", "categories.categories.value" ] }
   * </code>
   *
   * @param navigationName
   *         The case-sensitive name of the navigation to return
   */
  public Query addIncludedNavigations(String... navigationName) {
    return addField(includedNavigations, navigationName);
  }

  /**
   * @return A list of navigations that will be excluded from the response.
   */
  public List<String> getExcludeNavigations() {
    return excludedNavigations;
  }

  /**
   * <code>
   * Specify which navigations should not be returned. If set, this forces the response to
   * exclude certain navigations defined in Command Center. If this parameter is blank all
   * navigations in Command Center are returned. If a navigation name is specified that does
   * not exist, it will be ignored. If "includedNavigations" are specified, then all
   * "excludedNavigations" are ignored. Please see the documentation on "includedNavigations"
   * for details on wildcard characters in the field name.
   *
   * JSON Reference:
   *
   *     { "excludedNavigations": [ "width", "brand", "categories.categories.value" ] }
   *
   * </code>
   *
   * @param navigationName
   *         The case-sensitive name of the navigation to exclude
   */
  public Query addExcludedNavigations(String... navigationName) {
    return addField(excludedNavigations, navigationName);
  }

  /**
   * @return The query level url-parameters.
   */
  public Map<String, String> getQueryUrlParams() {
    return queryUrlParams;
  }

  /**
   * <code>
   *
   * Sets the query level url parameters. These will be used in the future to enable and disable
   * features, such as disabling Navigations in the response.
   *
   * </code>
   *
   * @param queryUrlParams
   *         The map of query level url parameters
   */
  public Query setQueryUrlParams(Map<String, String> queryUrlParams) {
    this.queryUrlParams = queryUrlParams;
    return this;
  }

  /**
   * <code>
   *
   * See {@link Query#setQueryUrlParams(Map)}. This is a convenience method for when you have no
   * value for the url parameter.
   *
   * </code>
   *
   * @param key
   *         The key of the url parameter
   */
  public Query addQueryUrlParams(String key) {
    return addQueryUrlParams(key, null);
  }

  /**
   * <code>
   *
   * See {@link Query#setQueryUrlParams(Map)}.
   *
   * </code>
   *
   * @param key
   *         The key of the url parameter
   * @param value
   *         The value of the url parameter
   */
  public Query addQueryUrlParams(String key, String value) {
    this.queryUrlParams.put(key, value);
    return this;
  }

  /**
   * <code>
   *
   * See {@link Query#setBiasing(Biasing)}. This is a convenience method to set which products should be
   * brought to the top of the result set.
   *
   * </code>
   *
   * @param bringToTop
   *         Any number of product IDs to bring to the top of the result set.
   */
  public Query setBringToTop(String... bringToTop) {
    CollectionUtils.addAll(this.biasing.getBringToTop(), bringToTop);
    return this;
  }

  /**
   * <code>
   *
   * See {@link Query#setBiasing(Biasing)}. This is a convenience method to set the list of products IDs that will be
   * used for `restrictToIds`.
   *
   * </code>
   *
   * @param restrictToIds
   *         Any number of product IDs to bring to the top of the result set.
   */
  public Query setRestrictToIds(String... restrictToIds) {
    CollectionUtils.addAll(this.biasing.getRestrictToIds(), restrictToIds);
    return this;
  }

  /**
   * <code>
   *
   * See {@link Query#setBiasing(Biasing)}. This is a convenience method to set the biasing augment status.
   *
   * </code>
   *
   * @param augment
   *         True to replace the biases defined in Command Center, false to augment.
   */
  public Query setBiasingAugment(boolean augment) {
    biasing.setAugmentBiases(augment);
    return this;
  }

  /**
   * <code>
   *
   * See {@link Query#setBiasing(Biasing)}. This is a convenience method to set the biasing influence.
   *
   * </code>
   *
   * @param influence
   *         The influence
   */
  public Query setInfluence(Float influence) {
    biasing.setInfluence(influence);
    return this;
  }

  /**
   * <code>
   *
   * See {@link Query#setBiasing(Biasing)}. This is a convenience method to add an individual bias.
   *
   * </code>
   *
   * @param name
   *         The name of the field to bias on
   * @param content
   *         The value to bias
   * @param strength
   *         The strength of the bias. Legal values are: "Absolute_Increase", "Strong_Increase", "Medium_Increase",
   *         "Weak_Increase", "Leave_Unchanged", "Weak_Decrease", "Medium_Decrease", "Strong_Decrease", "Absolute_Decrease".
   */
  public Query addBias(String name, String content, Bias.Strength strength) {
    biasing.getBiases().add(new Bias().setName(name).setContent(content).setStrength(strength));
    return this;
  }

  /**
   * @return The session ID
   */
  public String getSessionId() {
    return sessionId;
  }

  /**
   * <code>
   *  A unique string identifier of the session that your customer is currently in. The sessionID should be a unique value for a
   *  given user that persists for them as long as that user is active on the site for that session. We define a session as the
   *  time that you would consider a duration of an A/B test. In future, A/B testing tools within our solution will leverage
   *  the session ID to group customers into different experiences. Ensuring that session ID is persistent throughout a measure
   *  of time will help ensure that the customer experience is consistent as they shop and browse your site. Therefore, the
   *  sessionID should update only if the user is inactive for some period - we recommend keeping this in alignment for what
   *  you consider a shopping session for your customers. For example, you can align this to the timeout of items stored in the
   *  shopping cart. Session ID should not change when the user logs in and can be used to track a user changing from anonymous
   *  to logged in. Session ID must also be consistent between the Search and Recommendations APIs to ensure correct monitoring of
   *  conversion metrics.
   *
   * |@warn
   * | Sending raw session IDs is a security risk. Encrypt or hash session IDs prior to transmission.
   *
   * </code>
   * @param sessionId
   *         The session ID
   */
  public Query setSessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * @return The user ID
   */
  public String getVisitorId() {
    return visitorId;
  }

  /**
   * <code>
   *
   *  A unique string identifier of an end customer. Anonymous users (not logged in) should have a visitorID that is
   *  a randomly generated v4 UUID. This visitorID should stay with the anonymous user for as long as possible or
   *  until they log in. When a user logs in, their visitorID change to a known globally unique identifier for that customer.
   *  Visitor ID should remain the same for a particular customer over different sessions.  Also, it must be consistent
   *  between the Search and Recommendations APIs to ensure correct monitoring of conversion metrics.
   *
   * |@warn
   * | Sending raw session IDs is a security risk. Encrypt or hash session IDs prior to transmission.
   *
   * </code>
   * @param visitorId
   *         The visitor ID
   */
  public Query setVisitorId(String visitorId) {
    this.visitorId = visitorId;
    return this;
  }

  /**
   * @return The biasing
   */
  public Biasing getBiasing() {
    return biasing;
  }

  /**
   * <code>
   * Add a biasing profile, which is defined at query time. Possible settings
   * include:
   *
   *  - `bringToTop`: A list of product IDs to bring to the top of the result set. This list
   *  will ensure that the products are included in the result set and appear in the order
   *  defined.
   *  - `restrictToIds`: A list of product IDs that will be used as the subset on which any subsequent refinements, search, sort,
   *  and/or bias will be conducted on. For example, you can pass in a list of IDs within which to search. The list is not order sensitive.
   *  Records will only return if their ID is defined in this list *and* exist in the queried result set. This operation has a 1000 ID limit.
   *  - `influence`: The influence to apply to query-time biases and biases set in Command Center.
   *  If this field is not defined, then the influence of the biasing profile defined in Command Center will take effect.
   *  If an influence is not defined in Command Center, then the influence will default to 5.
   *  - `augmentBiases`: If true, the biases defined here will augment biasing profiles defined in Command Center.
   *  Otherwise, the biases will override the ones defined in command Center. By default, this is set to false.
   *  - `biases`: A list of biases, which either override or augment biasing profiles defined
   *  in Command Center. See the documentation for `addBias` for more information.
   * - `numericBoosts`: A list of numeric boosts, which will override or augment biasing profiles defined in Command Center. 
   *
   * |@tip
   * | #### When to use it
   * |
   * | `restrictToIds` is especially useful when you need to request a subset of your records due to information from a 3rd party source.
   * | All the typical search operations: search, navigation, biasing, sort, and so on - can be conducted on this set.
   *
   * |@warn
   * | When both `bringToTop` and `restrctToIds` are used, the ids specified in `bringToTop` will be first,
   * | followed by the ids in the second set, with any subsequent biasing, sort, refinements, and query operations applied to them.
   *
   * JSON Reference:
   *
   *     { "biasing": {
   *          "bringToTop": ["productId1","productId3","productId2"],
   *          "restrictToIds": ["productId1","productId3","productId2"],
   *          "influence": 5.0,
   *          "augmentBiases": false,
   *          "biases": [
   *               {"name":"brand", "content":"Brand A", "strength":"Medium_Increase"},
   *               {"name":"brand", "content":"Brand B", "strength":"Strong_Increase"},
   *               {"name":"material", "content":"Material A", "strength":"Strong_Decrease"}
   *          ],
   *         "numericBoosts": [
   *          {"name":"size", "strength":10, "inverted":false},
   *          {"name":"price", "strength":0.001, "inverted":true}
   *     ]
   *     }}
   *
   *
   * </code>
   *
   * @param biasing
   *         The biasing parameters
   *
   * @internal
   */
  public Query setBiasing(Biasing biasing) {
    this.biasing = biasing;
    return this;
  }

  /**
   * <code>
   * Add a secured payload to the query.
   *
   *
   * JSON Reference:
   *
   *     {
   *       "securedPayload": {
   *          "cipherText":"",
   *          "initialValue":"",
   *          "messageAuthenticationCode":""
   *       }
   *     }
   *
   *
   * </code>
   *
   * @param securedPayload
   *         The secured payload received at login
   *
   * @internal
   */
  public Query setSecuredPayload(AesContent securedPayload) {
    this.securedPayload = securedPayload;
    return this;
  }
}
