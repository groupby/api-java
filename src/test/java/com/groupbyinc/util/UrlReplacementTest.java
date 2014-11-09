package com.groupbyinc.util;

import com.groupbyinc.api.parser.ParserException;
import org.junit.Test;

import java.util.List;

import static com.groupbyinc.util.UrlReplacement.OperationType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UrlReplacementTest {

    @Test
    public void testToString() throws Exception {
        UrlReplacement r = new UrlReplacement(2, "a", OperationType.Swap);
        assertEquals("2-a", r.toString());
        r = new UrlReplacement(20, "%", OperationType.Insert);
        assertEquals("i20-%", r.toString());
    }

    @Test
    public void testFromString() throws Exception {
        UrlReplacement r = UrlReplacement.fromString("2-a");
    }

    @Test()
    public void testFromStringInvalidString() {
        try {
            UrlReplacement.fromString("a2-a");
            fail("Exception not thrown");
        } catch (ParserException e) {
            //expected
        }

    }

    @Test
    public void testToStringAndFromStringWithInsert() throws Exception {
        testToAndFromString("i2-/");
    }

    @Test
    public void testToStringAndFromStringWithSwap() throws Exception {
        testToAndFromString("35-9");
    }

    @Test
    public void testToStringAndFromStringWithDash() throws Exception {
        testToAndFromString("35--");
    }

    @Test
    public void testParseQueryString() throws Exception {
        testParseQuery("2-a", "3-b", "4-c");
    }

    @Test
    public void testParseQueryStringWithInserts() throws Exception {
        testParseQuery("2-a", "i3-b", "4-c");
    }

    @Test
    public void testParseQueryStringWithDash() throws Exception {
        testParseQuery("2--", "i3-b", "4-c");
    }

    @Test
    public void testParseQueryStringWithDash2() throws Exception {
        testParseQuery("2--", "i3-b", "4--");
    }

    @Test
    public void testParseQueryStringWithDashMismatch() {
        try {
            UrlReplacement.parseQueryString("2-a-i3-b--4-c");
            fail("Exception not thrown");
        } catch (ParserException e) {
            //expected
        }
    }

    @Test
    public void testSimpleApplyReplace() {
        testApply("avc123", "abc123", new UrlReplacement(1, "b", OperationType.Swap));
    }

    @Test
    public void testSimpleApplyReplaceAtStart() {
        testApply("zbc123", "abc123", new UrlReplacement(0, "a", OperationType.Swap));
    }

    @Test
    public void testSimpleApplyReplaceAtStartBadIndex() {
        testBadApply("zbc123", new UrlReplacement(-1, "a", OperationType.Swap));
    }

    @Test
    public void testSimpleApplyReplaceAtEnd() {
        testApply("abc124", "abc123", new UrlReplacement(5, "3", OperationType.Swap));
    }

    @Test
    public void testSimpleApplyReplaceAtEndBadIndex() {
        testBadApply("abc124", new UrlReplacement(6, "3", OperationType.Swap));
    }

    @Test
    public void testSimpleApplyInsertAtEnd() {
        testApply("abc12", "abc123", new UrlReplacement(5, "3", OperationType.Insert));
    }

    @Test
    public void testSimpleApplyInsertAtStart() {
        testApply("bc123", "abc123", new UrlReplacement(0, "a", OperationType.Insert));
    }

    @Test
    public void testSimpleApplyInsertAtStartBadIndex() {
        testBadApply("bc123", new UrlReplacement(-1, "a", OperationType.Insert));
    }

    @Test
    public void testSimpleApplyInsertAtEndBadIndex() {
        testBadApply("abc12", new UrlReplacement(6, "3", OperationType.Insert));
    }

    private void testBadApply(String pInput, UrlReplacement pUrlReplacement) {
        StringBuilder stringBuilder = new StringBuilder(pInput);
        pUrlReplacement.apply(stringBuilder, 0);
        assertEquals(pInput, stringBuilder.toString());
    }

    private void testApply(String pInput, String pExpected, UrlReplacement pUrlReplacement) {
        StringBuilder stringBuilder = new StringBuilder(pInput);
        pUrlReplacement.apply(stringBuilder, 0);
        assertEquals(pExpected, stringBuilder.toString());
    }

    private void testParseQuery(String... pReplacementString) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String replacement : pReplacementString) {
            if (sb.length() != 0) {
                sb.append('-');
            }
            sb.append(replacement);
        }
        List<UrlReplacement> urlReplacements = UrlReplacement.parseQueryString(sb.toString());

        assertEquals(pReplacementString.length, urlReplacements.size());

        for (int i = 0; i < urlReplacements.size(); i++) {
            assertEquals(urlReplacements.get(i).toString(), pReplacementString[pReplacementString.length - (i + 1)]);
        }
    }

    private void testToAndFromString(String replacementString) throws Exception {
        UrlReplacement r = UrlReplacement.fromString(replacementString);
        String urlReplacementString = r.toString();
        UrlReplacement r2 = UrlReplacement.fromString(urlReplacementString);
        assertEquals(r, r2);
        assertEquals(r2.toString(), replacementString);
    }
}
