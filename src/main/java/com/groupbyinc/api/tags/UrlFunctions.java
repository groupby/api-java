package com.groupbyinc.api.tags;

import com.groupbyinc.api.Query;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.util.UrlBeautifier;

import javax.servlet.jsp.JspException;
import java.util.Iterator;
import java.util.List;

public class UrlFunctions {

    public static String toUrlAdd(String pIdentifier, String pSearchString, List<Refinement> pRefinements,
                                  Refinement pRefinement) throws JspException {
        UrlBeautifier urlBeautifier = getBeautifier(pIdentifier);
        Query refinements = addRefinements(pRefinements, pRefinement);
        return urlBeautifier.toUrl(
                pSearchString, refinements.getRefinementString());
    }

    public static String toUrlRemove(String pIdentifier, String pSearchString, List<Refinement> pRefinements,
                                     Refinement pRefinement) throws JspException {
        UrlBeautifier urlBeautifier = getBeautifier(pIdentifier);
        Query refinements = removeRefinements(pRefinements, pRefinement);
        return urlBeautifier.toUrl(
                pSearchString, refinements.getRefinementString());
    }

    private static Query removeRefinements(List<Refinement> pRefinements, Refinement pRefinement) {
        Query query = new Query();
        query.getRefinements().addAll(pRefinements);
        String stringRefinements = query.getRefinementString();
        query = new Query();
        query.addRefinementsByString(stringRefinements);
        List<Refinement> refinements = query.getRefinements();
        if (refinements == null) {
            throw new IllegalStateException(
                    "No existing refinements so cannot remove a refinement");
        }
        if (pRefinement != null) {
            Iterator<Refinement> iterator = refinements.iterator();
            while (iterator.hasNext()) {
                Refinement refinement = iterator.next();
                if (refinement.toGsaString().equals(pRefinement.toGsaString())) {
                    iterator.remove();
                }
            }
        }
        query = new Query();
        query.getRefinements().addAll(refinements);
        return query;
    }

    private static Query addRefinements(List<Refinement> pRefinements, Refinement pRefinement) {
        Query query = new Query();
        if (pRefinements != null) {
            query.getRefinements().addAll(pRefinements);
        }
        if (pRefinement != null) {
            query.getRefinements().add(pRefinement);
        }
        return query;
    }

    private static UrlBeautifier getBeautifier(String pIdentifier) throws JspException {
        UrlBeautifier urlBeautifier = UrlBeautifier.getUrlBeautifiers().get(
                pIdentifier);
        if (urlBeautifier == null) {
            throw new JspException(
                    "Could not find UrlBeautifier named: " + pIdentifier +
                            ". Please call UrlBeautifier.createUrlBeautifier(String) to instatiate");
        }
        return urlBeautifier;
    }
}
