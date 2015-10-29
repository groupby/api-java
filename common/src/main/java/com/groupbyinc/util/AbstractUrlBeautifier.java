package com.groupbyinc.util;

import com.groupbyinc.api.AbstractQuery;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.api.parser.ParserException;
import com.groupbyinc.api.request.AbstractRequest;
import com.groupbyinc.common.apache.commons.collections4.MapUtils;
import com.groupbyinc.common.apache.commons.lang3.ArrayUtils;
import com.groupbyinc.common.apache.commons.lang3.StringUtils;
import com.groupbyinc.common.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * @internal
 */
public abstract class AbstractUrlBeautifier<RQ extends AbstractRequest<RQ>, Q extends AbstractQuery<RQ, Q>> {

    public static final String PARAM_REPLACEMENT = "z";

    public static final String SEARCH_NAVIGATION_NAME = "search";
    private static final String REFINEMENTS_PARAM_DEFAULT = "refinements";
    private static final String ID = "id";

    private static final Pattern idPattern = Pattern.compile("(?:\\A|.*&)id=([^&]*).*");
    private final Navigation SEARCH_NAVIGATION = new Navigation().setDisplayName("");

    private List<UrlReplacementRule> replacementRules = new ArrayList<UrlReplacementRule>();
    private LinkedHashMap<String, Navigation> tokenToName = new LinkedHashMap<String, Navigation>();
    private LinkedHashMap<String, Navigation> nameToToken = new LinkedHashMap<String, Navigation>();
    private List<Navigation> remainingMappings = new ArrayList<Navigation>();
    private String refinementsQueryParameterName = REFINEMENTS_PARAM_DEFAULT;
    private String append = null;

    protected AbstractUrlBeautifier() {
    }

    protected abstract Q createQuery();

    /**
     * <code>
     * Convert a search term and a list of refinements into a beautified URL.
     * Each refinement that has a mapping will be turned into a path segment.
     * If a mapping has been created for search, the search term will also be
     * placed into a URL path segment.
     * </code>
     *
     * @param searchString
     *         The current search state.
     * @param existingRefinements
     *         The current refinement state
     *
     * @return
     *
     * @throws UrlBeautificationException
     */
    public String toUrl(String searchString, String existingRefinements) throws UrlBeautificationException {
        StringBuilder pathSegmentLookup = new StringBuilder("/");
        Q query = createQuery();
        if (StringUtils.isNotBlank(searchString)) {
            query.setQuery(searchString);
        }
        URIBuilder uri = new URIBuilder();
        uri.setPath("");
        query.addRefinementsByString(existingRefinements);
        Map<String, Navigation> navigations = getDistinctRefinements(query);
        addRefinements(query.getQuery(), navigations, pathSegmentLookup, uri);
        addReferenceBlock(pathSegmentLookup, uri);
        addAppend(uri);
        addUnmappedRefinements(navigations, uri);
        String uriString = uri.toString();
        return uriString.startsWith("null") ? uriString.substring(4) : uriString;
    }

    private Map<String, Navigation> getDistinctRefinements(Q query) {
        Map<String, Navigation> navigations = query.getNavigations();
        for (Navigation n : navigations.values()) {
            Set<String> names = new HashSet<String>();
            Iterator<Refinement> iterator = n.getRefinements().iterator();
            while (iterator.hasNext()) {
                Refinement refinement = iterator.next();
                String name = n.getName() + refinement.toTildeString();
                if (!names.contains(name)) {
                    names.add(name);
                } else {
                    iterator.remove();
                }
            }
        }
        return navigations;
    }

    private void addUnmappedRefinements(Map<String, Navigation> navigations, URIBuilder uri) {
        if (MapUtils.isNotEmpty(navigations)) {
            Q query = createQuery();
            Map<String, Navigation> distinctRefinements = getDistinctRefinements(query);
            for (Map.Entry<String, Navigation> entry : navigations.entrySet()) {
                Navigation n = distinctRefinements.get(entry.getKey());
                if (n == null) {
                    distinctRefinements.put(entry.getKey(), entry.getValue());
                } else {
                    n.getRefinements().addAll(entry.getValue().getRefinements());
                }
            }
            String refinements = query.getRefinementString();
            if (StringUtils.isNotBlank(refinements)) {
                uri.addParameter(refinementsQueryParameterName, query.getRefinementString());
            }
        }
    }

    private void addAppend(URIBuilder uri) {
        if (StringUtils.isNotBlank(append)) {
            uri.setPath(uri.getPath() + append);
        }
    }

