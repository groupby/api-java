package com.groupbyinc.util;

import com.groupbyinc.api.Query;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.RefinementValue;
import com.groupbyinc.api.parser.ParserException;
import com.groupbyinc.injector.StaticInjector;
import com.groupbyinc.injector.StaticInjectorFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * <code>
 * The UrlBeautifier is used to transform a search and navigation state into a beautified and SEO friendly URL.
 * It can convert from a query object into a URL and from a URL into a query object.
 * <p/>
 * ###Create a beautifier and set up the mappings
 * <p/>
 * UrlBeautifier defaultUrlBeautifier = UrlBeautifier.getUrlBeautifiers()
 * .get("default");
 * if (defaultUrlBeautifier == null) {
 * UrlBeautifier.createUrlBeautifier("default");
 * defaultUrlBeautifier = UrlBeautifier.getUrlBeautifiers().get("default");
 * defaultUrlBeautifier.addRefinementMapping('b', "brand");
 * defaultUrlBeautifier.setSearchMapping('q');
 * defaultUrlBeautifier.setAppend("/index.html");
 * defaultUrlBeautifier.addReplacementRule('/', '-');
 * defaultUrlBeautifier.addReplacementRule('|', null, "brand");
 * }
 * <p/>
 * ###From a URL
 * <p/>
 * Query query = defaultUrlBeautifier.fromUrl(pRequest.getRequestURI());
 * <p/>
 * ###To a URL
 * <p/>
 * In this example we'll use the JSTL EL function that uses the UrlBeautifier behind the scenes.
 * <p/>
 * <a href="<c:url value="${b:toUrlAdd('default', q, selectedRefinements, newRefinement)}"/>">
 * ${value.value} (${value['count'] })
 * </a>
 * <p/>
 * </code>
 *
 * @author will
 */
public class UrlBeautifier {
    public static final String PARAM_REPLACEMENT = "z";
    private static final transient Logger LOG = LoggerFactory.getLogger(UrlBeautifier.class.getName());

    public static final String SEARCH_NAVIGATION_NAME = "search";
    private static final String REFINEMENTS_PARAM_DEFAULT = "refinements";
    static final StaticInjector<Map<String, UrlBeautifier>> INJECTOR =
            new StaticInjectorFactory<Map<String, UrlBeautifier>>().create();

    static {
        INJECTOR.set(new HashMap<String, UrlBeautifier>());
    }

    private final String ID = "id";
    private final Pattern idPattern = Pattern.compile("(?:\\A|.*&)id=([^&]*).*");

    private List<Refinement> mappingOrder = new ArrayList<Refinement>();
    private List<UrlReplacementRule> replacementRules = new ArrayList<UrlReplacementRule>();
    private Map<String, Refinement> tokenToName = new HashMap<String, Refinement>();
    private Map<String, Refinement> nameToToken = new HashMap<String, Refinement>();
    private final RefinementValue SEARCH_REFINEMENT = new RefinementValue();
    private String refinementsQueryParameterName = REFINEMENTS_PARAM_DEFAULT;
    private String append = null;

    private UrlBeautifier() {
    }

    /**
     * <code>
     * Create a UrlBeautifier and store it for the lifetime of this JVM under the name specified.
     * </code>
     *
     * @param pName
     *         The handle back to this UrlBeautifier
     */
    public static void createUrlBeautifier(String pName) {
        getUrlBeautifiers().put(pName, new UrlBeautifier());
    }

    /**
     * <code>
     * Convert a search term and a list of refinements into a beautified URL.
     * Each refinement that has a mapping will be turned into a path segment.
     * If a mapping has been created for search, the search term will also be
     * placed into a URL path segment.
     * </code>
     *
     * @param pSearchString
     *         The current search state.
     * @param pExistingRefinements
     *         The current refinement state
     *
     * @return
     */
    public String toUrl(String pSearchString, String pExistingRefinements) {
        StringBuilder pathSegmentLookup = new StringBuilder("/");
        // StringBuilder url = new StringBuilder();
        Query pQuery = new Query();
        if (StringUtils.isNotBlank(pSearchString)) {
            pQuery.setSearchString(pSearchString);
        }
        URIBuilder uri = new URIBuilder();
        uri.setPath("");
        pQuery.addRefinementsByString(pExistingRefinements);
        List<Refinement> refinements = getDistinctRefinements(pQuery);
        addRefinements(pQuery.getSearchString(), refinements, pathSegmentLookup, uri);
        addReferenceBlock(pathSegmentLookup, uri);
        addAppend(uri);
        addUnmappedRefinements(refinements, uri);
        String uriString = uri.toString();
        return uriString.startsWith("null") ? uriString.substring(4) : uriString;
    }

