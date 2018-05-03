package com.groupbyinc.api.interfaces;

import java.util.List;

public interface MatchStrategyInterface {

  List<? extends PartialMatchRuleInterface> getRules();

  void addRule(PartialMatchRuleInterface rule);
}
