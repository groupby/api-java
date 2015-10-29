package com.groupbyinc.util;

import com.groupbyinc.api.BaseQuery;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.api.tags.UrlFunctions;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Ferron Hanse
 */
public class UrlFunctionsBugTest {

    private final String DEFAULT_BEAUTIFIER = "default";

    UrlBeautifier urlBeautifier;

    BaseQuery query;

    @Before
    public void setUp() {
        query = new BaseQuery();
        UrlBeautifier.INJECTOR.set(new HashMap<String, UrlBeautifier>());
        UrlBeautifier.createUrlBeautifier(DEFAULT_BEAUTIFIER);
        urlBeautifier = UrlBeautifier.getUrlBeautifiers().get(DEFAULT_BEAUTIFIER);
        urlBeautifier.addRefinementMapping('s', "size");
        urlBeautifier.setSearchMapping('q');
        urlBeautifier.setAppend("/index.html");
        urlBeautifier.addReplacementRule('/', ' ');
        urlBeautifier.addReplacementRule('\\', ' ');
    }

    @Test
    public void refinementAdditionWithMapping() throws JspException, AbstractUrlBeautifier.UrlBeautificationException {
        List<Navigation> navigations = new ArrayList<Navigation>();

        String refinementString = "Category Root~Athletics~Men's~Sneakers";

        String url = UrlFunctions.toUrlAdd(DEFAULT_BEAUTIFIER, "", navigations, "category_leaf_expanded", new RefinementValue().setValue(refinementString).setCount(5483));
        assertEquals("/index.html?refinements=%7Ecategory_leaf_expanded%3DCategory+Root%7EAthletics%7EMen%27s%7ESneakers", url);
    }

}