    private List<Refinement> getDistinctRefinements(Query pQuery) {
        List<Refinement> refinements = pQuery.getRefinements();
        Set<String> names = new HashSet<String>();
        Iterator<Refinement> iterator = refinements.iterator();
        while (iterator.hasNext()) {
            Refinement refinement = iterator.next();
            String name = refinement.toGsaString();
            if (!names.contains(name)) {
                names.add(name);
            } else {
                iterator.remove();
            }
        }
        return refinements;
    }

    private void addUnmappedRefinements(List<Refinement> pRefinements, URIBuilder pUri) {
        if (pRefinements == null || pRefinements.isEmpty()) {
            return;
        }
        Query query = new Query();
        getDistinctRefinements(query).addAll(pRefinements);
        pUri.addParameter(
                refinementsQueryParameterName, query.getRefinementString());
    }

    private void addAppend(URIBuilder pUri) {
        if (StringUtils.isNotBlank(append)) {
            pUri.setPath(pUri.getPath() + append);
        }

    }

    private void addSearchString(String pSearchString, StringBuilder reference, URIBuilder pUri) {
        if (StringUtils.isNotBlank(pSearchString)) {
            pUri.setPath(pUri.getPath() + "/" + UrlEncoder.encode(pSearchString));
            reference.append(SEARCH_REFINEMENT.getNavigationDisplayName());
        }
    }

    private void addReferenceBlock(StringBuilder reference, URIBuilder pUri) {
        if (reference.length() > 1) {
            pUri.setPath(pUri.getPath() + reference.toString());
        }
    }

    private void addRefinements(String pSearchString, List<Refinement> pRefinements, StringBuilder pPathSegmentLookup,
                                URIBuilder pUri) {
        int indexOffSet = StringUtils.length(pUri.getPath()) + 1;
        List<UrlReplacement> replacements = new ArrayList<UrlReplacement>();
        mappingLabel:
        for (Refinement mapping : mappingOrder) {
            if (mapping == SEARCH_REFINEMENT && StringUtils.isNotBlank(pSearchString)) {
                pSearchString = applyReplacementRule(mapping, pSearchString, indexOffSet, replacements);
                indexOffSet += pSearchString.length() + 1;
                addSearchString(pSearchString, pPathSegmentLookup, pUri);
                continue;
            }
            Refinement refinement = null;
            while ((refinement = getRefinemnetByName(mapping.getNavigationName(), pRefinements)) != null) {
                switch (refinement.getType()) {
                case Value:
                    pPathSegmentLookup.append(getToken(refinement.getNavigationName()));
                    RefinementValue refValue = (RefinementValue) refinement;
                    refValue.setValue(
                            applyReplacementRule(mapping, refValue.getValue(), indexOffSet, replacements));
                    String encodedRefValue = "/" + UrlEncoder.encode(refValue.getValue());
                    indexOffSet += refValue.getValue().length() + 1;
                    pUri.setPath(pUri.getPath() + encodedRefValue);
                    pRefinements.remove(refinement);
                    break;
                case Range:
                    LOG.warn("You should not map ranges into URLs.");
                    continue mappingLabel;
                }
            }
        }
        if (!replacements.isEmpty()) {
            pUri.addParameter(PARAM_REPLACEMENT, UrlReplacement.buildQueryString(replacements));
        }
    }

    private Refinement getRefinemnetByName(String pName, List<Refinement> pRefinements) {
        for (Refinement refinement : pRefinements) {
            if (pName.equals(refinement.getNavigationName())) {
                return refinement;
            }
        }
        return null;
    }

    /**
     * <code>
     * Convert a URI into a query object. Mappings will be converted to the
     * correct search and refinement state.
     * </code>
     *
     * @param pUri
     *         The URI to parse into a query object
     *
     * @return
     *
     * @throws java.net.URISyntaxException
     */
    public Query fromUrl(String pUri) throws URISyntaxException {
        Query query = fromUrl(pUri, null);
        if (query == null) {
            throw new IllegalStateException(
                    "URL reference block is invalid, could not convert to query");
        }
        return query;
    }

    /**
     * <code>
     * Convert a URI into a query object. Mappings will be converted to the
     * correct search and refinement state.
     * </code>
     *
     * @param pUrl
     *         The URI to parse into a query object
     * @param pDefault
     *         The default query to use if this URL does not correctly parse.
     *
     * @return
     *
     * @throws java.net.URISyntaxException
     */

