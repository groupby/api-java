package com.groupbyinc.api.model;

import com.groupbyinc.api.interfaces.MatchStrategyInterface;
import com.groupbyinc.api.interfaces.PartialMatchRuleInterface;
import com.groupbyinc.common.apache.commons.collections4.CollectionUtils;
import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonProperty;
import com.groupbyinc.common.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;

/**
 * @internal
 */
public class MatchStrategy implements MatchStrategyInterface {

  @JsonProperty private String name;
  @JsonProperty private List<PartialMatchRule> rules = new ArrayList<PartialMatchRule>();

  public List<PartialMatchRule> getRules() {
    return rules;
  }

  @JsonIgnore
  public MatchStrategy setRules(PartialMatchRule... rules) {
    CollectionUtils.addAll(this.rules, rules);
    return this;
  }

  @JsonSetter
  public MatchStrategy setRules(List<PartialMatchRule> rules) {
    this.rules = rules;
    return this;
  }

  @Override
  @JsonIgnore
  public void addRule(PartialMatchRuleInterface rule) {
    rules.add(new PartialMatchRule().setTerms(rule.getTerms()).setTermsGreaterThan(rule.getTermsGreaterThan()).setMustMatch(rule.getMustMatch()).setPercentage(rule.getPercentage()));
  }

  public String getName() {
    return name;
  }

  public MatchStrategy setName(String name) {
    this.name = name;
    return this;
  }
}
