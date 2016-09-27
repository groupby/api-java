package com.groupbyinc.util;

import com.groupbyinc.api.parser.ParserException;
import org.junit.Test;

import java.util.List;

import static com.groupbyinc.util.UrlReplacement.OperationType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
    assertEquals("2-a", r.toString());
  }

  @Test()
  public void testFromStringInvalidString() {
    try {
      UrlReplacement.fromString("a2-a");
      fail("Exception not thrown");
    } catch (ParserException e) {
      //expected
      assertEquals("Invalid index:a2", e.getMessage());
    }
  }

  @Test
  public void testToStringAndFromStringWithInsert() throws Exception {
    assertToAndFromString("i2-/");
  }

  private void assertToAndFromString(String replacementString) throws Exception {
    UrlReplacement r = UrlReplacement.fromString(replacementString);
    String urlReplacementString = r.toString();
    UrlReplacement r2 = UrlReplacement.fromString(urlReplacementString);
    assertEquals(r, r2);
    assertEquals(r2.toString(), replacementString);
  }

  @Test
  public void testToStringAndFromStringWithSwap() throws Exception {
    assertToAndFromString("35-9");
  }

  @Test
  public void testToStringAndFromStringWithDash() throws Exception {
    assertToAndFromString("35--");
  }

  @Test
  public void testParseQueryString() throws Exception {
    assertParseQuery("2-a", "3-b", "4-c");
  }

  private void assertParseQuery(String... pReplacementString) throws Exception {
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
      assertEquals(urlReplacements.get(i)
                       .toString(), pReplacementString[pReplacementString.length - (i + 1)]);
    }
  }

  @Test
  public void testParseQueryStringWithInserts() throws Exception {
    assertParseQuery("2-a", "i3-b", "4-c");
  }

  @Test
  public void testParseQueryStringWithDash() throws Exception {
    assertParseQuery("2--", "i3-b", "4-c");
  }

  @Test
  public void testParseQueryStringWithDash2() throws Exception {
    assertParseQuery("2--", "i3-b", "4--");
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
    assertApply("abc123", "avc123", new UrlReplacement(1, "b", OperationType.Swap));
  }

  private void assertApply(String expected, String input, UrlReplacement urlReplacement) {
    StringBuilder stringBuilder = new StringBuilder(input);
    urlReplacement.apply(stringBuilder, 0);
    assertEquals(expected, stringBuilder.toString());
  }

  @Test
  public void testSimpleApplyReplaceAtStart() {
    assertApply("abc123", "zbc123", new UrlReplacement(0, "a", OperationType.Swap));
  }

  @Test
  public void testSimpleApplyReplaceAtStartBadIndex() {
    assertBadApply("zbc123", new UrlReplacement(-1, "a", OperationType.Swap));
  }

  private void assertBadApply(String input, UrlReplacement urlReplacement) {
    StringBuilder stringBuilder = new StringBuilder(input);
    urlReplacement.apply(stringBuilder, 0);
    assertEquals(input, stringBuilder.toString());
  }

  @Test
  public void testSimpleApplyReplaceAtEnd() {
    assertApply("abc123", "abc124", new UrlReplacement(5, "3", OperationType.Swap));
  }

  @Test
  public void testSimpleApplyReplaceAtEndBadIndex() {
    assertBadApply("abc124", new UrlReplacement(6, "3", OperationType.Swap));
  }

  @Test
  public void testSimpleApplyInsertAtEnd() {
    assertApply("abc123", "abc12", new UrlReplacement(5, "3", OperationType.Insert));
  }

  @Test
  public void testSimpleApplyInsertAtStart() {
    assertApply("abc123", "bc123", new UrlReplacement(0, "a", OperationType.Insert));
  }

  @Test
  public void testSimpleApplyInsertAtStartBadIndex() {
    assertBadApply("bc123", new UrlReplacement(-1, "a", OperationType.Insert));
  }

  @Test
  public void testSimpleApplyInsertAtEndBadIndex() {
    assertBadApply("abc12", new UrlReplacement(6, "3", OperationType.Insert));
  }

  @Test
  public void testEqualsAndHashCode() {
    UrlReplacement urlReplacement1 = new UrlReplacement(5, "3", OperationType.Insert);
    UrlReplacement urlReplacement2 = new UrlReplacement(5, "3", OperationType.Insert);
    UrlReplacement urlReplacement3 = new UrlReplacement(6, "3", OperationType.Insert);
    UrlReplacement urlReplacement4 = new UrlReplacement(6, "4", OperationType.Insert);
    UrlReplacement urlReplacement5 = new UrlReplacement(6, "4", OperationType.Swap);
    assertNotNull(urlReplacement1);
    assertEquals(urlReplacement1, urlReplacement1);
    assertTrue(urlReplacement1.equals(urlReplacement2));
    assertFalse(urlReplacement2.equals(urlReplacement3));
    assertFalse(urlReplacement3.equals(urlReplacement4));
    assertFalse(urlReplacement4.equals(urlReplacement5));

    assertTrue(urlReplacement1.hashCode() == urlReplacement1.hashCode());
    assertTrue(urlReplacement1.hashCode() == urlReplacement2.hashCode());
    assertFalse(urlReplacement2.hashCode() == urlReplacement3.hashCode());
    assertFalse(urlReplacement3.hashCode() == urlReplacement4.hashCode());
    assertFalse(urlReplacement4.hashCode() == urlReplacement5.hashCode());
  }
}
