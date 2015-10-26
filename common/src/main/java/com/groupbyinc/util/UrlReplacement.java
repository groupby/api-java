package com.groupbyinc.util;

import com.groupbyinc.api.parser.ParserException;
import com.groupbyinc.common.util.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.groupbyinc.util.UrlReplacementRule.INSERT_INDICATOR;
import static com.groupbyinc.util.UrlReplacementRule.REPLACEMENT_DELIMITER;

public class UrlReplacement {

    private int index;
    private String replacement;
    private OperationType type;

    public enum OperationType {
        Insert,
        Swap
    }

    public UrlReplacement(int index, String replacement, OperationType type) {
        this.index = index;
        this.replacement = replacement;
        this.type = type;
    }

    public static List<UrlReplacement> parseQueryString(String query) throws ParserException {
        int delimiterIndex = 0;
        List<UrlReplacement> replacements = new ArrayList<UrlReplacement>();
        if (StringUtils.isBlank(query)) {
            return replacements;
        }
        StringBuilder queryString = new StringBuilder(query);
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
        query = queryString.toString();
        if (StringUtils.isNotBlank(query)) {
            replacements.add(UrlReplacement.fromString(query));
        }
        Collections.reverse(replacements);
        return replacements;
    }

    public void apply(StringBuilder pathSegment, int offSet) {
        int relativeIndex = index - offSet;
        if (relativeIndex < 0 || (relativeIndex >= pathSegment.length() && type.equals(OperationType.Swap)) ||
            (relativeIndex > pathSegment.length() && type.equals(OperationType.Insert))) {
            return;
        }
        if (type.equals(OperationType.Insert)) {
            pathSegment.insert(relativeIndex, replacement);
        } else if (type.equals(OperationType.Swap)) {
            pathSegment.replace(relativeIndex, relativeIndex + replacement.length(), replacement);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type == OperationType.Insert) {
            sb.append(INSERT_INDICATOR);
        }
        sb.append(index);
        sb.append(REPLACEMENT_DELIMITER);
        sb.append(replacement);
        return sb.toString();
    }

    public static UrlReplacement fromString(String urlReplacementString) throws ParserException {
        OperationType operationType = OperationType.Swap;
        int delimiterIndex = urlReplacementString.indexOf(REPLACEMENT_DELIMITER);
        if (delimiterIndex < 0) {
            throw new ParserException("Argument did not match expected format: " + urlReplacementString);
        }
        String replacementValue = urlReplacementString.substring(delimiterIndex + 1);
        String indexString = urlReplacementString.substring(0, delimiterIndex);
        if (indexString.startsWith(INSERT_INDICATOR)) {
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
    public int hashCode() {
        int result = index;
        result = 31 * result + (replacement != null ? replacement.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UrlReplacement)) {
            return false;
        }
        UrlReplacement other = (UrlReplacement) o;
        if (index != other.index || type != other.type) {
            return false;
        }
        if (replacement == null) {
            return other.replacement == null;
        }
        return replacement.equals(other.replacement);
    }

    public static String buildQueryString(List<UrlReplacement> replacements) {
        StringBuilder sb = new StringBuilder();
        for (UrlReplacement replacement : replacements) {
            if (sb.length() != 0) {
                sb.append(REPLACEMENT_DELIMITER);
            }
            sb.append(replacement.toString());
        }
        return sb.toString();
    }

}
