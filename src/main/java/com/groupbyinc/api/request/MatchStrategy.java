package com.groupbyinc.api.request;

import com.groupbyinc.api.interfaces.MatchStrategyInterface;
import com.groupbyinc.api.interfaces.PartialMatchRuleInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @internal
 */
public class MatchStrategy implements MatchStrategyInterface {

  private List<PartialMatchRule> rules = new ArrayList<PartialMatchRule>();

  public List<PartialMatchRule> getRules() {
    return rules;
  }

  public MatchStrategy setRules(List<PartialMatchRule> rules) {
    this.rules = rules;
    return this;
  }

  @Override
  public void addRule(PartialMatchRuleInterface rule) {
    rules.add(new PartialMatchRule().setTerms(rule.getTerms()).setTermsGreaterThan(rule.getTermsGreaterThan()).setMustMatch(rule.getMustMatch()).setPercentage(rule.getPercentage()));
  }
}
