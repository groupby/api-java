package com.groupbyinc.util;

import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.api.tags.UrlFunctions;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Created by ferron on 4/22/15.
 */
public class UrlFunctionsTest {

    private final String DEFAULT_BEAUTIFIER="default";

    UrlBeautifier urlBeautifier;

    @Before
    public void setUp() {
        UrlBeautifier.INJECTOR.set(new HashMap<String, UrlBeautifier>());
        UrlBeautifier.createUrlBeautifier(DEFAULT_BEAUTIFIER);
        urlBeautifier = UrlBeautifier.getUrlBeautifiers().get(DEFAULT_BEAUTIFIER);
        urlBeautifier.setSearchMapping('q');
        urlBeautifier.setAppend("/index.html");
    }

    @Test
    public void refinementAdditionWithMapping() throws JspException, AbstractUrlBeautifier.UrlBeautificationException {
        // http://localhost:8888/nikepoc/Women/Pink/gc/index.html?refinements=~product=Clothing&language=
        // http://localhost:8888/nikepoc/Women/Clothing/Pink/gtc/index.html

        urlBeautifier.addRefinementMapping('g', "gender");
        urlBeautifier.addRefinementMapping('t', "product");
        urlBeautifier.addRefinementMapping('s', "primarysport");
        urlBeautifier.addRefinementMapping('c', "simpleColorDesc");
        urlBeautifier.addRefinementMapping('l', "collections");
        urlBeautifier.addRefinementMapping('f', "league");

        List<Navigation> navigations = new ArrayList<Navigation>();
        navigations.add(new Navigation()
                                .setName("gender")
                                .setDisplayName("Gender")
                                .setType(Navigation.Type.String)
                                .setRefinements(asList(new RefinementValue().setValue("Women"))));

        navigations.add(
                new Navigation().setName("simpleColorDesc").setDisplayName("Color").setType(Navigation.Type.String)
                                .setRefinements(asList(new RefinementValue().setValue("Pink"))));


        String url = UrlFunctions.toUrlAdd(
                DEFAULT_BEAUTIFIER, "", navigations, "product", new RefinementValue().setValue("Clothing").setCount(87));
        assertEquals("/Women/Clothing/Pink/gtc/index.html", url);
    }

    @Test
    public void refinementAdditionWithMappingMulti() throws JspException, AbstractUrlBeautifier.UrlBeautificationException {
        // http://localhost:8888/nikepoc/Women/Pink/gc/index.html?refinements=~product=Clothing&language=
        // http://localhost:8888/nikepoc/Women/Clothing/Pink/gtc/index.html

        urlBeautifier.addRefinementMapping('g', "gender");
        urlBeautifier.addRefinementMapping('t', "product");
        urlBeautifier.addRefinementMapping('s', "primarysport");
        urlBeautifier.addRefinementMapping('c', "simpleColorDesc");
        urlBeautifier.addRefinementMapping('l', "collections");
        urlBeautifier.addRefinementMapping('f', "league");

        List<Navigation> navigations = new ArrayList<Navigation>();
        List<Refinement> refinements = new ArrayList<Refinement>();
        refinements.add(new RefinementValue().setValue("Women"));

        navigations.add(new Navigation()
                                .setName("gender")
                                .setDisplayName("Gender")
                                .setType(Navigation.Type.String)
                                .setRefinements(refinements));

        navigations.add(
                new Navigation().setName("simpleColorDesc").setDisplayName("Color").setType(Navigation.Type.String)
                                .setRefinements(Collections.singletonList(new RefinementValue().setValue("Pink"))));

        String url = UrlFunctions.toUrlAdd(
                DEFAULT_BEAUTIFIER, "", navigations, "gender", new RefinementValue().setValue("Men").setCount(87));
        assertEquals("/Women/Men/Pink/ggc/index.html", url);

        url = UrlFunctions.toUrlAdd(
                DEFAULT_BEAUTIFIER, "", navigations, "gender", new RefinementValue().setValue("Kid").setCount(874));
        assertEquals("/Women/Kid/Pink/ggc/index.html", url);
    }

    @Test
    public void refinementAdditionWithoutMapping () throws JspException {
        List<Navigation> navigations = new ArrayList<Navigation>();
        navigations.add(new Navigation()
                                .setName("gender")
                                .setDisplayName("Gender")
                                .setType(Navigation.Type.String)
                                .setRefinements(asList(new RefinementValue().setValue("Women"))));

        navigations.add(
                new Navigation().setName("simpleColorDesc").setDisplayName("Color").setType(Navigation.Type.String)
                                .setRefinements(asList(new RefinementValue().setValue("Pink"))));


        String url = UrlFunctions.toUrlAdd(
                DEFAULT_BEAUTIFIER, "", navigations, "product", new RefinementValue().setValue("Clothing").setCount(87));
        assertEquals("/index.html?refinements=%7Egender%3DWomen%7EsimpleColorDesc%3DPink%7Eproduct%3DClothing", url);
    }

    @Test
    public void refinementAdditionWithoutMappingAndSpace () throws JspException {
        List<Navigation> navigations = new ArrayList<Navigation>();
        navigations.add(new Navigation()
                                .setName("gender")
                                .setDisplayName("Gender")
                                .setType(Navigation.Type.String)
                                .setRefinements(asList(new RefinementValue().setValue("Women"))));

        navigations.add(
                new Navigation().setName("simpleColorDesc").setDisplayName("Color").setType(Navigation.Type.String)
                                .setRefinements(asList(new RefinementValue().setValue("Pink"))));


        String url = UrlFunctions.toUrlAdd(
                DEFAULT_BEAUTIFIER, "", navigations, "product", new RefinementValue().setValue("Clothing Box").setCount(87));
        assertEquals("/index.html?refinements=%7Egender%3DWomen%7EsimpleColorDesc%3DPink%7Eproduct%3DClothing+Box", url);
    }
}
