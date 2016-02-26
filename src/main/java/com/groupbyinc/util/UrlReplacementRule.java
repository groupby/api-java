package com.groupbyinc.util;

import com.groupbyinc.common.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.groupbyinc.util.UrlReplacement.OperationType;

public class UrlReplacementRule {

  public static final String INSERT_INDICATOR = "i";
  public static final String REPLACEMENT_DELIMITER = "-";
  private String target;
  private String replacement;
  private String navigationName;

  public UrlReplacementRule(char target, Character replacement, String navigationName) {
    this.target = Character.toString(target);
    if (replacement == null) {
      this.replacement = "";
    } else {
      this.replacement = replacement.toString();
    }
    this.navigationName = navigationName;
  }

  public void apply(
      StringBuilder url, int indexOffSet, String navigationName, List<UrlReplacement> replacementBuilder) {
    if (url != null && (this.navigationName == null || navigationName.equals(this.navigationName))) {
      int index = url.indexOf(target);
      while (index != -1) {
        OperationType type = OperationType.Swap;
        if (StringUtils.isEmpty(replacement)) {
          replacement = "";
          type = OperationType.Insert;
          url.deleteCharAt(index);
        } else {
          url.replace(index, index + replacement.length(), replacement);
        }
        UrlReplacement replacement = new UrlReplacement(index + indexOffSet, target, type);
        replacementBuilder.add(replacement);
        index = url.indexOf(target, index);
      }
    }
  }
}
