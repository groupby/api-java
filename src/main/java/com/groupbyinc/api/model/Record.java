package com.groupbyinc.api.model;

import java.util.List;

public class Record extends AbstractRecord<Record> {
    private List<RefinementMatch> refinementMatches;

    /**
     * @internal
     * @return
     */
    public List<RefinementMatch> getRefinementMatches() {
        return refinementMatches;
    }

    /**
     * @internal
     * @param refinementMatches
     * @return
     */
    public Record setRefinementMatches(List<RefinementMatch> refinementMatches) {
        this.refinementMatches = refinementMatches;
        return this;
    }
}
