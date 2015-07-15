package com.groupbyinc.api;

import com.groupbyinc.api.model.CustomUrlParam;
import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.Sort;
import com.groupbyinc.api.model.refinement.RefinementRange;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.api.parser.Mappers;
import com.groupbyinc.api.request.AbstractRequest;
import com.groupbyinc.api.request.RefinementsRequest;
import com.groupbyinc.api.request.RestrictNavigation;
import com.groupbyinc.api.request.SelectedRefinement;
import com.groupbyinc.api.request.refinement.SelectedRefinementRange;
import com.groupbyinc.api.request.refinement.SelectedRefinementValue;
import com.groupbyinc.common.util.collections4.CollectionUtils;
import com.groupbyinc.common.util.lang3.StringUtils;
import jregex.Pattern;
import jregex.RETokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author will
 * @internal
 */
public abstract class AbstractQuery<R extends AbstractRequest<R>, Q extends AbstractQuery<R, Q>> {
    private static final String DOTS = "\\.\\.";

    // matches a tilde separated string
    public static final String TILDE_REGEX = "~((?=[\\w]*[=:]))";

    private static <R extends AbstractRequest<R>> String requestToJson(R request) {
        try {
            return Mappers.writeValueAsString(request);
        } catch (IllegalArgumentException e) {
            return "{}";
        }
    }

    private static <R extends AbstractRequest<R>> String requestToJson(RefinementsRequest<R> request) {
        try {
            return Mappers.writeValueAsString(request);
        } catch (IllegalArgumentException e) {
            return "{}";
        }
    }

    private String query;
    private int skip = 0;
    private int pageSize = 10;
    private String collection;
    private String area;
    private String biasingProfile;
    private String language;
    private List<CustomUrlParam> customUrlParams = new ArrayList<CustomUrlParam>();
    private LinkedHashMap<String, Navigation> navigations = new LinkedHashMap<String, Navigation>();
    private List<String> fields = new ArrayList<String>();
    private List<String> orFields = new ArrayList<String>();
    private boolean pruneRefinements = true;
    private boolean returnBinary = true;
    private boolean disableAutocorrection = false;
    protected RestrictNavigation restrictNavigation;

    protected abstract R generateRequest();

    protected abstract RefinementsRequest<R> populateRefinementRequest();

    protected static com.groupbyinc.api.request.Sort convertSort(Sort sort) {
        com.groupbyinc.api.request.Sort convertedSort = null;
        if (sort != null) {
            convertedSort = new com.groupbyinc.api.request.Sort().setField(sort.getField());
            switch (sort.getOrder()) {
            case Ascending:
                convertedSort.setOrder(com.groupbyinc.api.request.Sort.Order.Ascending);
                break;
            case Descending:
                convertedSort.setOrder(com.groupbyinc.api.request.Sort.Order.Descending);
                break;
            }
        }
        return convertedSort;
    }

    private List<SelectedRefinement> generateSelectedRefinements(LinkedHashMap<String, Navigation> navigations) {
        List<SelectedRefinement> refinements = new ArrayList<SelectedRefinement>();
        for (Navigation n : navigations.values()) {
            for (Refinement r : n.getRefinements()) {
                switch (r.getType()) {
                case Range: {
                    RefinementRange rr = (RefinementRange) r;
                    refinements.add(
                            new SelectedRefinementRange().setNavigationName(n.getName()).setLow(
                                    rr.getLow()).setHigh(rr.getHigh()).setExclude(rr.getExclude()));
                    break;
                }
                case Value: {
                    RefinementValue rv = (RefinementValue) r;
                    refinements.add(
                            new SelectedRefinementValue().setNavigationName(n.getName()).setValue(
                                    rv.getValue()).setExclude(rv.getExclude()));
                    break;
                }
                }
            }
        }
        return refinements;
    }

