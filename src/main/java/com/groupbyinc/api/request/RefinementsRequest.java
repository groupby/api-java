package com.groupbyinc.api.request;

/**
 * Request object for the api to send search service requests
 *
 * @author lonell
 * @internal
 */
public class RefinementsRequest {
    private Request originalQuery;
    private String navigationName;

    public String getNavigationName() {
        return navigationName;
    }

    public RefinementsRequest setNavigationName(String navigationName) {
        this.navigationName = navigationName;
        return this;
    }

    public Request getOriginalQuery() {
        return originalQuery;
    }

    public RefinementsRequest setOriginalQuery(Request originalQuery) {
        this.originalQuery = originalQuery;
        return this;
    }
}
