package com.groupbyinc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groupbyinc.api.parser.Model;

import java.util.Iterator;
import java.util.List;

/**
 * <code>
 * The results object contains a reference to all the objects that come back from the bridge.
 * </code>
 *
 * @author will
 * @internal
 */
public class ResultsBase<R extends RecordBase> extends Model {
    private static final long serialVersionUID = 1L;

    protected List<Navigation> availableNavigation;
    protected List<Refinement> selectedRefinements;
    protected PageInfo pageInfo;
    protected Template template;
    protected String redirect;
    protected String errors;
    protected String query;
    protected long totalRecordCount;

    protected List<Cluster> clusters;
    protected List<R> records;
    protected List<String> didYouMean;
    private List<String> relatedQueries;
    protected List<Metadata> siteParams;
    private List<String> rewrites;

    /**
     * @return
     *
     * @internal
     */
    @JsonIgnore
    public String getSelectedRefinementsJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (getSelectedRefinements() != null) {
            for (Iterator<Refinement> i = getSelectedRefinements().iterator(); i.hasNext(); ) {
                Refinement refinement = i.next();
                builder.append('"');
                builder.append(
                        encodeToJsonSafeString(
                                refinement.getNavigationName()));
                builder.append(":");
                if (refinement.isRange()) {
                    RefinementRange range = (RefinementRange) refinement;
                    builder.append(range.getLow());
                    builder.append(":");
                    builder.append(range.getHigh());
                } else {
                    RefinementValue value = (RefinementValue) refinement;
                    builder.append(encodeToJsonSafeString(value.getValue()));

                }
                builder.append('"');
                if (i.hasNext()) {
                    builder.append(',');
                }
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private static String encodeToJsonSafeString(String pOriginal) {
        return pOriginal.replaceAll("\"", "\\\\\"");

    }

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public ResultsBase() {
        // default constructor
    }

    /**
     * @return A list of spell corrections based on the search term.
     */
    public List<String> getDidYouMean() {
        return didYouMean;
    }

    /**
     * @param pDidYouMean
     *         Set the list
     *
     * @return
     */
    public ResultsBase setDidYouMean(List<String> pDidYouMean) {
        didYouMean = pDidYouMean;
        return this;
    }

    /**
     * @return A Related Query object containing a list of related queries for a
     * given search term e.g. searchTerm :- pizza, relatedQueries :-
     * pepperoni, cheese, vege, stuff crust
     */
    public List<String> getRelatedQueries() {
        return relatedQueries;
    }

    /**
     * @param pRelatedQueries
     *         Set the related queries for a search term
     *
     * @return
     */
    public ResultsBase setRelatedQueries(List<String> pRelatedQueries) {
        relatedQueries = pRelatedQueries;
        return this;
    }

    /**
     * @return A list of the records for this search and navigation state.
     */
    public List<R> getRecords() {
        return records;
    }

    /**
     * @param pRecords
     *         Set the records.
     *
     * @return
     */
    public ResultsBase setRecords(List<R> pRecords) {
        records = pRecords;
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
     * @param pTemplate
     *         Set the template
     *
     * @return
     */
    public ResultsBase setTemplate(Template pTemplate) {
        template = pTemplate;
        return this;
    }

    /**
     * @return Information about the results page.
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * @param pPageInfo
     *         Set the page info
     *
     * @return
     */
    public ResultsBase setPageInfo(PageInfo pPageInfo) {
        pageInfo = pPageInfo;
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
     * @param pRefinements
     *         Set the available navigation.
     *
     * @return
     */
    public ResultsBase setAvailableNavigation(List<Navigation> pRefinements) {
        availableNavigation = pRefinements;
        return this;
    }

    /**
     * @return A list of the currently seleccted refinemnets. Also known as
     * breadcrumbs.
     */
    public List<Refinement> getSelectedRefinements() {
        return selectedRefinements;
    }

    /**
     * @param pFilters
     *         Set the selected refinements
     *
     * @return
     */
    public ResultsBase setSelectedRefinements(List<Refinement> pFilters) {
        selectedRefinements = pFilters;
        return this;
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
     * @param pRedirect
     *         Set the redirect
     *
     * @return
     */
    public ResultsBase setRedirect(String pRedirect) {
        redirect = pRedirect;
        return this;
    }

    /**
     * @return String representation of any errors encountered.
     */
    public String getErrors() {
        return errors;
    }

    /**
     * @param pErrors
     *         Set errors.
     *
     * @return
     */
    public ResultsBase setErrors(String pErrors) {
        errors = pErrors;
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
     * @param pTotalRecordCount
     *         Set the total record count.
     *
     * @return
     */
    public ResultsBase setTotalRecordCount(long pTotalRecordCount) {
        totalRecordCount = pTotalRecordCount;
        return this;
    }

    /**
     * <code>
     * A cluster represents a set of documents that are considered closely
     * related based on a search term. This object will only be populated if you
     * use the
     * bridge.searchCluster
     * <p/>
     * </code>
     *
     * @return The list of clusters.
     */
    public List<Cluster> getClusters() {
        return clusters;
    }

    /**
     * @param pClusters
     *         Set the search clusters.
     *
     * @return
     */
    public ResultsBase setClusters(List<Cluster> pClusters) {
        clusters = pClusters;
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
     * @param pSiteParams
     *         Set the site metadata
     *
     * @return
     */
    public ResultsBase setSiteParams(List<Metadata> pSiteParams) {
        siteParams = pSiteParams;
        return this;
    }

    /**
     * @return The query sent to the bridge
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param pQuery
     *         Sets the query to the bridge
     *
     * @return
     */
    public ResultsBase setQuery(String pQuery) {
        query = pQuery;
        return this;
    }

    /**
     * @return A list of rewrites (spellings, synonyms, etc...) that occurred.
     */
    public List<String> getRewrites() {
        return rewrites;
    }

    /**
     * @param pRewrites
     *         Set the rewrites that occurred
     *
     * @return
     */
    public ResultsBase setRewrites(List<String> pRewrites) {
        rewrites = pRewrites;
        return this;
    }
}
