package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.util.apache.commons.collections4.CollectionUtils;

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

    @JsonIgnore
    public MatchStrategy setRules(PartialMatchRule... rules) {
        CollectionUtils.addAll(this.rules, rules);
        return this;
    }

}
