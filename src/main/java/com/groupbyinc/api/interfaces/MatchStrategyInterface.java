package com.groupbyinc.api.interfaces;

import java.util.List;

/**
 * MatchStrategy
 *
 * @author Ben Teichman
 */
public interface MatchStrategyInterface {

  List<? extends PartialMatchRuleInterface> getRules();

  void addRule(PartialMatchRuleInterface rule);
}
