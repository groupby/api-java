package com.groupbyinc.api.request;

import java.util.ArrayList;
import java.util.List;

/**
 * @internal
 */
public class Navigation {

  private String name;
  private List<String> pinnedRefinements = new ArrayList<String>();

  public String getName() {
    return name;
  }

  public Navigation setName(String name) {
    this.name = name;
    return this;
  }

  public List<String> getPinnedRefinements() {
    return pinnedRefinements;
  }

  public Navigation setPinnedRefinements(List<String> pinnedRefinements) {
    this.pinnedRefinements = pinnedRefinements;
    return this;
  }
}
