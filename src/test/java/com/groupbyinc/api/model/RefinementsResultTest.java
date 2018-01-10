package com.groupbyinc.api.model;

import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.api.request.RefinementsRequest;
import com.groupbyinc.api.request.Request;
import org.junit.Test;

import static com.groupbyinc.common.test.util.AssertUtils.assertJsonEquals;
import static java.util.Collections.singletonList;

public class RefinementsResultTest {

  @Test
  public void testResponse() throws Exception {
    RefinementValue rv = new RefinementValue();
    rv.setValue("Ö");
    RefinementsResult result = new RefinementsResult().setOriginalRequest(new RefinementsRequest().setNavigationName("theNav").setOriginalQuery(new Request().setQuery("abc")))
        .setNavigation(new Navigation().setName("theNav").setType(Navigation.Type.Value).setRefinements(singletonList((Refinement) rv)));

    assertJsonEquals("{" //
                     + "    'navigation': {" //
                     + "        'metadata': []," //
                     + "        'name': 'theNav'," //
                     + "        'or': false," //
                     + "        'range': false," //
                     + "        'refinements': [{" //
                     + "            'type': 'Value'," //
                     + "            'value': 'Ö'" //
                     + "        }]," //
                     + "        'type': 'Value'" //
                     + "    }," //
                     + "    'originalRequest': {" //
                     + "        'navigationName': 'theNav'," //
                     + "        'originalQuery': {'query': 'abc','pruneRefinements': true}" //
                     + "    }" //
                     + "}", result);
  }
}
