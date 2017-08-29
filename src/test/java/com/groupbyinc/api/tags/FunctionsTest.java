package com.groupbyinc.api.tags;

import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.Results;
import com.groupbyinc.api.model.refinement.RefinementRange;
import com.groupbyinc.api.model.refinement.RefinementValue;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionsTest {

  @Test
  public void testUncamel() throws Exception {
    assertUncamel("Java API Reference", "javaApiReference");
    assertUncamel("Java API Reference", "javaAPIReference");
    assertUncamel("GSA Configuration", "gsaConfiguration");
    assertUncamel("Pier 1 Load Balancing", "Pier1LoadBalancing");
    assertUncamel(".NET API", ".NetApi");
    assertUncamel("Network Settings", "NetworkSettings");
    assertUncamel("FAQ", "faq");
  }

  public void assertUncamel(String expected, String test) {
    assertEquals(expected, Functions.uncamel(test));
  }

  @Test
  public void testReverse() throws Exception {
    List<String> reverse = Functions.reverse(asList("1", "2", "3"));
    assertEquals("[3, 2, 1]", reverse.toString());
  }

  @Test
  public void testRefinementSelected() {
    Results r = new Results();
    r.setSelectedNavigation(asList(
        new Navigation().setName("a").setOr(true).setRefinements(asList(new RefinementValue().setValue("1"), new RefinementValue().setValue("2"))),
        new Navigation().setName("b").setRange(true).setRefinements(singletonList((Refinement) new RefinementRange().setLow("0").setHigh("1"))),
        new Navigation().setName("c").setOr(false).setRefinements(asList(new RefinementValue().setValue("1"), new RefinementValue().setValue("2")))));

    assertFalse(Functions.isRefinementSelected(r, null, "1"));
    assertFalse(Functions.isRefinementSelected(r, "", "1"));
    assertFalse(Functions.isRefinementSelected(r, "a", null));
    assertFalse(Functions.isRefinementSelected(r, "a", ""));
    assertFalse(Functions.isRefinementSelected(r, "a", "3"));
    assertFalse(Functions.isRefinementSelected(r, "b", "1"));
    assertFalse(Functions.isRefinementSelected(r, "c", "3"));

    assertTrue(Functions.isRefinementSelected(r, "a", "1"));
    assertTrue(Functions.isRefinementSelected(r, "a", "2"));
    assertTrue(Functions.isRefinementSelected(r, "c", "1"));
    assertTrue(Functions.isRefinementSelected(r, "c", "2"));
  }
}
