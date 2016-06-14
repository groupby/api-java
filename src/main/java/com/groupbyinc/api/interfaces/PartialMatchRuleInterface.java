package com.groupbyinc.api.interfaces;

/**
 * PartialMatchRule
 *
 * @author Ben Teichman
 */
public interface PartialMatchRuleInterface {

  Integer getTerms();

  Integer getTermsGreaterThan();

  Integer getMustMatch();

  Boolean getPercentage();
}
