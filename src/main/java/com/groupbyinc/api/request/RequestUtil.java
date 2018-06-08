package com.groupbyinc.api.request;

import com.groupbyinc.api.model.CustomUrlParam;
import com.groupbyinc.common.apache.commons.collections4.CollectionUtils;
import com.groupbyinc.common.apache.commons.lang3.ObjectUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RequestUtil {

  private static final Comparator<String> STRING_COMPARATOR = new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
      return ObjectUtils.compare(o1, o2);
    }
  };

  private static final Comparator<CustomUrlParam> CUSTOM_URL_PARAM_COMPARATOR = new Comparator<CustomUrlParam>() {
    @Override
    public int compare(CustomUrlParam o1, CustomUrlParam o2) {
      int compareTo = ObjectUtils.compare(o1.getKey(), o2.getKey());
      if (compareTo == 0) {
        compareTo = ObjectUtils.compare(o1.getValue(), o2.getValue());
      }
      return compareTo;
    }
  };

  private static final Comparator<SelectedRefinement> SELECTED_REFINEMENT_COMPARATOR = new Comparator<SelectedRefinement>() {
    @Override
    public int compare(SelectedRefinement o1, SelectedRefinement o2) {
      return ObjectUtils.compare(o1.toTildeString(), o2.toTildeString());
    }
  };

  private static final Comparator<Bias> BIAS_COMPARATOR = new Comparator<Bias>() {
    @Override
    public int compare(Bias o1, Bias o2) {
      int compareTo = ObjectUtils.compare(o1.getName(), o2.getName());
      if (compareTo == 0) {
        compareTo = ObjectUtils.compare(o1.getContent(), o2.getContent());
      }
      if (compareTo == 0) {
        compareTo = ObjectUtils.compare(o1.getStrength(), o2.getStrength());
      }
      return compareTo;
    }
  };

  private static final Comparator<NumericBoost> NUMERIC_BOOST_COMPARATOR = new Comparator<NumericBoost>() {
    @Override
    public int compare(NumericBoost o1, NumericBoost o2) {
      int compareTo = ObjectUtils.compare(o1.getName(), o2.getName());
      if (compareTo == 0) {
        compareTo = ObjectUtils.compare(o1.getStrength(), o2.getStrength());
      }
      if (compareTo == 0) {
        compareTo = ObjectUtils.compare(o1.isInverted(), o2.isInverted());
      }
      return compareTo;
    }
  };

  public static Request normalizeRequest(Request request) {
    if (request == null) {
      return null;
    }
    sort(request.getCustomUrlParams(), CUSTOM_URL_PARAM_COMPARATOR);

    sort(request.getRefinements(), SELECTED_REFINEMENT_COMPARATOR);

    Biasing biasing = request.getBiasing();
    if (biasing != null) {
      sort(biasing.getBiases(), BIAS_COMPARATOR);
      sort(biasing.getNumericBoosts(), NUMERIC_BOOST_COMPARATOR);
      sort(biasing.getRestrictToIds(), STRING_COMPARATOR);
    }

    sort(request.getIncludedNavigations(), STRING_COMPARATOR);
    sort(request.getExcludedNavigations(), STRING_COMPARATOR);
    sort(request.getOrFields(), STRING_COMPARATOR);
    sort(request.getFields(), STRING_COMPARATOR);
    return request;
  }

  public static Request normalizeCacheKey(Request request) {
    if (request == null) {
      return null;
    }
    request.setVisitorId("cache").setSessionId("cache").setClientKey("cache");
    return normalizeRequest(request);
  }

  private static <T> void sort(List<T> list, Comparator<T> comparator) {
    if (CollectionUtils.isNotEmpty(list)) {
      Collections.sort(list, comparator);
    }
  }
}
