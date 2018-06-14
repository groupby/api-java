package com.groupbyinc.api.model;

import com.groupbyinc.api.request.Request;
import com.groupbyinc.common.jackson.Mappers;
import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonProperty;
import com.groupbyinc.common.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <code>
 * The results object contains a reference to all the objects that come back from the search service.
 * </code>
 */
public class Results {

  protected String id;
  protected String area;
  protected String query;
  protected List<Record> records = new ArrayList<Record>();
  // this is a hidden property
  @SuppressWarnings("unused")
  @JsonProperty
  protected DebugInfo debugInfo;
  private long totalRecordCount;
  private String biasingProfile;
  private String redirect;
  private String errors;
  private String originalQuery;
  private String correctedQuery;
  private Template template;
  private PageInfo pageInfo = new PageInfo();
  private MatchStrategy matchStrategy;
  private List<String> warnings;
  private List<Navigation> availableNavigation = new ArrayList<Navigation>();
  private List<Navigation> selectedNavigation = new ArrayList<Navigation>();
  private List<String> didYouMean = new ArrayList<String>();
  private List<Metadata> siteParams = new ArrayList<Metadata>();
  private Request originalRequest;
  private List<String> relatedQueries = new ArrayList<String>();
  private List<String> rewrites = new ArrayList<String>();
  private ResultsMetadata metadata = new ResultsMetadata();

  /**
   * @return An id that uniquely identifies this Results object.
   */
  public String getId() {
    return id;
  }

  /**
   * @param id
   *         Set the unique identifier for this Results object.
   *
   * @return
   */
  public Results setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * @return The area that the query was run against.
   */
  public String getArea() {
    return area;
  }

  /**
   * @param area
   *         Set the area to run the query against
   *
   * @return
   */
  public Results setArea(String area) {
    this.area = area;
    return this;
  }

  /**
   * @return A list of spell corrections based on the search term.
   */
  public List<String> getDidYouMean() {
    return didYouMean;
  }

  /**
   * @param didYouMean
   *         Set the list
   *
   * @return
   */
  public Results setDidYouMean(List<String> didYouMean) {
    this.didYouMean = didYouMean;
    return this;
  }

  /**
   * @return A Related Query object containing a list of related queries for a
   * given search term e.g. searchTerm :- pizza, relatedQueries :-
   * pepperoni, cheese, vegetables, stuff crust
   */
  public List<String> getRelatedQueries() {
    return relatedQueries;
  }

  /**
   * @param relatedQueries
   *         Set the related queries for a search term
   *
   * @return
   */
  public Results setRelatedQueries(List<String> relatedQueries) {
    this.relatedQueries = relatedQueries;
    return this;
  }

  /**
   * @return A list of the records for this search and navigation state.
   */
  public List<Record> getRecords() {
    return records;
  }

  /**
   * @param records
   *         Set the records.
   *
   * @return
   */
  public Results setRecords(List<Record> records) {
    this.records = records;
    return this;
  }

  /**
   * @return If a rule has fired a template will be returned specified in the
   * rule.
   */
  public Template getTemplate() {
    return template;
  }

  /**
   * @param template
   *         Set the template
   *
   * @return
   */
  public Results setTemplate(Template template) {
    this.template = template;
    return this;
  }

  /**
   * @return Information about the results page.
   */
  public PageInfo getPageInfo() {
    return pageInfo;
  }

