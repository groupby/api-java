package com.groupbyinc.api.request;

import com.groupbyinc.api.model.CustomUrlParam;
import com.groupbyinc.common.apache.commons.collections4.CollectionUtils;
import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonInclude;
import com.groupbyinc.common.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request object for the api to send search service requests
 *
 * @internal
 */
public class Request {

  private String clientKey;
  private String collection;
  private String area;
  private String sessionId;
  private String visitorId;
  private String biasingProfile;
  private String language;
  private String query;
  private String refinementQuery;
  private String matchStrategyName;
  private Biasing biasing;
  private RestrictNavigation restrictNavigation;
  private MatchStrategy matchStrategy;
  private Integer skip;
  private Integer pageSize;
  private Boolean returnBinary;
  private Boolean disableAutocorrection;
  @JsonIgnore private Map<String, String> queryUrlParams = new HashMap<String, String>();
  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<Sort> sort = new ArrayList<Sort>();
  @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<String> fields = new ArrayList<String>();
  @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<String> orFields = new ArrayList<String>();
  @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<SelectedRefinement> refinements = new ArrayList<SelectedRefinement>();
  @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<CustomUrlParam> customUrlParams = new ArrayList<CustomUrlParam>();
  @JsonInclude(JsonInclude.Include.NON_DEFAULT) private Boolean wildcardSearchEnabled = false;
  @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<String> includedNavigations = new ArrayList<String>();
  @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<String> excludedNavigations = new ArrayList<String>();
  @JsonInclude(JsonInclude.Include.NON_DEFAULT) private Boolean pruneRefinements = true;

  public String getClientKey() {
    return clientKey;
  }

  public Request setClientKey(String clientKey) {
    this.clientKey = clientKey;
    return this;
  }

  public String getArea() {
    return area;
  }

  public Request setArea(String area) {
    this.area = area;
    return this;
  }

  public String getCollection() {
    return collection;
  }

  public Request setCollection(String collection) {
    this.collection = collection;
    return this;
  }

  public String getSessionId() {
    return sessionId;
  }

  public Request setSessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  public String getVisitorId() {
    return visitorId;
  }

  public Request setVisitorId(String visitorId) {
    this.visitorId = visitorId;
    return this;
  }

  public String getQuery() {
    return query;
  }

  public Request setQuery(String query) {
    this.query = query;
    return this;
  }

  public Integer getSkip() {
    return skip;
  }

  public Request setSkip(Integer skip) {
    this.skip = skip;
    return this;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public Request setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  public List<CustomUrlParam> getCustomUrlParams() {
    return customUrlParams;
  }

  public Request setCustomUrlParams(List<CustomUrlParam> customUrlParams) {
    this.customUrlParams = customUrlParams;
    return this;
  }

  public List<String> getFields() {
    return fields;
  }

  public Request setFields(List<String> fields) {
    this.fields = fields;
    return this;
  }

  public List<String> getOrFields() {
    return orFields;
  }

  public Request setOrFields(List<String> orFields) {
    this.orFields = orFields;
    return this;
  }

  public String getBiasingProfile() {
    return biasingProfile;
  }

  public Request setBiasingProfile(String biasingProfile) {
    this.biasingProfile = biasingProfile;
    return this;
  }

  public List<Sort> getSort() {
    return sort;
  }

  @JsonIgnore
  public Request setSort(Sort... sort) {
    CollectionUtils.addAll(this.sort, sort);
    return this;
  }

  public Request setSort(List<Sort> sort) {
    this.sort = sort;
    return this;
  }

  public String getLanguage() {
    return language;
  }

  public Request setLanguage(String language) {
    this.language = language;
    return this;
  }

  public Boolean getPruneRefinements() {
    return pruneRefinements;
  }

  public Request setPruneRefinements(Boolean pruneRefinements) {
    this.pruneRefinements = pruneRefinements;
    return this;
  }

  public Boolean getReturnBinary() {
    return returnBinary;
  }

  public Request setReturnBinary(Boolean returnBinary) {
    this.returnBinary = returnBinary;
    return this;
  }

  public Boolean getDisableAutocorrection() {
    return disableAutocorrection;
  }

  public Request setDisableAutocorrection(Boolean disableAutocorrection) {
    this.disableAutocorrection = disableAutocorrection;
    return this;
  }

  public List<SelectedRefinement> getRefinements() {
    return refinements;
  }

  public Request setRefinements(List<SelectedRefinement> refinements) {
    this.refinements = refinements;
    return this;
  }

  public String getRefinementQuery() {
    return refinementQuery;
  }

  public Request setRefinementQuery(String refinementQuery) {
    this.refinementQuery = refinementQuery;
    return this;
  }

  public RestrictNavigation getRestrictNavigation() {
    return restrictNavigation;
  }

  public Request setRestrictNavigation(RestrictNavigation restrictNavigation) {
    this.restrictNavigation = restrictNavigation;
    return this;
  }

  public Boolean isWildcardSearchEnabled() {
    return wildcardSearchEnabled;
  }

  public Request setWildcardSearchEnabled(Boolean wildcardSearchEnabled) {
    this.wildcardSearchEnabled = wildcardSearchEnabled;
    return this;
  }

  public MatchStrategy getMatchStrategy() {
    return matchStrategy;
  }

  public Request setMatchStrategy(MatchStrategy matchStrategy) {
    this.matchStrategy = matchStrategy;
    return this;
  }

  public List<String> getIncludedNavigations() {
    return includedNavigations;
  }

  public Request setIncludedNavigations(List<String> includedNavigations) {
    this.includedNavigations = includedNavigations;
    return this;
  }

  public List<String> getExcludedNavigations() {
    return excludedNavigations;
  }

  public Request setExcludedNavigations(List<String> excludedNavigations) {
    this.excludedNavigations = excludedNavigations;
    return this;
  }

  public Biasing getBiasing() {
    return biasing;
  }

  public Request setBiasing(Biasing biasing) {
    this.biasing = biasing;
    return this;
  }

  public String getMatchStrategyName() {
    return matchStrategyName;
  }

  public Request setMatchStrategyName(String matchStrategyName) {
    this.matchStrategyName = matchStrategyName;
    return this;
  }

  public Map<String, String> getQueryUrlParams() {
    return queryUrlParams;
  }

  public Request setQueryUrlParams(Map<String, String> queryUrlParams) {
    this.queryUrlParams = queryUrlParams;
    return this;
  }
}