    private R populateRequest(String clientKey) {
        R request = generateRequest();
        request.setClientKey(clientKey);
        request.setArea(area);
        request.setCollection(collection);
        request.setQuery(query);
        request.setFields(fields);
        request.setOrFields(orFields);
        request.setLanguage(language);
        request.setBiasingProfile(biasingProfile);
        request.setPageSize(pageSize);
        request.setSkip(skip);
        request.setCustomUrlParams(getCustomUrlParams());
        request.setRefinements(generateSelectedRefinements(navigations));
        request.setRestrictNavigation(convertRestrictNavigation());

        if (!pruneRefinements) {
            request.setPruneRefinements(false);
        }
        if (returnBinary) {
            request.setReturnBinary(true);
        }
        if (disableAutocorrection) {
            request.setDisableAutocorrection(true);
        }
        return request;
    }

    private com.groupbyinc.api.request.RestrictNavigation convertRestrictNavigation() {
        return restrictNavigation == null ? null : new com.groupbyinc.api.request.RestrictNavigation().setName(
                restrictNavigation.getName()).setCount(restrictNavigation.getCount());
    }

    /**
     * <code>
     * Used internally by the bridge object to generate the JSON that is sent to the search service.
     * </code>
     *
     * @param clientKey The client key used to authenticate this request.
     * @return A JSON representation of this query object.
     */
    public String getBridgeJson(String clientKey) {
        R request = populateRequest(clientKey);
        return requestToJson(request);
    }

    /**
     * <code>
     * Used internally by the bridge object to generate the JSON that is sent to the search service.
     * </code>
     *
     * @param clientKey The client key used to authenticate this request.
     * @return A JSON representation of this query object.
     */
    public String getBridgeRefinementsJson(String clientKey, String navigationName) {
        RefinementsRequest<R> request = populateRefinementRequest();
        request.setOriginalQuery(populateRequest(clientKey));
        request.setNavigationName(navigationName);
        return requestToJson(request);
    }

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public AbstractQuery() {
        // default
    }

    /**
     * @return The current search string.
     */
    public String getQuery() {
        return query;
    }

    /**
     * <code>
     * Set a search string. If query is blank all records are considered.
     *
     * JSON Reference:
     *
     *     { "query": "gloves" }
     *
     * </code>
     *
     * @param query The search term to fire against the engine
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setQuery(String query) {
        this.query = query;
        return (Q) this;
    }

    /**
     * @deprecated since 2.0, use getCollection instead.
     * @return The data collection
     */
    public String getSubCollection() {
        return collection;
    }

    /**
     * @deprecated since 2.0, use setCollection instead.
     *
     * @param subCollection The string representation of a collection query.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setSubCollection(String subCollection) {
        collection = subCollection;
        return (Q) this;
    }

    /**
     * @return The data collection
     */
    public String getCollection() {
        return collection;
    }

    /**
     * <code>
     * The collection to use.  If you have uploaded additional data into collections apart from the default
     * collection using the stream tool, you can access them by specifying them here.
     *
     * You can also search across multiple collections.  To search across FAQs and Manuals you would do
     *
     *     "FAQs|Manuals"
     *
     * JSON Reference:
     *
     *     { "collection": "FAQs" }
     *     { "collection": "FAQs|Manuals" }
     *
     * </code>
     *
     * @param collection The string representation of a collection query.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setCollection(String collection) {
        this.collection = collection;
        return (Q) this;
    }

    /**
     * @return The area name
     */
    public String getArea() {
        return area;
    }

    /**
     * <code>
     * The area you wish to fire against, production, staging, etc...
     * If blank, the default production area will be used.
     *
     * JSON Reference:
     *
     *     { "area": "Development" }
     * </code>
     *
     * @param area The area name.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setArea(String area) {
        this.area = area;
        return (Q) this;
    }

    /**
     * @return A string representation of all of the currently set refinements
     */
    public String getRefinementString() {
        if (CollectionUtils.isNotEmpty(navigations.values())) {
            StringBuilder result = new StringBuilder();
            for (Navigation n : navigations.values()) {
                for (Refinement r : n.getRefinements()) {
                    result.append("~").append(n.getName()).append(r.toTildeString());
                }
            }
            if (result.length() > 0) {
                return result.toString();
            }
        }
        return null;
    }

