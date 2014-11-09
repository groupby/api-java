package com.groupbyinc.api.tags;

import com.groupbyinc.api.Bridge;
import com.groupbyinc.api.BridgeFactory;
import com.groupbyinc.api.model.Results;

public class ResultsTag extends ResultsTagBase<Results, Bridge> {
    private static final long serialVersionUID = 1L;

    public ResultsTag() {
        super(new BridgeFactory());
    }
}
