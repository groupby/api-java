package com.groupbyinc.api.model;

import com.groupbyinc.api.interfaces.PartialMatchRuleInterface;

/**
 * @internal
 */
public class PartialMatchRule implements PartialMatchRuleInterface {

  private Integer terms;
  private Integer termsGreaterThan;
  private Integer mustMatch;
  private Boolean percentage;

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
}