    public Query fromUrl(String pUrl, Query pDefault) throws URISyntaxException {
        URI uri = new URI(pUrl);
        String urlQueryString = uri.getQuery();
        if (StringUtils.isNotBlank(urlQueryString) && idPattern.matcher(urlQueryString).matches()) {
            Matcher m = idPattern.matcher(urlQueryString);
            m.find();
            return new Query().addValueRefinement(ID, m.group(1));
        } else {
            Query query = new Query();
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
                return pDefault;
            }
            try {
                pathSegments = applyReplacementToPathSegment(
                        pathSegments, UrlReplacement.parseQueryString(replacementUrlQueryString));
            } catch (ParserException e) {
                LOG.warn("Replacement Query is malformed, returning default query");
                return pDefault;
            }
            while (pathSegments.size() > 0) {
                addRefinement(pathSegments, query, pathSegmentLookup);
            }
            if (StringUtils.isNotBlank(urlQueryString)) {
                String[] queryParams = urlQueryString.split("\\&");
                if (queryParams != null && queryParams.length > 0) {
                    for (String keyValue : queryParams) {
                        if (keyValue.startsWith(
                                refinementsQueryParameterName + "=")) {
                            query.addRefinementsByString(keyValue.split("=", 2)[1]);
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
        String lastSegment = pathSegments.remove(pathSegments.size() - 1);
        return lastSegment;
    }

    private void addRefinement(List<String> pPathSegments, Query pQuery, String pReferenceBlock) {
        String token = String.valueOf(
                pReferenceBlock.charAt(
                        pReferenceBlock.length() - pPathSegments.size()));
        if (token.equals(SEARCH_REFINEMENT.getNavigationDisplayName())) {
            pQuery.setSearchString(pPathSegments.remove(0));
        } else if (getFieldName(token) != null) {
            pQuery.addValueRefinement(
                    getFieldName(token), pPathSegments.remove(0));
        } else {
            pPathSegments.remove(0);
        }
    }

    private String getFieldName(String pToken) {
        Refinement mapping = tokenToName.get(pToken);
        return mapping == null ? null : mapping.getNavigationName();
    }

    private String getToken(String pName) {
        return nameToToken.get(pName).getNavigationDisplayName();
    }

    /**
     * <code>
     * Set the mapping from a search term to a path segment.
     * Note: you cannot use vowels for mapping tokens to prevent dictionary word creation.
     * The order in which this method is called determines where in the URL the search term will show up.
     * <p/>
     * </code>
     *
     * @param pToken
     *         The single letter to represent search in the lookup.
     */
    public void setSearchMapping(char pToken) {
        SEARCH_REFINEMENT.setNavigationDisplayName(String.valueOf(pToken));
        SEARCH_REFINEMENT.setNavigationName(SEARCH_NAVIGATION_NAME);
        addMapping(SEARCH_REFINEMENT);
    }

    private void setValues(String pToken, String pName, Refinement mapping) {
        mapping.setNavigationDisplayName(pToken);
        mapping.setNavigationName(pName);
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
        Refinement mapping = new RefinementValue();
        setValues(String.valueOf(pToken), pName, mapping);
        addMapping(mapping);
    }

    private void addMapping(Refinement mapping) {
        String name = mapping.getNavigationName();
        String token = mapping.getNavigationDisplayName();
        if (mapping.getNavigationDisplayName().length() != 1 || StringUtils.isBlank(token)) {
            throw new IllegalStateException("Token length must be one");
        }
        if (mapping.getNavigationDisplayName().matches("[aoeuiAOEUIyY]")) {
            throw new IllegalStateException(
                    "Vowels are not allowed to avoid DICtionary words appearing");
        }
        if (tokenToName.containsKey(token)) {
            throw new IllegalStateException(
                    "This token: " + token + " is already mapped to: " + tokenToName.get(token).getNavigationName());
        }
        tokenToName.put(token, mapping);
        nameToToken.put(name, mapping);
        mappingOrder.add(mapping);
    }

    /**
     * <code>
     * Clean up all mappings.
     * </code>
     */
    public void clearSavedFields() {
        append = null;
        tokenToName = new HashMap<String, Refinement>();
        nameToToken = new HashMap<String, Refinement>();
        mappingOrder = new ArrayList<Refinement>();
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
     * <p/>
     * /index.html
     * <p/>
     * </code>
     *
     * @param pAppend
     *         The value to append to each beautified URL.
     */
    public void setAppend(String pAppend) {
        append = pAppend;
    }

    /**
     * <code>
     * Get a map of UrlBeautifiers keyed by name.
     * </code>
     *
     * @return
     */
    public static Map<String, UrlBeautifier> getUrlBeautifiers() {
        return INJECTOR.get();
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
     * <p/>
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
     * <p/>
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

    private String applyReplacementRule(Refinement pRefinement, String pUrl, int pIndexOffSet,
                                        List<UrlReplacement> pReplacements) {
        StringBuilder urlBuilder = new StringBuilder(pUrl);
        for (UrlReplacementRule replacementRule : replacementRules) {
            replacementRule.apply(urlBuilder, pIndexOffSet, pRefinement, pReplacements);
        }
        return urlBuilder.toString();
    }
}
