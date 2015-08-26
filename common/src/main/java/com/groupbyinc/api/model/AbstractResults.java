package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.core.JsonProcessingException;
import com.groupbyinc.utils.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>
 * The results object contains a reference to all the objects that come back from the search service.
 * </code>
 *
 * @author will
 * @internal
 */
public abstract class AbstractResults<R extends AbstractRecord<R>, T extends AbstractResults<R, T>> {
    protected long totalRecordCount;
    protected String area;
    protected String biasingProfile;
    protected String redirect;
    protected String errors;
    protected String query;
    protected String originalQuery;
    protected String correctedQuery;

    protected Template template;
    protected PageInfo pageInfo = new PageInfo();
    protected List<Navigation> availableNavigation = new ArrayList<Navigation>();
    protected List<Navigation> selectedNavigation = new ArrayList<Navigation>();

    protected List<R> records = new ArrayList<R>();
    protected List<String> didYouMean = new ArrayList<String>();
    private List<String> relatedQueries = new ArrayList<String>();
    private List<String> rewrites = new ArrayList<String>();
    protected List<Metadata> siteParams = new ArrayList<Metadata>();
    protected List<Cluster> clusters;

    /**
     * @return The area that the query was run against.
     */
    public String getArea() {
        return area;
    }

    /**
     * @param pArea Set the area to run the query against
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setArea(String pArea) {
        area = pArea;
        return (T) this;
    }

    /**
     * @return A list of spell corrections based on the search term.
     */
    public List<String> getDidYouMean() {
        return didYouMean;
    }

    /**
     * @param didYouMean Set the list
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setDidYouMean(List<String> didYouMean) {
        this.didYouMean = didYouMean;
        return (T) this;
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
     * @param relatedQueries Set the related queries for a search term
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setRelatedQueries(List<String> relatedQueries) {
        this.relatedQueries = relatedQueries;
        return (T) this;
    }

    /**
     * @return A list of the records for this search and navigation state.
     */
    public List<R> getRecords() {
        return records;
    }

    /**
     * @param records Set the records.
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setRecords(List<R> records) {
        this.records = records;
        return (T) this;
    }

    /**
     * @return If a rule has fired a template will be returned specified in the
     * rule.
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @param template Set the template
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setTemplate(Template template) {
        this.template = template;
        return (T) this;
    }

    /**
     * @return Information about the results page.
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * @param pageInfo Set the page info
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return (T) this;
    }

    /**
     * @return A list of all the ways in which you can filter the current result
     * set.
     */
    public List<Navigation> getAvailableNavigation() {
        return availableNavigation;
    }

    /**
     * @param availableNavigation Set the available navigation.
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setAvailableNavigation(List<Navigation> availableNavigation) {
        this.availableNavigation = availableNavigation;
        return (T) this;
    }

    /**
     * @return A list of the currently selected refinements. Also known as breadcrumbs.
     */
    public List<Navigation> getSelectedNavigation() {
        return selectedNavigation;
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
     * @param selectedNavigation Set the selected refinements
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setSelectedNavigation(List<Navigation> selectedNavigation) {
        this.selectedNavigation = selectedNavigation;
        return (T) this;
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
     * @param redirect Set the redirect
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setRedirect(String redirect) {
        this.redirect = redirect;
        return (T) this;
    }

    /**
     * @return String representation of any errors encountered.
     */
    public String getErrors() {
        return errors;
    }

    /**
     * @param errors Set errors.
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setErrors(String errors) {
        this.errors = errors;
        return (T) this;
    }

    /**
     * @return A count of the total number of records in this search and
     * navigation state.
     */
    public long getTotalRecordCount() {
        return totalRecordCount;
    }

    /**
     * @param totalRecordCount Set the total record count.
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setTotalRecordCount(long totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
        return (T) this;
    }

    /**
     * <code>
     * A cluster represents a set of documents that are considered closely
     * related based on a search term. This object will only be populated if you
     * use the
     * bridge.searchCluster
     *
     * </code>
     *
     * @return The list of clusters.
     */
    public List<Cluster> getClusters() {
        return clusters;
    }

    /**
     * @param clusters Set the search clusters.
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
        return (T) this;
    }

    /**
     * @return A list of metadata as set in the area management section of the
     * command center.
     */
    public List<Metadata> getSiteParams() {
        return siteParams;
    }

    /**
     * @param siteParams Set the site metadata
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setSiteParams(List<Metadata> siteParams) {
        this.siteParams = siteParams;
        return (T) this;
    }

    /**
     * @return The original query sent to the search service
     */
    public String getOriginalQuery() {
        return originalQuery;
    }

    /**
     * @param originalQuery Sets the original query sent to the search service
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
        return (T) this;
    }

    /**
     * @return The corrected query sent to the engine, if auto-correction is enabled
     */
    public String getCorrectedQuery() {
        return correctedQuery;
    }

    /**
     * @param correctedQuery Sets the corrected query sent to the engine, if auto-correction is enabled
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setCorrectedQuery(String correctedQuery) {
        this.correctedQuery = correctedQuery;
        return (T) this;
    }

    /**
     * @return The query sent to the engine, after query rewrites are applied
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query Sets the query sent to the engine, after query rewrites are applied
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setQuery(String query) {
        this.query = query;
        return (T) this;
    }

    /**
     * @return A list of rewrites (spellings, synonyms, etc...) that occurred.
     */
    public List<String> getRewrites() {
        return rewrites;
    }

    /**
     * @param rewrites Set the rewrites that occurred
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setRewrites(List<String> rewrites) {
        this.rewrites = rewrites;
        return (T) this;
    }

    public String getBiasingProfile() {
        return biasingProfile;
    }

    @SuppressWarnings("unchecked")
    public T setBiasingProfile(String biasingProfile) {
        this.biasingProfile = biasingProfile;
        return (T) this;
    }
}
