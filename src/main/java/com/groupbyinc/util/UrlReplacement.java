package com.groupbyinc.util;

import com.groupbyinc.api.parser.ParserException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.groupbyinc.util.UrlReplacementRule.REPLACEMENT_DELIMITER;

/**
 * Created by groupby on 9/13/14.
 */
public class UrlReplacement {
    private int index;
    private String replacement;
    private OperationType type;

    public enum OperationType {
        Insert,
        Swap;
    }

    public UrlReplacement(int pIndex, String pReplacement, OperationType pType) {
        index = pIndex;
        replacement = pReplacement;
        type = pType;
    }

    public static List<UrlReplacement> parseQueryString(String pQuery) throws ParserException {
        int delimiterIndex = 0;
        List<UrlReplacement> replacements = new ArrayList<UrlReplacement>();
        if (StringUtils.isBlank(pQuery)) {
            return replacements;
        }
        StringBuilder queryString = new StringBuilder(pQuery);
        while (delimiterIndex >= 0) {
            int pairSeparator = queryString.indexOf(REPLACEMENT_DELIMITER);
            if (pairSeparator < 0) {
                //separators don't match up properly
                throw new ParserException("Replacement Query Delimiters did not match up");
            }
            delimiterIndex = queryString.indexOf(REPLACEMENT_DELIMITER, pairSeparator + 2);
            if (delimiterIndex < 0) {
                break;
            }
            replacements.add(UrlReplacement.fromString(queryString.substring(0, delimiterIndex)));
            queryString.delete(0, delimiterIndex);
            if (Character.toString(queryString.charAt(0)).equals(REPLACEMENT_DELIMITER)) {
                queryString.deleteCharAt(0);
            }
        }
        pQuery = queryString.toString();
        if (StringUtils.isNotBlank(pQuery)) {
            replacements.add(UrlReplacement.fromString(pQuery));
        }
        Collections.reverse(replacements);
        return replacements;
    }

    public void apply(StringBuilder pPathSegment, int pOffSet) {
        int relativeIndex = index - pOffSet;
        if (relativeIndex < 0 || (relativeIndex >= pPathSegment.length() && type.equals(OperationType.Swap)) ||
                (relativeIndex > pPathSegment.length() && type.equals(OperationType.Insert))) {
            return;
        }
        if (type.equals(OperationType.Insert)) {
            pPathSegment.insert(relativeIndex, replacement);
        } else if (type.equals(OperationType.Swap)) {
            pPathSegment.replace(relativeIndex, relativeIndex + replacement.length(), replacement);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type == OperationType.Insert) {
            sb.append('i');
        }
        sb.append(index);
        sb.append(REPLACEMENT_DELIMITER);
        sb.append(replacement);
        return sb.toString();
    }

    public static UrlReplacement fromString(String pUrlReplacementString) throws ParserException {
        OperationType operationType = OperationType.Swap;
        int delimiterIndex = pUrlReplacementString.indexOf(REPLACEMENT_DELIMITER);
        if (delimiterIndex < 0) {
            throw new ParserException("Argument did not match expected format: " + pUrlReplacementString);
        }
        String replacementValue = pUrlReplacementString.substring(delimiterIndex + 1);
        String indexString = pUrlReplacementString.substring(0, delimiterIndex);
        if (indexString.startsWith("i")) {
            operationType = OperationType.Insert;
            indexString = indexString.substring(1);
        }

        try {
            int indexValue = Integer.parseInt(indexString);
            return new UrlReplacement(indexValue, replacementValue, operationType);

        } catch (NumberFormatException e) {
            throw new ParserException("Invalid index:" + indexString);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UrlReplacement)) {
            return false;
        }
        UrlReplacement other = (UrlReplacement) o;
        if (index != other.index || type != other.type) {
            return false;
        }
        if (replacement == null) {
            return replacement == other.replacement;
        }
        return replacement.equals(other.replacement);
    }

    public static String buildQueryString(List<UrlReplacement> pReplacements) {
        StringBuilder sb = new StringBuilder();
        for (UrlReplacement replacement : pReplacements) {
            if (sb.length() != 0) {
                sb.append(REPLACEMENT_DELIMITER);
            }
            sb.append(replacement.toString());
        }
        return sb.toString();
    }

}
