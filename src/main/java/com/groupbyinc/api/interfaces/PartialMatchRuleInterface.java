package com.groupbyinc.api.interfaces;

public interface PartialMatchRuleInterface {

  Integer getTerms();

  Integer getTermsGreaterThan();

  Integer getMustMatch();

  Boolean getPercentage();
}
