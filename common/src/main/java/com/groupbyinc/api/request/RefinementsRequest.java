package com.groupbyinc.api.request;

/**
 * Request object for the api to send search service requests
 *
 * @author lonell
 * @internal
 */
public class RefinementsRequest<R extends AbstractRequest<R>> {
    private R originalQuery;
    private String navigationName;

    public String getNavigationName() {
        return navigationName;
    }

    public RefinementsRequest<R> setNavigationName(String navigationName) {
        this.navigationName = navigationName;
        return this;
    }

    public R getOriginalQuery() {
        return originalQuery;
    }

    public RefinementsRequest<R> setOriginalQuery(R originalQuery) {
        this.originalQuery = originalQuery;
        return this;
    }
}
