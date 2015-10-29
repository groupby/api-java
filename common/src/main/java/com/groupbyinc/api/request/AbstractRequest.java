package com.groupbyinc.api.request;

import com.groupbyinc.api.model.CustomUrlParam;
import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonInclude;
import com.groupbyinc.common.jackson.annotation.JsonInclude.Include;
import com.groupbyinc.common.jackson.annotation.JsonProperty;
import com.groupbyinc.common.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Request object for the api to send search service requests
 *
 * @author Lonell Liburd
 * @internal
 */
public class AbstractRequest<T extends AbstractRequest<T>> {

    private String clientKey;
    private String collection;
    private String area;
    private String biasingProfile;
    private String language;
    private String query;
    private String refinementQuery;
    private RestrictNavigation restrictNavigation;

    @JsonProperty
    @JsonInclude(Include.NON_EMPTY)
    private List<Sort> sort = new ArrayList<Sort>();
    @JsonInclude(Include.NON_EMPTY)
    private List<String> fields = new ArrayList<String>();
    @JsonInclude(Include.NON_EMPTY)
    private List<String> orFields = new ArrayList<String>();
    @JsonInclude(Include.NON_EMPTY)
    private List<SelectedRefinement> refinements = new ArrayList<SelectedRefinement>();
    @JsonInclude(Include.NON_EMPTY)
    private List<CustomUrlParam> customUrlParams = new ArrayList<CustomUrlParam>();

    private Integer skip;
    private Integer pageSize;
    private Boolean returnBinary;
    private Boolean disableAutocorrection;
    @JsonInclude(Include.NON_DEFAULT)
    private Boolean pruneRefinements = true;

    public String getClientKey() {
        return clientKey;
    }

    @SuppressWarnings("unchecked")
    public T setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return (T) this;
    }

    public String getArea() {
        return area;
    }

    @SuppressWarnings("unchecked")
    public T setArea(String area) {
        this.area = area;
        return (T) this;
    }

    public String getCollection() {
        return collection;
    }

    @SuppressWarnings("unchecked")
    public T setCollection(String collection) {
        this.collection = collection;
        return (T) this;
    }

    public String getQuery() {
        return query;
    }

    @SuppressWarnings("unchecked")
    public T setQuery(String query) {
        this.query = query;
        return (T) this;
    }

    public Integer getSkip() {
        return skip;
    }

    @SuppressWarnings("unchecked")
    public T setSkip(Integer skip) {
        this.skip = skip;
        return (T) this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    @SuppressWarnings("unchecked")
    public T setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return (T) this;
    }

    public List<CustomUrlParam> getCustomUrlParams() {
        return customUrlParams;
    }

    @SuppressWarnings("unchecked")
    public T setCustomUrlParams(List<CustomUrlParam> customUrlParams) {
        this.customUrlParams = customUrlParams;
        return (T) this;
    }

    public List<String> getFields() {
        return fields;
    }

    @SuppressWarnings("unchecked")
    public T setFields(List<String> fields) {
        this.fields = fields;
        return (T) this;
    }

    public List<String> getOrFields() {
        return orFields;
    }

    @SuppressWarnings("unchecked")
    public T setOrFields(List<String> orFields) {
        this.orFields = orFields;
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

    public List<Sort> getSort() {
        return sort;
    }

    @SuppressWarnings("unchecked")
    public T setSort(List<Sort> sort) {
        this.sort = sort;
        return (T) this;
    }

    @JsonIgnore
    @SuppressWarnings("unchecked")
    public T setSort(Sort... sort) {
        CollectionUtils.addAll(this.sort, sort);
        return (T) this;
    }

    public String getLanguage() {
        return language;
    }

    @SuppressWarnings("unchecked")
    public T setLanguage(String language) {
        this.language = language;
        return (T) this;
    }

    public Boolean getPruneRefinements() {
        return pruneRefinements;
    }

    @SuppressWarnings("unchecked")
    public T setPruneRefinements(Boolean pruneRefinements) {
        this.pruneRefinements = pruneRefinements;
        return (T) this;
    }

    public Boolean getReturnBinary() {
        return returnBinary;
    }

    @SuppressWarnings("unchecked")
    public T setReturnBinary(Boolean returnBinary) {
        this.returnBinary = returnBinary;
        return (T) this;
    }

    public Boolean getDisableAutocorrection() {
        return disableAutocorrection;
    }

    @SuppressWarnings("unchecked")
    public T setDisableAutocorrection(Boolean disableAutocorrection) {
        this.disableAutocorrection = disableAutocorrection;
        return (T) this;
    }

    public List<SelectedRefinement> getRefinements() {
        return refinements;
    }

    @SuppressWarnings("unchecked")
    public T setRefinements(List<SelectedRefinement> refinements) {
        this.refinements = refinements;
        return (T) this;
    }

    public String getRefinementQuery() {
        return refinementQuery;
    }

    @SuppressWarnings("unchecked")
    public T setRefinementQuery(String refinementQuery) {
        this.refinementQuery = refinementQuery;
        return (T) this;
    }

    public RestrictNavigation getRestrictNavigation() {
        return restrictNavigation;
    }

    @SuppressWarnings("unchecked")
    public T setRestrictNavigation(RestrictNavigation restrictNavigation) {
        this.restrictNavigation = restrictNavigation;
        return (T) this;
    }

}