  /**
   * @param pageInfo
   *         Set the page info
   *
   * @return
   */
  public Results setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
    return this;
  }

  /**
   * @return A list of all the ways in which you can filter the current result
   * set.
   */
  public List<Navigation> getAvailableNavigation() {
    return availableNavigation;
  }

  /**
   * @param availableNavigation
   *         Set the available navigation.
   *
   * @return
   */
  public Results setAvailableNavigation(List<Navigation> availableNavigation) {
    this.availableNavigation = availableNavigation;
    return this;
  }

  /**
   * @return A list of the currently selected refinements. Also known as breadcrumbs.
   */
  public List<Navigation> getSelectedNavigation() {
    return selectedNavigation;
  }

  /**
   * @param selectedNavigation
   *         Set the selected refinements
   *
   * @return
   */
  public Results setSelectedNavigation(List<Navigation> selectedNavigation) {
    this.selectedNavigation = selectedNavigation;
    return this;
  }

  /**
   * @return A JSON-formatted string representing the selected refinements.
   */
  @JsonIgnore
  public String getSelectedNavigationJson() {
    if (selectedNavigation != null) {
      try {
        return Mappers.JSON.writeValueAsString(selectedNavigation);
      } catch (JsonProcessingException e) {
        // Ignore
      }
    }
    return "[]";
  }

  /**
   * <code>
   * Note, if a redirect is triggered the engine does not send back records.
   * </code>
   *
   * @return A URL to redirect to based on this search term.
   */
  public String getRedirect() {
    return redirect;
  }

  /**
   * @param redirect
   *         Set the redirect
   *
   * @return
   */
  public Results setRedirect(String redirect) {
    this.redirect = redirect;
    return this;
  }

  /**
   * @return String representation of any errors encountered.
   */
  public String getErrors() {
    return errors;
  }

  /**
   * @param errors
   *         Set errors.
   *
   * @return
   */
  public Results setErrors(String errors) {
    this.errors = errors;
    return this;
  }

  /**
   * @return A count of the total number of records in this search and
   * navigation state.
   */
  public long getTotalRecordCount() {
    return totalRecordCount;
  }

  /**
   * @param totalRecordCount
   *         Set the total record count.
   *
   * @return
   */
  public Results setTotalRecordCount(long totalRecordCount) {
    this.totalRecordCount = totalRecordCount;
    return this;
  }

  /**
   * @return A list of metadata as set in the area management section of the
   * command center.
   */
  public List<Metadata> getSiteParams() {
    return siteParams;
  }

  /**
   * @param siteParams
   *         Set the site metadata
   *
   * @return
   */
  public Results setSiteParams(List<Metadata> siteParams) {
    this.siteParams = siteParams;
    return this;
  }

  /**
   * @return The original query sent to the search service
   */
  public String getOriginalQuery() {
    return originalQuery;
  }

  /**
   * @param originalQuery
   *         Sets the original query sent to the search service
   *
   * @return
   */
  public Results setOriginalQuery(String originalQuery) {
    this.originalQuery = originalQuery;
    return this;
  }

  /**
   * @return The corrected query sent to the engine, if auto-correction is enabled
   */
  public String getCorrectedQuery() {
    return correctedQuery;
  }

  /**
   * @param correctedQuery
   *         Sets the corrected query sent to the engine, if auto-correction is enabled
   *
   * @return
   */
  public Results setCorrectedQuery(String correctedQuery) {
    this.correctedQuery = correctedQuery;
    return this;
  }

  /**
   * @return The query sent to the engine, after query rewrites are applied
   */
  public String getQuery() {
    return query;
  }

  /**
   * @param query
   *         Sets the query sent to the engine, after query rewrites are applied
   *
   * @return
   */
  public Results setQuery(String query) {
    this.query = query;
    return this;
  }

  /**
   * @return A list of rewrites (spellings, synonyms, etc...) that occurred.
   */
  public List<String> getRewrites() {
    return rewrites;
  }

  /**
   * @param rewrites
   *         Set the rewrites that occurred
   *
   * @return
   */
  public Results setRewrites(List<String> rewrites) {
    this.rewrites = rewrites;
    return this;
  }

  /**
   * @return The biasing profile in effect.
   */
  public String getBiasingProfile() {
    return biasingProfile;
  }

  /**
   * @param biasingProfile
   *         Set the biasing profile in effect
   *
   * @return
   */
  public Results setBiasingProfile(String biasingProfile) {
    this.biasingProfile = biasingProfile;
    return this;
  }

  /**
   * @return A list of warnings encountered.
   */
  public List<String> getWarnings() {
    return warnings;
  }

  /**
   * @param warnings
   *         Set warnings.
   *
   * @return
   */
  public Results setWarnings(List<String> warnings) {
    this.warnings = warnings;
    return this;
  }

  /**
   * @param warnings
   *         The warnings to add
   * @return
   */
  public Results addWarnings(Collection<String> warnings) {
    if (warnings != null) {
      for (String warning : warnings) {
        addWarning(warning);
      }
    }
    return this;
  }

  /**
   * @param warning
   *         The warning to add
   * @return
   */
  public Results addWarning(String warning) {
    if (warnings == null) {
      warnings = new ArrayList<String>();
    }
    this.warnings.add(warning);
    return this;
  }

  /**
   * @return The match strategy.
   */
  public MatchStrategy getMatchStrategy() {
    return matchStrategy;
  }

  /**
   * @param matchStrategy
   *         Set the match strategy in effect
   *
   * @return
   */
  public Results setMatchStrategy(MatchStrategy matchStrategy) {
    this.matchStrategy = matchStrategy;
    return this;
  }

  /**
   * @return The original request received.
   */
  public Request getOriginalRequest() {
    return originalRequest;
  }

  /**
   * @param originalRequest
   *         Set the original request
   *
   * @return
   */
  public Results setOriginalRequest(Request originalRequest) {
    this.originalRequest = originalRequest;
    return this;
  }

  /**
   * @return The metadata associated to the Results.
   */
  public ResultsMetadata getMetadata() {
    return metadata;
  }

  /**
   * @param metadata
   *         Set the Results metadata
   *
   * @return
   */
  public Results setMetadata(ResultsMetadata metadata) {
    this.metadata = metadata;
    return this;
  }
}
