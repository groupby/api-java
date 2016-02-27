package com.groupbyinc.api.request;

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

  public MatchStrategy setRules(List<PartialMatchRule> rules) {
    this.rules = rules;
    return this;
  }
}
