package com.groupbyinc.api.model;

import java.util.Map;

/**
 * Created by groupby on 15/10/14.
 */
public class Record extends RecordBase {
    private static final long serialVersionUID = 1L;

    private Map<String, Map<String, Integer>> refinementMatches;

    public Map<String, Map<String, Integer>> getRefinementMatches() {
        return refinementMatches;
    }

    public void setRefinementMatches(Map<String, Map<String, Integer>> pRefinementMatches) {
        refinementMatches = pRefinementMatches;
    }
}