    private void addSearchString(String searchString, StringBuilder reference, URIBuilder pUri) {
        if (StringUtils.isNotBlank(searchString)) {
            pUri.setPath(pUri.getPath() + "/" + UrlEncoder.encode(searchString));
            reference.append(SEARCH_NAVIGATION.getDisplayName());
        }
    }

    private void addReferenceBlock(StringBuilder reference, URIBuilder uri) {
        if (reference.length() > 1) {
            uri.setPath(uri.getPath() + reference.toString());
        }
    }

    private void addRefinements(String pSearchString, Map<String, Navigation> navigations,
                                StringBuilder pathSegmentLookup, URIBuilder uri) throws UrlBeautificationException {
        int indexOffSet = StringUtils.length(uri.getPath()) + 1;
        List<UrlReplacement> replacements = new ArrayList<UrlReplacement>();

        for (Navigation m : remainingMappings) {
            if (m == SEARCH_NAVIGATION && StringUtils.isNotBlank(pSearchString)) {
                pSearchString = applyReplacementRule(m, pSearchString, indexOffSet, replacements);
                indexOffSet += pSearchString.length() + 1;
                addSearchString(pSearchString, pathSegmentLookup, uri);
                continue;
            }
            Navigation n = navigations.get(m.getName());
            if (n != null) {
                Iterator<Refinement> ri = n.getRefinements().iterator();
                while (ri.hasNext()) {
                    Refinement r = ri.next();
                    switch (r.getType()) {
                    case Value:
                        pathSegmentLookup.append(getToken(n.getName()));
                        RefinementValue rv = (RefinementValue) r;
                        rv.setValue(applyReplacementRule(n, rv.getValue(), indexOffSet, replacements));
                        String encodedRefValue = "/" + UrlEncoder.encode(rv.getValue());
                        indexOffSet += rv.getValue().length() + 1;
                        uri.setPath(uri.getPath() + encodedRefValue);
                        ri.remove();
                        break;
                    case Range:
                        throw new UrlBeautificationException("You should not map ranges into URLs.");
                    }
                }
                if (n.getRefinements().isEmpty()) {
                    navigations.remove(n.getName());
                }
            }
        }
        if (!replacements.isEmpty()) {
            uri.addParameter(PARAM_REPLACEMENT, UrlReplacement.buildQueryString(replacements));
        }
    }

    /**
     * <code>
     * Convert a URI into a query object. Mappings will be converted to the
     * correct search and refinement state.
     * </code>
     *
     * @param uri
     *         The URI to parse into a query object
     *
     * @return
     *
     * @throws UrlBeautificationException
     */
    public Q fromUrl(String uri) throws UrlBeautificationException {
        Q query = fromUrl(uri, null);
        if (query == null) {
            throw new IllegalStateException("URL reference block is invalid, could not convert to query");
        }
        return query;
    }

