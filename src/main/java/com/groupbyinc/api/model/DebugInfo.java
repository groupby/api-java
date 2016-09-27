package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by groupby on 08/04/15.
 * @internal
 */
public class DebugInfo {

  @JsonProperty protected Map<String, Object> rawRequest;
  @JsonProperty protected Map<String, Object> rawResponse;
  @JsonProperty protected Map<String, Object> rawAggregationsRequest;
  @JsonProperty protected Map<String, Object> rawAggregationsResponse;

  public static DebugInfo createDebugInfo(
      Map<String, Object> rawRequest, Map<String, Object> rawResponse, Map<String, Object> rawAggregationsRequest, Map<String, Object> rawAggregationsResponse) {
    DebugInfo debugInfo = new DebugInfo();
    debugInfo.rawRequest = rawRequest;
    debugInfo.rawResponse = rawResponse;
    debugInfo.rawAggregationsRequest = rawAggregationsRequest;
    debugInfo.rawAggregationsResponse = rawAggregationsResponse;
    return debugInfo;
  }
}
