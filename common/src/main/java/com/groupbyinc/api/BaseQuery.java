package com.groupbyinc.api;

import com.groupbyinc.api.request.BaseRequest;
import com.groupbyinc.api.request.RefinementsRequest;

/**
 * @internal
 */
public class BaseQuery extends AbstractQuery<BaseRequest, BaseQuery> {
    @Override
    protected BaseRequest generateRequest() {
        return new BaseRequest();
    }

    @Override
    protected RefinementsRequest<BaseRequest> populateRefinementRequest() {
        return new RefinementsRequest<BaseRequest>();
    }
}
