package com.groupbyinc.api.model;

import com.groupbyinc.common.apache.commons.collections4.CollectionUtils;
import com.groupbyinc.common.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author osman
 * @internal
 */
public class MatchStrategy {

  private List<PartialMatchRule> rules = new ArrayList<PartialMatchRule>();

  public List<PartialMatchRule> getRules() {
    return rules;
  }

  @JsonIgnore
  public MatchStrategy setRules(PartialMatchRule... rules) {
    CollectionUtils.addAll(this.rules, rules);
    return this;
  }

  public MatchStrategy setRules(List<PartialMatchRule> rules) {
    this.rules = rules;
    return this;
  }
}
