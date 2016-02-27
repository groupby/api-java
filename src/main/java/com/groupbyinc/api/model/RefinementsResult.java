package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonProperty;

public class RefinementsResult {

  protected String errors;
  protected Navigation navigation;
  @JsonProperty DebugInfo debugInfo;

  public String getErrors() {
    return errors;
  }

  public RefinementsResult setErrors(String errors) {
    this.errors = errors;
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
