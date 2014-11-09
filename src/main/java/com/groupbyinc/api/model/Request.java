package com.groupbyinc.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by groupby on 9/2/14.
 */
public class Request {
    @JsonProperty("l")
    private String collection;
    @JsonProperty("e")
    private String area;
    @JsonProperty("q")
    private String searchString;
    @JsonProperty("sk")
    private String skip;
    @JsonProperty("p")
    private String pageSize = "10";
    @JsonProperty("o")
    private String customUrlParams;
    @JsonProperty("f")
    private String fields = "";
    @JsonProperty("uf")
    private String unionableFields;
    @JsonProperty("rf")
    private String requiredFields;
    @JsonProperty("b")
    private String biasingProfile;
    @JsonProperty("s")
    private String sort;
    @JsonProperty("ac")
    private String accurateCounts;
    @JsonProperty("lr")
    private String languageRestrict;
    @JsonProperty("hl")
    private String interfaceLanguage;
    @JsonProperty("pr")
    private String pruneRefinements;
    @JsonProperty("rn")
    private String restrictNavigation;
    @JsonProperty("rb")
    private String returnBinary;
    @JsonProperty("c")
    private String clientKey;
    @JsonProperty("rs")
    private String refinementSearch;
    @JsonProperty("r")
    private String refinements;
    @JsonProperty("pf")
    private String partialFields;


    public String getCollection() {
        return collection;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String pClientKey) {
        clientKey = pClientKey;
    }

    public void setCollection(String pCollection) {
        collection = pCollection;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String pArea) {
        area = pArea;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String pSearchString) {
        searchString = pSearchString;
    }

    public String getSkip() {
        return skip;
    }

    public void setSkip(String pSkip) {
        skip = pSkip;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pPageSize) {
        pageSize = pPageSize;
    }

    public String getCustomUrlParams() {
        return customUrlParams;
    }

    public void setCustomUrlParams(String pCustomUrlParams) {
        customUrlParams = pCustomUrlParams;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String pFields) {
        fields = pFields;
    }

    public String getUnionableFields() {
        return unionableFields;
    }

    public void setUnionableFields(String pUnionableFields) {
        unionableFields = pUnionableFields;
    }

    public String getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(String pRequiredFields) {
        requiredFields = pRequiredFields;
    }

    public String getBiasingProfile() {
        return biasingProfile;
    }

    public void setBiasingProfile(String pBiasingProfile) {
        biasingProfile = pBiasingProfile;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String pSort) {
        sort = pSort;
    }

    public String getAccurateCounts() {
        return accurateCounts;
    }

    public void setAccurateCounts(String pAccurateCounts) {
        accurateCounts = pAccurateCounts;
    }

    public String getLanguageRestrict() {
        return languageRestrict;
    }

    public void setLanguageRestrict(String pLanguageRestrict) {
        languageRestrict = pLanguageRestrict;
    }

    public String getInterfaceLanguage() {
        return interfaceLanguage;
    }

    public void setInterfaceLanguage(String pInterfaceLanguage) {
        interfaceLanguage = pInterfaceLanguage;
    }

    public String getPruneRefinements() {
        return pruneRefinements;
    }

    public void setPruneRefinements(String pPruneRefinements) {
        pruneRefinements = pPruneRefinements;
    }

    public String getRestrictNavigation() {
        return restrictNavigation;
    }

    public void setRestrictNavigation(String pRestrictNavigation) {
        restrictNavigation = pRestrictNavigation;
    }

    public String getReturnBinary() {
        return returnBinary;
    }

    public void setReturnBinary(String pReturnBinary) {
        returnBinary = pReturnBinary;
    }

    public String getRefinementSearch() {
        return refinementSearch;
    }

    public void setRefinementSearch(String pRefinementSearch) {
        refinementSearch = pRefinementSearch;
    }

    public String getRefinements() {
        return refinements;
    }

    public void setRefinements(String pRefinements) {
        refinements = pRefinements;
    }

    public String getPartialFields() {
        return partialFields;
    }

    public void setPartialFields(String pPartialFields) {
        partialFields = pPartialFields;
    }
}
