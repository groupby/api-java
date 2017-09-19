package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.Mappers;
import com.groupbyinc.common.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by groupby on 08/04/15.
 * @internal
 */
public class DebugInfo {

  private static final transient Logger LOG = LoggerFactory.getLogger(DebugInfo.class);
  @JsonProperty protected Map<String, Object> rawRequest;
  @JsonProperty protected Map<String, Object> rawResponse;

  public static DebugInfo createDebugInfo(
      Map<String, Object> rawRequest, Map<String, Object> rawResponse) {
    DebugInfo debugInfo = new DebugInfo();
    debugInfo.rawRequest = rawRequest;
    debugInfo.rawResponse = rawResponse;
    LOG.error("JKDLSFJDLSF: \n{}\n{}", Mappers.writeValueAsString(rawRequest, true), Mappers.writeValueAsString(rawResponse, true));

    return debugInfo;
  }
}
