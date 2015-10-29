package com.groupbyinc.api.tags;

import com.groupbyinc.api.Query;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.common.jackson.core.type.TypeReference;
import com.groupbyinc.common.jackson.Mappers;
import com.groupbyinc.util.AbstractUrlBeautifier;
import com.groupbyinc.util.UrlBeautifier;

import javax.servlet.jsp.JspException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UrlFunctions {

    private static final TypeReference<List<Navigation>> NAVIGATIONS_TYPE = new TypeReference<List<Navigation>>() {
    };

    public static String toUrlAdd(String identifier, String searchString, List<Navigation> navigations, String navigationName, Refinement refinement) throws JspException {
        UrlBeautifier urlBeautifier = getBeautifier(identifier);
        Query refinements = addRefinements(navigations, navigationName, refinement);
        try {
            return urlBeautifier.toUrl(searchString, refinements.getRefinementString());
        } catch (AbstractUrlBeautifier.UrlBeautificationException e) {
            throw new JspException("Unable to add to url", e);
        }
    }

    public static String toUrlRemove(String identifier, String searchString, List<Navigation> navigations, String navigationName, Refinement refinement) throws JspException {
        UrlBeautifier urlBeautifier = getBeautifier(identifier);
        Query refinements = removeRefinements(navigations, navigationName, refinement);
        try {
            return urlBeautifier.toUrl(searchString, refinements.getRefinementString());
        } catch (AbstractUrlBeautifier.UrlBeautificationException e) {
            throw new JspException("Unable to remove from url", e);
        }
    }

    private static Query removeRefinements(List<Navigation> navigations, String navigationName, Refinement refinement) {
        Query query = new Query();
        Map<String, Navigation> queryNavigations = query.getNavigations();

        for (Navigation n : navigations) {
            queryNavigations.put(n.getName(), n);
        }
        String stringRefinements = query.getRefinementString();
        query = new Query();
        query.addRefinementsByString(stringRefinements);
        queryNavigations = query.getNavigations();
        if (queryNavigations == null) {
            throw new IllegalStateException("No existing refinements so cannot remove a refinement");
        }
        if (refinement != null) {
            Iterator<Map.Entry<String, Navigation>> ni = queryNavigations.entrySet().iterator();
            while (ni.hasNext()) {
                Navigation n = ni.next().getValue();
                if (n.getName().equals(navigationName)) {
                    Iterator<Refinement> ri = n.getRefinements().iterator();
                    while (ri.hasNext()) {
                        Refinement r = ri.next();
                        if (r.toTildeString().equals(refinement.toTildeString())) {
                            ri.remove();
                        }
                    }
                    if (n.getRefinements().isEmpty()) {
                        ni.remove();
                    }
                }
            }
        }
        return query;
    }

    private static Query addRefinements(List<Navigation> navigations, String navigationName, Refinement refinement) {
        Query query = new Query();
        Map<String, Navigation> queryNavigations = query.getNavigations();
        if (navigations != null) {
            // copy navigations
            List<Navigation> navigationsCopy = Mappers.readValue(Mappers.writeValueAsBytes(navigations, false), NAVIGATIONS_TYPE, false);
            for (Navigation navigation : navigationsCopy) {
                queryNavigations.put(navigation.getName(), navigation);
            }
        }
        if (queryNavigations.get(navigationName) == null) {
            queryNavigations.put(navigationName, new Navigation().setName(navigationName));
        }
        if (refinement != null) {
            queryNavigations.get(navigationName).getRefinements().add(refinement);
        }
        return query;
    }

    private static UrlBeautifier getBeautifier(String identifier) throws JspException {
        UrlBeautifier urlBeautifier = UrlBeautifier.getUrlBeautifiers().get(identifier);
        if (urlBeautifier == null) {
            throw new JspException("Could not find UrlBeautifier named: " + identifier + ". Please call UrlBeautifier.createUrlBeautifier(String) to instantiate");
        }
        return urlBeautifier;
    }

}
