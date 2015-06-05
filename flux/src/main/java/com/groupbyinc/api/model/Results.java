package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonProperty;

public class Results extends AbstractResults<Record, Results> {
    @JsonProperty
    DebugInfo debugInfo;
}
