package com.groupbyinc.api.tags;

import com.groupbyinc.util.StringEscapeUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Functions {
    private static final SimpleDateFormat ISO_DATE = new SimpleDateFormat(
            "yyyy-MM-dd");
    private static final String[] acronyms = {
            "Api", "Xml", "Gsa", "Asp", "Jstl", "Net", "Php", "Sayt", "Db", "Faq", "Sdk", "Seo"
    };

    private Functions() {
        // hide utility constructor
    }

    public static boolean contains(Collection<?> pHaystack, Object pNeedle) {
        if (pHaystack == null || pNeedle == null) {
            return false;
        }
        return pHaystack.contains(pNeedle);
    }

    public static String md5(String pValue) {
        if (pValue == null) {
            return "";
        }
        return DigestUtils.md5Hex(pValue);
    }

    public static String replaceAll(String pHaystack, String pNeedle, String pNewNeedle) {
        if (pHaystack == null) {
            return "";
        }
        if (pNeedle == null) {
            return pHaystack;
        }
        if (pNewNeedle == null) {
            return pHaystack;
        }
        return pHaystack.replaceAll(pNeedle, pNewNeedle);
    }

    public static String escapeJs(String value) {
        return StringEscapeUtils.escapeJavaScript(value);
    }

    public static String epochToIso(String pValue) {
        if (pValue == null || pValue.trim().length() == 0) {
            return "";
        }
        return ISO_DATE.format(new Date(new Long(pValue)));
    }

    public static String uncamel(String pValue) {
        String value = reverseAcronyms(pValue);
        for (int i = 0; i < 10; i++) {
            value = value.replaceAll(
                    "([a-zA-Z0-9])([A-Z0-9])([a-z0-9]*)", "$1 $2$3");
        }
        return StringUtils.capitalize(replaceAcronyms(value));
    }

    private static String replaceAcronyms(String pValue) {
        String value = StringUtils.capitalize(pValue);
        for (String acr : acronyms) {
            value = value.replaceAll(
                    acr + "(\\b|[A-Z])", acr.toUpperCase() + "$1");
        }
        return value;
    }

    private static String reverseAcronyms(String pValue) {
        String value = pValue;
        for (String acr : acronyms) {
            value = value.replaceAll(acr.toUpperCase(), acr);
        }
        return value;
    }

    public static <T extends Object> List<T> reverse(List<T> list) {
        List<T> copy = new ArrayList<T>(list.size());
        for (T t : list) {
            copy.add(t);
        }
        Collections.reverse(copy);
        return copy;
    }
}
