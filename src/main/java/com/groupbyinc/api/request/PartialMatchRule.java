package com.groupbyinc.api.request;

import com.groupbyinc.common.jackson.annotation.JsonIgnore;

/**
 * @author osman
 * @internal
 */
public class PartialMatchRule {
    private Integer terms;
    private Integer termsGreaterThan;
    private Integer mustMatch;
    private Boolean percentage = false;

    public Integer getTerms() {
        return terms;
    }

    public PartialMatchRule setTerms(Integer terms) {
        this.terms = terms;
        return this;
    }

    public Integer getTermsGreaterThan() {
        return termsGreaterThan;
    }

    public PartialMatchRule setTermsGreaterThan(Integer termsGreaterThan) {
        this.termsGreaterThan = termsGreaterThan;
        return this;
    }

    public Integer getMustMatch() {
        return mustMatch;
    }

    public PartialMatchRule setMustMatch(Integer mustMatch) {
        this.mustMatch = mustMatch;
        return this;
    }

    public Boolean getPercentage() {
        return percentage;
    }

    public PartialMatchRule setPercentage(Boolean percentage) {
        this.percentage = percentage;
        return this;
    }

    @JsonIgnore
    public Integer getEffectiveGreaterThan() {
        if (terms != null) {
            return terms - 1;
        } else if (termsGreaterThan != null) {
            return termsGreaterThan;
        } else {
            return null;
        }
    }
}
