package com.groupbyinc.api.model;

import java.util.List;

public class Results extends AbstractResults<Record, Results> {
    private List<KeyMatch> keyMatches;
    private List<String> synonyms;

    /**
     * <code>
     * The GSA supports the concept of key matches which links a search term to a URL.
     * The resulting object represents the response from the GSA.
     * </code>
     *
     * @return A list of KeyMatch objects
     */
    public List<KeyMatch> getKeyMatches() {
        return keyMatches;
    }

    /**
     * @param keyMatches Set key matches
     * @return
     */
    public Results setKeyMatches(List<KeyMatch> keyMatches) {
        this.keyMatches = keyMatches;
        return this;
    }

    /**
     * @return A list of synonyms resulting from the use of the Related Queries
     * section in the GSA.
     */
    public List<String> getSynonyms() {
        return synonyms;
    }

    /**
     * @param synonyms Set a list of synonyms
     * @return
     */
    public Results setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
        return this;
    }
}
