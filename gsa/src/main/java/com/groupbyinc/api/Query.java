package com.groupbyinc.api;

import com.groupbyinc.api.model.Sort;
import com.groupbyinc.api.request.RefinementsRequest;
import com.groupbyinc.api.request.Request;
import com.groupbyinc.api.request.RestrictNavigation;
import com.groupbyinc.common.util.lang3.StringUtils;

public class Query extends AbstractQuery<Request, Query> {
    private boolean accurateCounts = false;
    private String partialFields;
    private String requiredFields;
    private Sort sort;

    @Override
    protected Request generateRequest() {
        Request request = new Request();
        request.setPartialFields(partialFields);
        request.setRequiredFields(requiredFields);
        request.setAccurateCounts(accurateCounts ? "1" : null);
        if (sort != null && !StringUtils.equals(sort.getField(), Sort.RELEVANCE.getField())) {
            request.setSort(convertSort(sort));
        }
        return request;
    }

    @Override
    protected RefinementsRequest<Request> populateRefinementRequest() {
        return new RefinementsRequest<Request>().setOriginalQuery(generateRequest());
    }

    /**
     * @return True if accurate counts is set to true
     * @deprecated Deprecated Deemed unuseful and will be removed in the next version.
     */
    @Deprecated
    public boolean isAccurateCounts() {
        return accurateCounts;
    }

    /**
     * <code>
     * !Warning! Tells the engine to more accurately count records and refinements.
     * This method will have severe impact on response time and throughput capacity.
     * This should only be used for debugging purposes.
     *
     * </code>
     * @param accurateCounts
     * @deprecated Deprecated Deemed unuseful and will be removed in the next release.
     */
    @Deprecated
    public void setAccurateCounts(boolean accurateCounts) {
        this.accurateCounts = accurateCounts;
    }

    /**
     * @return The partial fields value
     */
    public String getPartialFields() {
        return partialFields;
    }

    /**
     * <code>
     * A direct pass through of the Partial Fields parameter for the GSA.  See the GSA documentation for further
     * details.
     * [GSA 7.2 Partial Fields Reference](http://www.google.com/support/enterprise/static/gsa/docs/admin/72/gsa_doc_set/xml_reference/request_format.html#1077773)
     *
     * JSON Reference:
     *
     *     { "partialFields": "..." }
     *
     * </code>
     *
     * @param partialFields
     *         The partial fields value
     *
     * @return
     */
    @Deprecated
    public Query setPartialFields(String partialFields) {
        this.partialFields = partialFields;
        return this;
    }

    /**
     * @return The current required fields value.
     */
    @Deprecated
    public String getRequiredFields() {
        return requiredFields;
    }

    /**
     * <code>
     * A direct pass through of the Required Fields parameter for the GSA.  See the GSA documentation for further
     * details.
     * [GSA 7.2 Required Fields Reference](http://www.google.com/support/enterprise/static/gsa/docs/admin/72/gsa_doc_set/xml_reference/request_format.html#1077773)
     *
     * JSON Reference:
     *
     *     { "requiredFields": "..." }
     *
     * </code>
     *
     * @param requiredFields
     *
     * @return
     */
    @Deprecated
    public Query setRequiredFields(String requiredFields) {
        this.requiredFields = requiredFields;
        return this;
    }

    /**
     * @return The current language restrict value.
     * @deprecated 2.0.0
     */
    @Deprecated
    public String getLanguageRestrict() {
        return "lang_" + getLanguage();
    }

    /**
     * <code>
     * Sets a language filter on the query. This allows the GSA to return only results from a certain
     * language as well as do accent-insensitive searches. Please see the GSA
     * documentation under [Language Filters](http://www.google.com/support/enterprise/static/gsa/docs/admin/72/gsa_doc_set/xml_reference/request_format.html#1077312)
     * for a detailed list of supported languages and filter options. If you do not specify a language
     * (or pass in an unrecognized language), the language filter will be ignored.
     *
     * JSON Reference:
     *
     * { lr: 'lang_fr' }
     *
     * </code>
     *
     * @param languageRestrict The value for language restrict
     * @return
     */
    @Deprecated
    public Query setLanguageRestrict(String languageRestrict) {
        String lang;
        if (StringUtils.isNotBlank(languageRestrict) && languageRestrict.startsWith("lang_")) {
            lang = languageRestrict.substring("lang_".length());
        } else {
            lang = languageRestrict;
        }
        setLanguage(lang);
        return this;
    }

    /**
     * @return The current interface language setting.
     * @deprecated Deprecated Only need to use getLanguage()
     */
    @Deprecated
    public String getInterfaceLanguage() {
        return getLanguage();
    }

    /**
     * <code>
     * Sets the interface language (host language) of your interface. Please see the Google documentation
     * under [Interface Languages](https://developers.google.com/custom-search/docs/xml_results?hl=en&csw=1#wsInterfaceLanguages)
     * for a detailed list of supported interface languages. If you do not specify a language
     * (or pass in an unrecognized language), the interface language will be ignored.
     *
     * JSON Reference:
     *
     *     { "interfaceLanguage": "fr"}
     *
     *
     * </code>
     *
     * @param interfaceLanguage
     *         The interface language
     *
     * @return
     * @deprecated Deprecated Only need to use setLanguage
     */
    @Deprecated
    public Query setInterfaceLanguage(String interfaceLanguage) {
        setLanguage(interfaceLanguage);
        return this;
    }

    public RestrictNavigation getRestrictNavigation() {
        return restrictNavigation;
    }

    /**
     * @return The current sort parameter
     */
    public Sort getSort() {
        return sort;
    }

    /**
     * <code>
     * Specifies the sort order. If not specified, the default is to sort by relevance.
     * This is a direct pass through to the GSA and as such the GSA documents will provide the most
     * up to date information on this parameter, please see:
     * [GSA 7.2 Sort](http://www.google.com/support/enterprise/static/gsa/docs/admin/72/gsa_doc_set/xml_reference/request_format.html#1077686)
     *
     * </code>
     *
     * @param sort The sort criteria
     * @return
     */
    public Query setSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public Query setPruneRefinements(boolean pruneRefinements) {
        return super.setPruneRefinements(pruneRefinements);
    }
}