    /**
     * <code>
     * Convert a URI into a query object. Mappings will be converted to the
     * correct search and refinement state.
     * </code>
     *
     * @param url
     *         The URI to parse into a query object
     * @param defaultQuery
     *         The default query to use if this URL does not correctly parse.
     *
     * @return
     *
     * @throws UrlBeautificationException
     */
    public Q fromUrl(String url, Q defaultQuery) throws UrlBeautificationException {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new UrlBeautificationException("Unable to parse url", e);
        }
        String urlQueryString = uri.getQuery();
        if (StringUtils.isNotBlank(urlQueryString) && idPattern.matcher(urlQueryString).matches()) {
            Matcher m = idPattern.matcher(urlQueryString);
            m.find();
            return createQuery().addValueRefinement(ID, m.group(1));
        } else {
            Q query = createQuery();
            String replacementUrlQueryString = getReplacementQuery(uri.getRawQuery());
            List<String> pathSegments = new ArrayList<String>();
            String uriPath = uri.getPath();
            if (StringUtils.isNotBlank(append) && uriPath.endsWith(append)) {
                uriPath = uriPath.substring(0, uriPath.length() - append.length());
            }
            pathSegments.addAll(asList(uriPath.split("/")));
            String pathSegmentLookup = lastSegment(pathSegments);

            if (pathSegments.size() > pathSegmentLookup.length()) {
                removeUnusedPathSegments(pathSegments, pathSegmentLookup);
            } else if (pathSegments.size() < pathSegmentLookup.length()) {
                return defaultQuery;
            }
            try {
                pathSegments = applyReplacementToPathSegment(
                        pathSegments, UrlReplacement.parseQueryString(replacementUrlQueryString));
            } catch (ParserException e) {
                throw new UrlBeautificationException("Replacement Query is malformed, returning default query", e);
            }
            while (pathSegments.size() > 0) {
                addRefinement(pathSegments, query, pathSegmentLookup);
            }
            if (StringUtils.isNotBlank(urlQueryString)) {
                String[] queryParams = urlQueryString.split("\\&");
                if (ArrayUtils.isNotEmpty(queryParams)) {
                    for (String keyValue : queryParams) {
                        if (keyValue.startsWith(refinementsQueryParameterName + "=")) {
                            String v = keyValue.substring(refinementsQueryParameterName.length());
                            query.addRefinementsByString(v);
                            break;
                        }
                    }
                }
            }
            return query;
        }
    }

    private void removeUnusedPathSegments(List<String> pathSegments, String pathSegmentLookup) {
        while (pathSegments.size() > pathSegmentLookup.length()) {
            pathSegments.remove(0);
        }
    }

    private String lastSegment(List<String> pathSegments) {
        return pathSegments.remove(pathSegments.size() - 1);
    }

    private void addRefinement(List<String> pathSegments, Q query, String referenceBlock) {
        String token = String.valueOf(referenceBlock.charAt(referenceBlock.length() - pathSegments.size()));
        if (token.equals(SEARCH_NAVIGATION.getDisplayName())) {
            query.setQuery(pathSegments.remove(0));
        } else if (getFieldName(token) != null) {
            query.addValueRefinement(getFieldName(token), pathSegments.remove(0));
        } else {
            pathSegments.remove(0);
        }
    }

    private String getFieldName(String token) {
        Navigation mapping = tokenToName.get(token);
        return mapping == null ? null : mapping.getName();
    }

    private String getToken(String name) {
        Navigation mapping = nameToToken.get(name);
        return mapping == null ? null : mapping.getDisplayName();
    }

    /**
     * <code>
     * Set the mapping from a search term to a path segment.
     * Note: you cannot use vowels for mapping tokens to prevent dictionary word creation.
     * The order in which this method is called determines where in the URL the search term will show up.
     * </code>
     *
     * @param pToken
     *         The single letter to represent search in the lookup.
     */
    public void setSearchMapping(char pToken) {
        SEARCH_NAVIGATION.setName(SEARCH_NAVIGATION_NAME);
        SEARCH_NAVIGATION.setDisplayName(String.valueOf(pToken));
        addMapping(SEARCH_NAVIGATION);
    }

    private void setValues(Navigation pMapping, String pName, String pToken) {
        pMapping.setName(pName);
        pMapping.setDisplayName(pToken);
    }

    /**
     * <code>
     * Set up a mapping for a refinement.
     * Note: you cannot use vowels for mapping tokens to prevent dictionary word creation.
     * The order in which this method is called determines where in the URL the refinements will show up.
     * </code>
     *
     * @param pToken
     *         The single letter to represent this refinement in the lookup.
     * @param pName
     *         The name of the navigation that will be mapped using this
     *         token.
     */
    public void addRefinementMapping(char pToken, String pName) {
        Navigation mapping = new Navigation();
        setValues(mapping, pName, String.valueOf(pToken));
        addMapping(mapping);
    }

    private void addMapping(Navigation mapping) {
        String name = mapping.getName();
        String token = mapping.getDisplayName();
        if (token.length() != 1 || StringUtils.isBlank(token)) {
            throw new IllegalStateException("Token length must be one");
        }
        if (token.matches("[aoeuiAOEUIyY]")) {
            throw new IllegalStateException("Vowels are not allowed to avoid Dictionary words appearing");
        }
        if (tokenToName.containsKey(token)) {
            throw new IllegalStateException(
                    "This token: " + token + " is already mapped to: " + tokenToName.get(token).getName());
        }
        tokenToName.put(token, mapping);
        nameToToken.put(name, mapping);
        remainingMappings.add(mapping);
    }

    /**
     * <code>
     * Clean up all mappings.
     * </code>
     */
    public void clearSavedFields() {
        append = null;
        tokenToName = new LinkedHashMap<String, Navigation>();
        nameToToken = new LinkedHashMap<String, Navigation>();
        remainingMappings = new ArrayList<Navigation>();
    }

    /**
     * <code>
     * Return the current appended URL segment
     * </code>
     *
     * @return
     */
    public String getAppend() {
        return append;
    }

    /**
     * <code>
     * Quite often URLs need to end with specific extensions to map to the correct controller in the backend.
     * Here you can set this value.
     * For example:
     * /index.html
     * </code>
     *
     * @param pAppend
     *         The value to append to each beautified URL.
     */
    public void setAppend(String pAppend) {
        append = pAppend;
    }

    /**
     * @return The name with which non-mapped refinements will be mapped into
     * the URL query string.
     */
    public String getRefinementsQueryParameterName() {
        return refinementsQueryParameterName;
    }

    /**
     * <code>
     * Sets the name of the query parameter with which non-mapped refinements will show up in the query string.
     * This includes ranges which are never mapped to beautified URLs.
     * </code>
     *
     * @param pRefinementsQueryParameterName
     *         The name of the query parameter to use.
     */
    public void setRefinementsQueryParameterName(String pRefinementsQueryParameterName) {
        refinementsQueryParameterName = pRefinementsQueryParameterName;
    }

    /**
     * <code>
     * Adds a new replacement rule that will be applied to the search term and mapped refinements. The original
     * search term and refinements will be put back into the query object.
     * If pReplacement is null the target character will be removed.
     * Note: Replacements that are chained may still contain the original target character.
     * For example:
     * addReplacementRule('x','y');
     * addReplacementRule('z','x');
     * The result of this may contain x's in the final result.
     * www.example.com/xyz will become www.example.com/yyz after the first replacement and www.example.com/yyx
     * after the second replacement.
     * </code>
     *
     * @param pTarget
     *         The char values to be replaced
     * @param pReplacement
     *         The replacement char value
     */
    public void addReplacementRule(char pTarget, Character pReplacement) {
        addReplacementRule(pTarget, pReplacement, null);
    }

    /**
     * <code>
     * Adds a new replacement rule that will only be applied to the specified refinement. The original
     * search term and refinements will be put back into the query object.
     * If pReplacement is null the target character will be removed.
     * Note: Replacements that are chained may still contain the original target character.
     * For example:
     * addReplacementRule('x', 'y', "brand");
     * addReplacementRule('z', 'x', "brand");
     * The result of this may contain x's in the final result.
     * www.example.com/xyz/b will become www.example.com/yyz/b after the first replacement and www.example.com/yyx/b
     * after the second replacement.
     * </code>
     *
     * @param pTarget
     *         The char values to be replaced
     * @param pReplacement
     *         The replacement char value
     * @param pRefinementName
     *         The name of the refinement that this replacement should be applied to.
     */
    public void addReplacementRule(char pTarget, Character pReplacement, String pRefinementName) {
        if (!((Character) pTarget).equals(pReplacement)) {
            replacementRules.add(new UrlReplacementRule(pTarget, pReplacement, pRefinementName));
        }
    }

    private String getReplacementQuery(String pQuery) {
        if (StringUtils.isNotBlank(pQuery)) {
            for (String token : pQuery.split("&")) {
                if (token.startsWith(PARAM_REPLACEMENT + "=")) {
                    return UrlEncoder.decode(token.substring(2));
                }
            }
        }
        return "";
    }

    private List<String> applyReplacementToPathSegment(List<String> pPathSegments, List<UrlReplacement> pReplacements) {
        if (pPathSegments.isEmpty()) {
            return pPathSegments;
        }
        List<String> replacedPathSegments = new ArrayList<String>(pPathSegments.size());
        int indexOffSet = 1;
        for (String pathSegment : pPathSegments) {
            StringBuilder decodedPathSegment = new StringBuilder(UrlEncoder.decode(pathSegment));
            for (UrlReplacement replacement : pReplacements) {
                replacement.apply(decodedPathSegment, indexOffSet);
            }
            replacedPathSegments.add(decodedPathSegment.toString());
            indexOffSet += decodedPathSegment.length() + 1;
        }
        return replacedPathSegments;
    }

    private String applyReplacementRule(Navigation pNavigation, String pValue, int pIndexOffSet,
                                        List<UrlReplacement> pReplacements) {
        StringBuilder urlBuilder = new StringBuilder(pValue);
        for (UrlReplacementRule replacementRule : replacementRules) {
            replacementRule.apply(urlBuilder, pIndexOffSet, pNavigation.getName(), pReplacements);
        }
        return urlBuilder.toString();
    }

    public static class UrlBeautificationException extends Exception {
        public UrlBeautificationException(String message) {
            super(message);
        }

        public UrlBeautificationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
