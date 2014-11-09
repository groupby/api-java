package com.groupbyinc.api.model;

import java.util.List;

public class Results extends ResultsBase<Record> {
    private static final long serialVersionUID = 1L;

    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String pArea) {
        area = pArea;
    }

    /**
     * @deprecated v0.1.5
     */
    @Deprecated
    public List<KeyMatch> getKeyMatches() {
        return null;
    }

    /**
     * @deprecated v0.1.5
     */
    @Deprecated
    public Results setKeyMatches(List<KeyMatch> pKeyMatches) {
        return this;
    }

    /**
     * @deprecated v0.1.5
     */
    @Deprecated
    public List<String> getSynonyms() {
        return null;
    }

    /**
     * @deprecated v0.1.5
     */
    @Deprecated
    public Results setSynonyms(List<String> pSynonyms) {
        return this;
    }
}
