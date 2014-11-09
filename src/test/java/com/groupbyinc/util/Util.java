package com.groupbyinc.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Util {
    private static final transient Logger LOG = LoggerFactory.getLogger(Util.class);

    public static List<String> grepPrint(String pInput, String pGrepFor) {
        List<String> grep = grep(pInput, pGrepFor);
        for (String gr : grep) {
            LOG.info(gr);
        }
        return grep;
    }

    public static List<String> grep(String pInput, String pGrepFor) {
        List<String> result = new ArrayList<String>();
        if (StringUtils.isNotBlank(pInput) && StringUtils.isNotBlank(pGrepFor)) {
            Pattern p = Pattern.compile(pGrepFor);
            for (String line : pInput.split("\n")) {
                Matcher matcher = p.matcher(line);
                if (matcher.find()) {
                    result.add(line);
                }
            }
        }
        return result;
    }

    public static void assertUrlEquals(String pString, String pString2) {
        List<String> list1 = asList(pString.split("[\\&\\?]"));
        List<String> list2 = asList(pString2.split("[\\&\\?]"));
        Collections.sort(list1);
        Collections.sort(list2);
        assertEquals(StringUtils.join(list1, "\n"), StringUtils.join(list2, "\n"));
    }

    public static void assertListEquals(List<String> pExpectedLines, List<String> pActualLines) {

        if (pExpectedLines.size()!=pActualLines.size()){
            fail("Expected line count: " + pExpectedLines.size() + " Actual line count: " + pActualLines.size());
        }
        Collections.sort(pExpectedLines);
        Collections.sort(pActualLines);

        for (int i = 0; i < pExpectedLines.size(); i++) {
            assertEquals(pExpectedLines.get(i), pActualLines.get(i));
        }
    }

    public static class LenientDoublesJsonComparator extends DefaultComparator {
        public LenientDoublesJsonComparator() {
            super(JSONCompareMode.NON_EXTENSIBLE);
        }

        @Override
        public void compareValues(String pPrefix, Object pExpectedValue, Object pActualValue, JSONCompareResult pResult)
                throws JSONException {
            if (pExpectedValue instanceof Number && pActualValue instanceof Number) {
                if (Math.abs(
                        ((Number) pExpectedValue).doubleValue() - ((Number) pActualValue).doubleValue()) > 0.0000001f) {
                    pResult.fail(pPrefix, pExpectedValue, pActualValue);
                }
            } else {
                super.compareValues(pPrefix, pExpectedValue, pActualValue, pResult);
            }
        }

    }

    public static void assertJsonLinesEquals(List<String> pExpectedLines, List<String> pActualLines) throws Exception {
        if (pExpectedLines.size()!=pActualLines.size()){
            fail("Expected line count: " + pExpectedLines.size() + " Actual line count: " + pActualLines.size());
        }
        Collections.sort(pExpectedLines);
        Collections.sort(pActualLines);

        for (int i = 0; i < pExpectedLines.size(); i++) {
            assertJsonEquals(pExpectedLines.get(i), pActualLines.get(i));
        }
    }

    public static void assertJsonEquals(String pExpected, String pActual) throws Exception {
        JSONObject expected = new JSONObject(pExpected);
        JSONObject actual = new JSONObject(pActual);
        JSONCompareResult result = JSONCompare.compareJSON(expected, actual, new LenientDoublesJsonComparator());
        if (!result.passed()) {
            System.out.println(
                    "reason: " + result.getMessage() + "\nexpected: " + expected.toString().replaceAll("\"", "'") +
                            "\n  actual: " + actual.toString().replaceAll("\"", "'"));
        }
        assertTrue(
                "reason: " + result.getMessage() + "\nexpected: " + expected.toString(4) + "\nactual: " +
                        actual.toString(4), result.passed());
    }

}
