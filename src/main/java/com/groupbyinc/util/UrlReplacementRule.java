package com.groupbyinc.util;

import com.groupbyinc.api.model.Refinement;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.groupbyinc.util.UrlReplacement.OperationType;

/**
 * Created by groupby on 9/12/14.
 */
public class UrlReplacementRule {

    private String target;
    private String replacement;
    private String refinementName;
    public static final String INSERT_INDICATOR = "i";
    public static final String REPLACEMENT_DELIMITER = "-";

    public UrlReplacementRule(char pTarget, Character pReplacement, String pRefinementName) {
        target = Character.toString(pTarget);
        if (pReplacement == null) {
            replacement = "";
        } else {
            replacement = pReplacement.toString();
        }
        refinementName = pRefinementName;
    }

    public void apply(StringBuilder pUrl, int indexOffSet, Refinement currentRefinement,
                      List<UrlReplacement> replacementBuilder) {

        if (pUrl == null || (refinementName != null && !currentRefinement.getNavigationName().equals(refinementName))) {
            return;
        }
        int index = pUrl.indexOf(target);
        while (index != -1) {
            OperationType type = OperationType.Swap;
            if (StringUtils.isEmpty(replacement)) {
                replacement = "";
                type = OperationType.Insert;
                pUrl.deleteCharAt(index);

            } else {
                pUrl.replace(index, index + replacement.length(), replacement);
            }
            UrlReplacement replacement = new UrlReplacement(index + indexOffSet, target, type);
            replacementBuilder.add(replacement);
            index = pUrl.indexOf(target, index);
        }
    }

}
