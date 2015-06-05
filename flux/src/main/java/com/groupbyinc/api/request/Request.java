package com.groupbyinc.api.request;

import com.groupbyinc.common.jackson.annotation.JsonInclude;

/**
 * Request object for the api to send search service requests
 *
 * @author lonell
 * @internal
 */
public class Request extends AbstractRequest<Request> {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean wildcardSearchEnabled = false;

    private MatchStrategy matchStrategy;

    public Boolean isWildcardSearchEnabled() {
        return wildcardSearchEnabled;
    }

    public Request setWildcardSearchEnabled(Boolean wildcardSearchEnabled) {
        this.wildcardSearchEnabled = wildcardSearchEnabled;
        return this;
    }

    public MatchStrategy getMatchStrategy() {
        return matchStrategy;
    }

    public Request setMatchStrategy(MatchStrategy matchStrategy) {
        this.matchStrategy = matchStrategy;
        return this;
    }
}
