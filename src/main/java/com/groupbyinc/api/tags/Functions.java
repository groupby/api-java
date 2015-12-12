package com.groupbyinc.api.tags;

import com.groupbyinc.api.model.AbstractResults;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.common.apache.commons.codec.digest.DigestUtils;
import com.groupbyinc.common.apache.commons.collections4.CollectionUtils;
import com.groupbyinc.common.apache.commons.lang3.StringEscapeUtils;
import com.groupbyinc.common.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Functions {

    private static final SimpleDateFormat ISO_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final String[] acronyms = {"Api", "Xml", "Gsa", "Asp", "Jstl", "Net", "Php", "Sayt", "Db", "Faq", "Sdk", "Seo"};

    private Functions() {
        // hide utility constructor
    }

    public static boolean contains(Collection<?> haystack, Object needle) {
        return !(haystack == null || needle == null) && haystack.contains(needle);
    }

    public static String md5(String value) {
        if (value == null) {
            return StringUtils.EMPTY;
        }
        return DigestUtils.md5Hex(value);
    }

    public static String replaceAll(String haystack, String needle, String newNeedle) {
        if (haystack == null) {
            return StringUtils.EMPTY;
        }
        if (needle == null) {
            return haystack;
        }
        if (newNeedle == null) {
            return haystack;
        }
        return haystack.replaceAll(needle, newNeedle);
    }

    public static String escapeJs(String value) {
        return StringEscapeUtils.escapeEcmaScript(value);
    }

    public static String epochToIso(String value) {
        if (value == null || value.trim().length() == 0) {
            return StringUtils.EMPTY;
        }
        return ISO_DATE.format(new Date(Long.parseLong(value)));
    }

    public static String uncamel(String value) {
        String v = reverseAcronyms(value);
        for (int i = 0; i < 10; i++) {
            v = v.replaceAll("([a-zA-Z0-9])([A-Z0-9])([a-z0-9]*)", "$1 $2$3");
        }
        return StringUtils.capitalize(replaceAcronyms(v));
    }

    private static String replaceAcronyms(String value) {
        String v = StringUtils.capitalize(value);
        for (String acr : acronyms) {
            v = v.replaceAll(acr + "(\\b|[A-Z])", acr.toUpperCase() + "$1");
        }
        return v;
    }

    private static String reverseAcronyms(String value) {
        String v = value;
        for (String acr : acronyms) {
            v = v.replaceAll(acr.toUpperCase(), acr);
        }
        return v;
    }

    public static <T> List<T> reverse(List<T> list) {
        List<T> copy = new ArrayList<T>(list.size());
        for (T t : list) {
            copy.add(t);
        }
        Collections.reverse(copy);
        return copy;
    }

    public static boolean isRefinementSelected(AbstractResults<?, ?> results, String navigationName, String value) {
        if (StringUtils.isEmpty(navigationName) || StringUtils.isEmpty(value)) {
            return false;
        } else if (CollectionUtils.isEmpty(results.getSelectedNavigation())) {
            return false;
        }

        for (Navigation n : results.getSelectedNavigation()) {
            if (navigationName.equals(n.getName())) {
                if (n.isRange()) {
                    return false;
                }
                for (Refinement r : n.getRefinements()) {
                    RefinementValue rv = (RefinementValue) r;
                    if (value.equals(rv.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
