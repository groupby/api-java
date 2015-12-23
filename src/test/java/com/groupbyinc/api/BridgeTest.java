package com.groupbyinc.api;

import com.groupbyinc.common.util.ResourceUtils;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by groupby on 10/23/15.
 */
public class BridgeTest {
    private void assertResponse(String resourcePath, boolean binary) throws Exception {
        assertNotNull(
                new Bridge("dummy", "dummy").map(ResourceUtils.readResourceIntoInputStream(resourcePath), binary));
    }

    @Test
    public void testGB1546() throws Exception {
        assertResponse("/GB-1546.json", false);
        assertResponse("/GB-1546.cbor", true);
    }
}