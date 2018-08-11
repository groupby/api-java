package com.groupbyinc.api;

import com.groupbyinc.common.test.CircleCIParallelTestCase;
import com.groupbyinc.common.util.ResourceUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BridgeTest extends CircleCIParallelTestCase {

  @Test
  public void testGB1546() throws Exception {
    assertResponse("/GB-1546.json", false);
    assertResponse("/GB-1546.cbor", true);
  }

  @Test
  public void testCaching() {
    CloudBridge test = new CloudBridge("aoeu", "aoeu");
    assertEquals(0, test.getHeaders().size());
    test.setCachingEnabled(false);
    assertEquals("Skip-Caching", test.getHeaders().get(0).getName());
    test.setCachingEnabled(false);
    assertEquals(1, test.getHeaders().size());
    test.setCachingEnabled(true);
    assertEquals(0, test.getHeaders().size());
  }

  private void assertResponse(String resourcePath, boolean binary) throws Exception {
    assertNotNull(new Bridge("dummy", "dummy").map(ResourceUtils.readResourceIntoInputStream(resourcePath), binary));
  }
}