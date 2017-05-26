package com.groupbyinc.api.model;

import com.groupbyinc.api.request.RefinementsRequest;
import com.groupbyinc.common.jackson.annotation.JsonProperty;

public class RefinementsResult {

  private RefinementsRequest originalRequest;
  protected Navigation navigation;
  @JsonProperty protected DebugInfo debugInfo;
  private String errors;

  public String getErrors() {
    return errors;
  }

  public RefinementsResult setErrors(String errors) {
    this.errors = errors;
    return this;
  }

  public RefinementsRequest getOriginalRequest() {
    return originalRequest;
  }

  public RefinementsResult setOriginalRequest(RefinementsRequest originalRequest) {
    this.originalRequest = originalRequest;
    return this;
  }

  public Navigation getNavigation() {
    return navigation;
  }

  public RefinementsResult setNavigation(Navigation navigation) {
    this.navigation = navigation;
    return this;
  }
}