    /**
     * @return A string representation of all of the currently set custom url
     * parameters
     */
    public String getCustomUrlParamsString() {
        if (CollectionUtils.isEmpty(customUrlParams)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (CustomUrlParam customUrlParam : customUrlParams) {
            result.append("~").append(customUrlParam.getKey()).append("=").append(customUrlParam.getValue());
        }
        return result.toString();
    }

    /**
     * @return A list of custom url params
     */
    public List<CustomUrlParam> getCustomUrlParams() {
        return customUrlParams;
    }

    /**
     * @param clientKey Your client key
     * @return
     * @internal
     */
    protected String getBridgeJsonRefinementSearch(String clientKey) {
        R request = generateRequest();
        request.setClientKey(clientKey);
        request.setCollection(collection);
        request.setArea(area);
        request.setRefinementQuery(query);
        return requestToJson(request);
    }

    protected String[] splitRefinements(String refinementString) {
        if (StringUtils.isNotBlank(refinementString)) {
            Pattern pattern = new Pattern(TILDE_REGEX);
            RETokenizer tokenizer = pattern.tokenizer(refinementString);
            return tokenizer.split();
        }   
        return new String[]{};
    }

    /**
     * <code>
     * A helper method to parse and set refinements.
     * If you pass in refinements of the format
     *
     *     Brand=Bose~price:20..80
     *
     * The query object will correctly parse out the refinements.
     *
     * </code>
     *
     * @param refinementString A tilde separated list of refinements
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addRefinementsByString(String refinementString) {
        if (refinementString == null) {
            return (Q) this;
        }
        String[] filterStrings = splitRefinements(refinementString);
        for (String filterString : filterStrings) {
            if (StringUtils.isBlank(filterString) || "=".equals(filterString)) {
                continue;
            }
            int colon = filterString.indexOf(":");
            int equals = filterString.indexOf("=");
            boolean isRange = colon != -1 && equals == -1;
            String[] nameValue = filterString.split("[:=]", 2);
            Refinement refinement;
            if (isRange) {
                RefinementRange rr = new RefinementRange();
                if (nameValue[1].endsWith("..")) {
                    rr.setLow(nameValue[1].split(DOTS)[0]);
                    rr.setHigh("");
                } else if (nameValue[1].startsWith("..")) {
                    rr.setLow("");
                    rr.setHigh(nameValue[1].split(DOTS)[1]);
                } else {
                    String[] lowHigh = nameValue[1].split(DOTS);
                    rr.setLow(lowHigh[0]);
                    rr.setHigh(lowHigh[1]);
                }
                refinement = rr;
            } else {
                refinement = new RefinementValue();
                ((RefinementValue) refinement).setValue(nameValue[1]);
            }
            if (StringUtils.isNotBlank(nameValue[0])) {
                addRefinement(nameValue[0], refinement);
            }
        }
        return (Q) this;
    }

    /**
     * <code>
     *
     * Sets any additional parameters that can be used to trigger rules.
     * Takes a CustomUrlParam object.
     *
     * </code>
     *
     * @param customUrlParam The parameter to add
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addCustomUrlParam(CustomUrlParam customUrlParam) {
        customUrlParams.add(customUrlParam);
        return (Q) this;
    }

    /**
     * <code>
     *
     * Sets any additional parameters that can be used to trigger rules.
     * Takes a name and a value.
     *
     * JSON Reference:
     *
     * Custom URL parameters separated by ~ in the form:
     *
     *     { "customUrlParams": [ { "key": "region", "value": "east" } ] }
     *
     *
     * </code>
     *
     * @param key   The parameter key
     * @param value The parameter value
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addCustomUrlParam(String key, String value) {
        customUrlParams.add(new CustomUrlParam().setKey(key).setValue(value));
        return (Q) this;
    }

    /**
     * <code>
     *
     * Helper method that takes a ~ separated string of additional parameters that can be
     * used to trigger rules. Takes ~ separated name/value list
     *
     * </code>
     *
     * @param values The list of name/values
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addCustomUrlParamsByString(String values) {
        if (values == null) {
            return (Q) this;
        }
        String[] params = values.split("&");
        for (String value : params) {
            if (StringUtils.isNotBlank(value)) {
                String[] keyValue = value.split("=");
                if (keyValue.length == 2 && StringUtils.isNotBlank(keyValue[0]) && StringUtils.isNotBlank(
                        keyValue[1])) {
                    customUrlParams.add(new CustomUrlParam().setKey(keyValue[0]).setValue(keyValue[1]));
                }
            }
        }
        return (Q) this;
    }

    /**
     * @return A list of fields that will be returned by the engine.
     */
    public List<String> getFields() {
        return fields;
    }

    @SuppressWarnings("unchecked")
    private Q addField(List<String> fields, String... name) {
        if (name == null) {
            return (Q) this;
        }
        Collections.addAll(fields, name);
        return (Q) this;
    }

    /**
     * <code>
     * Specify which fields should be returned on each record that comes back from the engine. You may specify more
     * than one field, if you specify <b>*</b> all fields will be returned.
     * If this parameter is blank the search service will return no attributes with the records.
     *
     * JSON Reference:
     *
     *     { "fields": [ "width", "brand", "height" ] }
     *
     * </code>
     *
     * @param name The case-sensitive name of the attribute to return
     * @return
     */
    public Q addFields(String... name) {
        return addField(fields, name);
    }

    /**
     * @return A list of the fields that the search service will treat as OR-able.
     */
    public List<String> getOrFields() {
        return orFields;
    }

    /**
     * <code>
     * Specify which fields should be queried with 'OR' instead of the default 'AND'.
     *
     * This behavior is typically defined in command center on a per navigation basis.  However,
     * you can set which fields should be treated as an OR field at the query level if desired.
     *
     * As with normal refinement selections, once you have refined, the list of refinements for
     * that selected navigation will no longer be returned.
     *
     * JSON Reference:
     *
     *     { "orFields": [ "field1", "field2" ] }
     *
     * </code>
     *
     * @param name The field that should be treated as OR by the search service before
     *              being executed.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addOrField(String... name) {
        return addField(orFields, name);
    }

    /**
     * <code>
     * Add a range refinement.  Takes a refinement name, a lower and upper bounds.
     *
     * </code>
     *
     * @param navigationName The name of the refinement
     * @param low  The low value
     * @param high The high value
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addRangeRefinement(String navigationName, String low, String high) {
        return addRangeRefinement(navigationName, low, high, false);
    }

    /**
     * <code>
     * Add a range refinement.  Takes a refinement name, a lower and upper bounds, and whether or not to exclude
     * this refinement.
     *
     * </code>
     *
     * @param navigationName The name of the refinement
     * @param low  The low value
     * @param high The high value
     * @param exclude True if the results should exclude this range refinement, false otherwise
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addRangeRefinement(String navigationName, String low, String high, boolean exclude) {
        return addRefinement(navigationName, new RefinementRange().setLow(low).setHigh(high).setExclude(exclude));
    }

    /**
     * <code>
     * Add a value refinement.  Takes a refinement name and a value.
     *
     * </code>
     *
     * @param navigationName  The name of the navigation
     * @param value The refinement value
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addValueRefinement(String navigationName, String value) {
        return addValueRefinement(navigationName, value, false);
    }

    /**
     * <code>
     * Add a value refinement.  Takes a refinement name, a value, and whether or not to exclude this refinement.
     *
     * </code>
     *
     * @param navigationName  The name of the navigation
     * @param value The refinement value
     * @param exclude True if the results should exclude this value refinement, false otherwise
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addValueRefinement(String navigationName, String value, boolean exclude) {
        return addRefinement(navigationName, new RefinementValue().setValue(value).setExclude(exclude));
    }

    /**
     * <code>
     * Add a refinement.  Please note that refinements are case-sensitive
     *
     * JSON Reference:
     *
     * Value and range refinements are both appended to an array on the refinements field.
     *
     * Note the 'type' field, which marks the refinement as either a value or range refinement.
     *
     *     { "refinements": [ {"type": "Range", "navigationName": "price", "low": "1.0", "high": "2.0"},
     *                        {"type": "Value", "navigationName": "brand", "value": "Nike" } ] }
     *
     * Refinements can be negated by setting the exclude property. An excluded refinement will return
     * results that do not match the value or fall into the range specified in the refinement.
     *
     *     { "refinements": [ {"type": "Range", "navigationName": "price", "low": "1.0", "high": "2.0", "exclude": true},
     *                        {"type": "Value", "navigationName": "brand", "value": "Nike", "exclude": true } ] }
     *
     * </code>
     *
     * @param navigationName  The name of the refinement
     * @param refinement The refinement to add
     * @return
     */
    @SuppressWarnings("unchecked")
    private Q addRefinement(String navigationName, Refinement refinement) {
        Navigation navigation = navigations.get(navigationName);
        if (navigation == null) {
            navigation = new Navigation().setName(navigationName);
            navigation.setRange(refinement instanceof RefinementRange);
            navigations.put(navigationName, navigation);
        }
        navigation.getRefinements().add(refinement);
        return (Q) this;
    }

    /**
     * @return The number of documents to skip
     */
    public long getSkip() {
        return skip;
    }

    /**
     * <code>
     * Tell the search service to offset to the Nth record.
     *
     * JSON Reference:
     *
     *     { "skip": 400 }
     *
     * </code>
     *
     * @param skip The number of documents to skip
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setSkip(int skip) {
        this.skip = skip;
        return (Q) this;
    }

    /**
     * @return The current page size
     */
    public long getPageSize() {
        return pageSize;
    }

    /**
     * <code>
     * Page size.  Default is 10.
     *
     * JSON Reference:
     *
     *     { "pageSize": 8 }
     *
     * </code>
     *
     * @param pageSize The number of records to return with the query.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return (Q) this;
    }

    /**
     * @return A map of the currently set refinements
     */
    public Map<String, Navigation> getNavigations() {
        return navigations;
    }

    /**
     * @return Is return JSON set to true.
     */
    public boolean isReturnBinary() {
        return returnBinary;
    }

    /**
     * <code>
     * Tells the search service to return binary data. This is enabled by default in the APIs for more efficient transport.
     * To disable this in an API, set this to `false`.
     *
     * JSON Reference:
     *
     * If passed true, informs the search service to return binary data rather than JSON.
     *
     *     { "returnBinary": true }
     *
     * </code>
     *
     * @param returnBinary Whether to tell the search service to return binary data rather than JSON.
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setReturnBinary(boolean returnBinary) {
        this.returnBinary = returnBinary;
        return (Q) this;
    }

    /**
     * @return The current biasing profile name.
     */
    public String getBiasingProfile() {
        return biasingProfile;
    }

    /**
     * <code>
     * Override the biasing profile used for this query - takes precedence over any
     * biasing profile set in the command center.
     *
     * JSON Reference:
     *
     *     { "biasingProfile": "PopularityBias" }
     *
     *
     * </code>
     *
     * @param biasingProfile
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setBiasingProfile(String biasingProfile) {
        this.biasingProfile = biasingProfile;
        return (Q) this;
    }

    /**
     * @return The current language filter on the query.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * <code>
     * Sets the language filter on the query and restricts the results to a certain language. If you do not specify a
     * language, english ("lang_en") will be considered the default. An unrecognized language will result in an error.
     *
     * Currently supported languages are:
     *
     *     lang_en
     *
     * JSON Reference:
     *
     *     { "language": "lang_en" }
     *
     * </code>
     *
     * @param language The value for language restrict
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setLanguage(String language) {
        this.language = language;
        return (Q) this;
    }

    /**
     * @internal
     * @return Are refinements with zero counts being removed.
     */
    public boolean isPruneRefinements() {
        return pruneRefinements;
    }

    /**
     * <code>
     * Specifies whether refinements should be pruned from
     * the available navigation.
     * A refinement is pruned if the number of results for that refinement is zero.
     *
     * If all refinements from a navigation are pruned, that
     * navigation is also pruned.
     * Defaults to true
     *
     * JSON Reference:
     *
     *     { pruneRefinements: false }
     *
     * </code>
     *
     * @param pruneRefinements
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setPruneRefinements(boolean pruneRefinements) {
        this.pruneRefinements = pruneRefinements;
        return (Q) this;
    }

    /**
     * @internal
     * @return Is the auto-correction behavior disabled
     */
    public boolean isAutocorrectionDisabled() {
        return disableAutocorrection;
    }

    /**
     * <code>
     * Specifies whether the auto-correction behavior should be disabled. By default, when no results are returned
     * for the given query (and there is a did-you-mean available), the first did-you-mean is automatically queried
     * instead.
     *
     * Defaults to false
     *
     * JSON Reference:
     *
     *     { "disableAutocorrection": false }
     *
     * </code>
     *
     * @param disableAutocorrection
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setDisableAutocorrection(boolean disableAutocorrection) {
        this.disableAutocorrection = disableAutocorrection;
        return (Q) this;
    }

    /**
     * <code>
     * <b>Warning</b>  This will count as two queries against your search index.
     *
     * Typically, this feature is used when you have a large number of navigation items that will overwhelm the end
     * user. It works by using one of the existing navigation items to decide what the query is about and fires a second
     * query to restrict the navigation to the most relevant set of navigation items for this search term.
     *
     * For example, if you pass in a search of `paper` and a restrict navigation of `category:2`
     *
     * The bridge will find the category navigation refinements in the first query and fire a second query for the top 2
     * most populous categories.  Therefore, a search for something generic like "paper" will bring back top category
     * matches like copy paper (1,030), paper pads (567).  The bridge will fire off the second query with the search
     * term, plus an OR refinement with the most likely categories.  The navigation items in the first query are
     * entirely replaced with the navigation items in the second query, except for the navigation that was used for the
     * restriction so that users still have the ability to navigate by all category types.
     *
     * JSON Reference:
     *
     *     { "restrictNavigation": { "name": "category", "count": 2 } }
     *
     * </code>
     *
     * @param restrictNavigation
     *         Restriction criteria
     *
     * @return this query
     */
    public Q setRestrictNavigation(RestrictNavigation restrictNavigation) {
        this.restrictNavigation = restrictNavigation;
        return (Q) this;
    }

    /**
     * <code>
     * <b>Warning</b>  This will count as two queries against your search index.
     *
     * Typically, this feature is used when you have a large number of navigation items that will overwhelm the end
     * user. It works by using one of the existing navigation items to decide what the query is about and fires a second
     * query to restrict the navigation to the most relevant set of navigation items for this search term.
     *
     * For example, if you pass in a search of `paper` and a restrict navigation of `category:2`
     *
     * The bridge will find the category navigation refinements in the first query and fire a second query for the top
     * 2 most populous categories.  Therefore, a search for something generic like "paper" will bring back top category
     * matches like copy paper (1,030), paper pads (567).  The bridge will fire off the second query with the search
     * term, plus an OR refinement with the most likely categories.  The navigation items in the first query are
     * entirely replaced with the navigation items in the second query, except for the navigation that was used
     * for the restriction so that users still have the ability to navigate by all category types.
     * </code>
     *
     * @param name the name of the field should be used in the navigation restriction in the second query.
     * @param count the number of fields matches
     * @return this query
     */
    public Q setRestrictNavigation(String name, int count) {
        this.restrictNavigation = new RestrictNavigation().setName(name).setCount(count);
        return (Q) this;
    }
}
