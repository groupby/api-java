package com.groupbyinc.api;

import com.groupbyinc.api.model.Refinement;
import com.groupbyinc.api.model.RefinementRange;
import com.groupbyinc.api.model.RefinementValue;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <code>
 * The Query object is responsible for sending requests to the Bridge.
 * <p/>
 * For example:
 * <p/>
 * Query query = new Query();
 * query.setSearchTerm("laptops");
 * SimpleBridge bridge = new SimpleBridge(key, host, port);
 * Results results = bridge.search(query);
 * <p/>
 * </code>
 *
 * @author will
 */
public class Query {
    private static final String DOTS = "\\.\\.";
    public static final String FIELDS_DELIMITER = ".";
    public static final String FIELDS_SPLITTER = "\\.";
    public static final String PARAM_REFINEMENT_SEARCH = "rs";
    public static final String PARAM_REFINEMENTS = "r";
    public static final String PARAM_PAGE_SIZE = "p";
    public static final String PARAM_SKIP = "sk";
    public static final String PARAM_SEARCH_STRING = "q";
    public static final String PARAM_CLIENT_KEY = "c";
    public static final String PARAM_CUSTOM_URL_PARAMS = "o";
    public static final String PARAM_SORT = "s";
    public static final String PARAM_FIELDS = "f";
    public static final String PARAM_UNIONABLE_FIELDS = "uf";
    public static final String PARAM_SUB_COLLECTION = "l";
    public static final String PARAM_SUB_FRONT_END = "e";
    public static final String PARAM_BIASING_PROFILE = "b";
    public static final String PARAM_ACCURATE_COUNTS = "ac";
    public static final String PARAM_RETURN_BINARY = "rb";
    public static final String PARAM_REQUIRED_FIELDS = "rf";
    public static final String PARAM_PARTIAL_FIELDS = "pf";
    public static final String PARAM_LANGUAGE_RESTRICT = "lr";
    public static final String PARAM_INTERFACE_LANGUAGE = "hl";
    public static final String PARAM_PRUNE_REFINEMENTS = "pr";
    public static final String PARAM_RESTRICT_NAVIGATION = "rn";

    private String searchString;
    private String sort;
    private long skip = 0;
    private int pageSize = 10;
    private String subCollection;
    private String area;
    private String biasingProfile;
    private String partialFields;
    private String requiredFields;
    private String languageRestrict;
    private String interfaceLanguage;
    private String restrictNavigation;
    private boolean accurateCounts = false;
    private boolean returnBinary = true;
    private List<String> customUrlParams = new ArrayList<String>();
    private List<Refinement> refinements = new ArrayList<Refinement>();
    private List<String> fields = new ArrayList<String>();
    private List<String> unionableFields = new ArrayList<String>();
    private boolean pruneRefinements = true;
    private static final String Q = "\"";
    private static final String C = ":";

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public Query() {
        // default
    }

    /**
     * @return A list of fields that will be returned by the GSA.
     */
    public List<String> getFields() {
        return fields;
    }

    /**
     * @return A list of the fields that the bridge will treat as ORable.
     */
    public List<String> getUnionableFields() {
        return unionableFields;
    }

    /**
     * @return The current search string.
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * <code>
     * Set a search string. If q is blank all records are considered.
     * <p/>
     * JSON Reference:
     * <p/>
     * { q: 'gloves' }
     * <p/>
     * </code>
     *
     * @param pQuery
     *
     * @return
     */
    public Query setSearchString(String pQuery) {
        searchString = pQuery;
        return this;
    }

    /**
     * @return The data sub collection
     */
    public String getSubCollection() {
        return subCollection;
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
     * <p/>
     * JSON Reference:
     * <p/>
     * { e: 'Development' }
     * </code>
     *
     * @param pArea
     *         The area name.
     *
     * @return
     */
    public Query setArea(String pArea) {
        area = pArea;
        return this;
    }

    /**
     * <code>
     * The subcollection to use.  If you have uploaded additional data into sub-collections using the stream tool,
     * you can access them by specifying them here.  Note, you do not include your customer ID here.  Just the
     * sub-collection part.
     * So, if you have a subcollection called company1FAQs, you would specify
     * <p/>
     * "FAQs"
     * <p/>
     * You can also specify boolean logic to search across multiple sub collections.  To search across FAQs and
     * Manuals you would do
     * <p/>
     * "FAQs|Manuals"
     * <p/>
     * To find records that exist in both collections use the '.' and operator.
     * <p/>
     * "FAQs.Manuals"
     * <p/>
     * JSON Reference:
     * <p/>
     * { l: 'FAQs' }
     * { l: 'FAQs|Manuals' }
     * { l: 'FAQs.Manuals' }
     * <p/>
     * </code>
     *
     * @param pSubCollection
     *         The string representation of a collection query.
     *
     * @return
     */
    public Query setSubCollection(String pSubCollection) {
        subCollection = pSubCollection;
        return this;
    }

    /**
     * @return The current sort parameter
     */
    public String getSort() {
        return sort;
    }

    /**
     * <code>
     * Specifies the sort order. If not specified, the default is to sort by relevance.
     * This is a direct pass through to the GSA and as such the GSA documents will provide the most
     * up to date information on this parameter, please see:
     * [GSA 7.2 Sort](http://www.google.com/support/enterprise/static/gsa/docs/admin/72/gsa_doc_set/xml_reference
     * /
     * request_format.html#1077686)
     * <p/>
     * </code>
     *
     * @param pSort
     *         The String to sort on.
     *
     * @return
     */

    public Query setSort(String pSort) {
        sort = pSort;
        return this;
    }

    /**
     * @return A string representation of all of the currently set refinements
     */
    public String getRefinementString() {
        if (refinements == null || refinements.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (Refinement refinement : refinements) {
            result.append("~").append(refinement.toGsaString());
        }
        return result.toString();
    }

    /**
     * @return A string representation of all of the currently set custom url
     * parameters
     */
    public String getCustomUrlParamsString() {
        return stringify(customUrlParams, "~");
    }

    /**
     * @return A list of the fields that will be return with each record for
     * this query.
     */
    public String getFieldsString() {
        return getFieldsString(fields);
    }

    /**
     * @return A string representation of the fields the bridge will treat as
     * unionable.
     */
    public String getUnionableFieldsString() {
        return getFieldsString(unionableFields);
    }

    private String getFieldsString(List<String> pFields) {
        String fieldString = stringify(pFields, FIELDS_DELIMITER);
        if (fieldString != null && fieldString.startsWith(FIELDS_DELIMITER)) {
            fieldString = fieldString.substring(1);
        }
        return fieldString;
    }

    private String stringify(List<String> listToStringify, String pSep) {
        if (listToStringify == null || listToStringify.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (String value : listToStringify) {
            result.append(pSep).append(value);
        }
        return result.toString();
    }

    /**
     * @return A list of custom url params
     */
    public List<String> getCustomUrlParams() {
        return customUrlParams;
    }

    /**
     * <code>
     * Used internally by the bridge object to generate the JSON that is sent to the bridge service.
     * </code>
     *
     * @param pClientKey
     *         The client key used to authenticate this request.
     *
     * @return A JSON representation of this query object.
     */
    public String getBridgeJson(String pClientKey) {
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(PARAM_SEARCH_STRING, searchString);
        jsonMap.put(PARAM_REFINEMENTS, getRefinementString());
        jsonMap.put(PARAM_SORT, sort);
        jsonMap.put(PARAM_FIELDS, getFieldsString());
        jsonMap.put(PARAM_UNIONABLE_FIELDS, getUnionableFieldsString());
        jsonMap.put(PARAM_SUB_COLLECTION, subCollection);
        jsonMap.put(PARAM_SUB_FRONT_END, area);
        jsonMap.put(PARAM_BIASING_PROFILE, biasingProfile);
        jsonMap.put(PARAM_PARTIAL_FIELDS, partialFields);
        jsonMap.put(PARAM_REQUIRED_FIELDS, requiredFields);
        jsonMap.put(PARAM_PAGE_SIZE, String.valueOf(pageSize));
        jsonMap.put(PARAM_ACCURATE_COUNTS, accurateCounts ? "1" : null);
        jsonMap.put(PARAM_RETURN_BINARY, returnBinary ? "1" : null);
        jsonMap.put(PARAM_SKIP, String.valueOf(skip));
        jsonMap.put(PARAM_CUSTOM_URL_PARAMS, getCustomUrlParamsString());
        jsonMap.put(PARAM_CLIENT_KEY, pClientKey);
        if (restrictNavigation != null) {
            jsonMap.put(PARAM_RESTRICT_NAVIGATION, restrictNavigation);
        }
        if (languageRestrict != null) {
            jsonMap.put(PARAM_LANGUAGE_RESTRICT, languageRestrict);
        }
        if (interfaceLanguage != null) {
            jsonMap.put(PARAM_INTERFACE_LANGUAGE, interfaceLanguage);
        }
        if (pruneRefinements) {
            jsonMap.put(PARAM_PRUNE_REFINEMENTS, "1");
        }
        return mapToJson(jsonMap);
    }

    /**
     * @param pClientKey
     *
     * @return
     *
     * @deprecated
     */
    public String getBridgeJsonFilterSearch(String pClientKey) {
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(PARAM_REFINEMENT_SEARCH, searchString);
        jsonMap.put(PARAM_CLIENT_KEY, pClientKey);
        jsonMap.put(PARAM_SUB_COLLECTION, subCollection);
        jsonMap.put(PARAM_SUB_FRONT_END, area);
        return mapToJson(jsonMap);
    }

    private String mapToJson(Map<String, String> pJsonMap) {
        boolean didOne = false;
        StringBuilder st = new StringBuilder("{");
        Set<String> keys = pJsonMap.keySet();
        for (String key : keys) {

            String str = pJsonMap.get(key);
            if (StringUtils.isNotBlank(str)) {
                String v = str.replaceAll("\"", "\\\\\"");
                if (didOne) {
                    st.append(",");
                }
                st.append(Q).append(key).append(Q).append(C).append(Q).append(v).append(Q);
                didOne = true;
            }
        }
        st.append("}");
        return st.toString();
    }

    /**
     * <code>
     * A helper method to parse and set refinements.
     * If you pass in refinements of the format
     * <p/>
     * Brand=Bose~price:20..80
     * <p/>
     * The query object will correctly parse out the refinements.
     * </code>
     *
     * @param pRefinementString
     *         A tilde separated list of refinements
     *
     * @return
     */
    public Query addRefinementsByString(String pRefinementString) {
        if (pRefinementString == null) {
            return this;
        }
        String[] filterStrings = pRefinementString.split("~");
        for (String filterString : filterStrings) {
            if (StringUtils.isBlank(filterString)) {
                continue;
            }
            int colon = filterString.indexOf(":");
            int equals = filterString.indexOf("=");
            boolean isRange = colon != -1 && equals == -1;
            String[] nameValue = filterString.split("[:=]", 2);
            Refinement refinement = null;
            if (isRange) {
                refinement = new RefinementRange();
                if (nameValue[1].endsWith("..")) {
                    ((RefinementRange) refinement).setLow(
                            nameValue[1].split(DOTS)[0]);
                    ((RefinementRange) refinement).setHigh("");
                } else if (nameValue[1].startsWith("..")) {
                    ((RefinementRange) refinement).setLow("");
                    ((RefinementRange) refinement).setHigh(
                            nameValue[1].split(DOTS)[1]);
                } else {
                    String[] lowHigh = nameValue[1].split(DOTS);
                    ((RefinementRange) refinement).setLow(lowHigh[0]);
                    ((RefinementRange) refinement).setHigh(lowHigh[1]);
                }
            } else {
                refinement = new RefinementValue();
                ((RefinementValue) refinement).setValue(nameValue[1]);
            }
            refinement.setNavigationName(nameValue[0]);
            addRefinement(refinement);

        }
        return this;
    }

    /**
     * <code>
     * <p/>
     * Sets any additional parameters that can be used to trigger rules.
     * Takes a name and a value.
     * <p/>
     * JSON Reference:
     * Custom URL parameters separated by ~ in the form:
     * <p/>
     * { o: '~name=value~name=value' }
     * <p/>
     * <p/>
     * </code>
     *
     * @param pName
     *         The parameter name
     * @param pValue
     *         The parameter value
     *
     * @return
     */
    public Query addCustomUrlParam(String pName, String pValue) {
        customUrlParams.add(pName + "=" + pValue);
        return this;
    }

    /**
     * <code>
     * <p/>
     * Helper method that takes a ~ separated string of additional parameters that can be
     * used to trigger rules. Takes ~ separeted name/value list
     * <p/>
     * </code>
     *
     * @param pValues
     *         The list of name/values
     *
     * @return
     */
    public Query addCustomUrlParamsByString(String pValues) {
        if (pValues == null) {
            return this;
        }
        String[] values = pValues.split("&");
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                customUrlParams.add(value.trim());
            }
        }
        return this;
    }

    private Query addField(List<String> pFields, String... pName) {
        if (pName == null) {
            return this;
        }
        for (String name : pName) {
            pFields.add(name);
        }
        return this;
    }

    private Query addFieldsByString(List<String> pFields, String pValues) {
        if (pValues == null) {
            return this;
        }
        String[] values = pValues.split("~");
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                pFields.add(value.trim());
            }
        }
        return this;
    }

    /**
     * <code>
     * Specify which fields should be returned on each record that comes back from the GSA.
     * If you specify <b>*</b> all fields will be returned.
     * If this parameter is blank the bridge will return no attributes with the records.
     * <p/>
     * JSON Reference:
     * Fields are period delimited like so.
     * <p/>
     * { f: 'width.brand.height' }
     * <p/>
     * </code>
     *
     * @param pName
     *         The case-sensitive name of the attribute to return
     *
     * @return
     */
    public Query addField(String... pName) {
        return addField(fields, pName);
    }

    /**
     * <code>
     * Helper method to add fields that are ~ separated.
     * <p/>
     * addFieldsByString("brand~price~");
     * <p/>
     * Means that the GSA will return records with only brand and price returned.
     * </code>
     *
     * @param pValues
     *         Tilde separated list of field names
     *
     * @return
     */
    public Query addFieldsByString(String pValues) {
        return addFieldsByString(fields, pValues);
    }

    /**
     * <code>
     * Specify which fields should be queried with 'OR' instead of the default 'AND'.
     * <p/>
     * JSON Reference:
     * <p/>
     * { uf: 'field1~field2' }
     * <p/>
     * </code>
     *
     * @param pName
     *         The field that should be treated as OR by the bridge before
     *         being fired against the GSA.
     *
     * @return
     */
    public Query addUnionableField(String... pName) {
        return addField(unionableFields, pName);
    }

    /**
     * <code>
     * Helper method to add a ~ separated list of names
     * </code>
     *
     * @param pNames
     *         The tilde separated list of names.
     *
     * @return
     */
    public Query addUnionableFieldsByString(String pNames) {
        return addFieldsByString(unionableFields, pNames);
    }

    /**
     * <code>
     * Add a range refinement.  Takes a refinement name, a lower and upper bounds.
     * <p/>
     * JSON Reference: Value and range refinements are both stored in the r field.
     * <p/>
     * Note the colon and double periods.
     * <p/>
     * { r: 'rangeName:value1..value2' }
     * <p/>
     * </code>
     *
     * @param pName
     *         The name of the refinement
     * @param pLow
     *         The low value
     * @param pHigh
     *         The high value
     *
     * @return
     */
    public Query addRangeRefinement(String pName, String pLow, String pHigh) {
        RefinementRange refinement = new RefinementRange();
        refinement.setNavigationName(pName);
        refinement.setLow(pLow);
        refinement.setHigh(pHigh);
        addRefinement(refinement);
        return this;
    }

    /**
     * <code>
     * Add a value refinement.  Takes a refinement name and a value.
     * <p/>
     * JSON Reference: Value and range refinements are both stored in the r field.
     * <p/>
     * Note the equals.
     * <p/>
     * { r: 'name=value' }
     * <p/>
     * </code>
     *
     * @param pName
     *         The name of the refinement
     * @param pValue
     *         The refinement value
     *
     * @return
     */
    public Query addValueRefinement(String pName, String pValue) {
        RefinementValue refinement = new RefinementValue();
        refinement.setNavigationName(pName);
        refinement.setValue(pValue);
        addRefinement(refinement);
        return this;
    }

    private Query addRefinement(Refinement pRefinement) {
        refinements.add(pRefinement);
        return this;
    }

    /**
     * @return The number of documents to skip
     */
    public long getSkip() {
        return skip;
    }

    /**
     * <code>
     * Tell the bridge to offset to the Nth record.
     * <p/>
     * JSON Reference:
     * <p/>
     * { sk: '400' }
     * <p/>
     * </code>
     *
     * @param pSkip
     *         The number of documents to skip
     *
     * @return
     */
    public Query setSkip(long pSkip) {
        skip = pSkip;
        return this;
    }

    /**
     * @return The current page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * <code>
     * Page size.  Default is 10.
     * <p/>
     * JSON Reference:
     * <p/>
     * { p: '10' }
     * <p/>
     * </code>
     *
     * @param pPageSize
     *         The number of records to return with the query.
     *
     * @return
     */
    public Query setPageSize(int pPageSize) {
        pageSize = pPageSize;
        return this;
    }

    /**
     * @return True if accurate counts is set to true
     */
    public boolean isAccurateCounts() {
        return accurateCounts;
    }

    /**
     * <code>
     * !Warning! Tells the engine to more accurately count records and refinements.
     * This method will have severe impact on response time and throughput capacity.
     * This should only be used for debugging purposes.
     * <p/>
     * JSON Reference:
     * <p/>
     * { ac: '1' }
     * <p/>
     * </code>
     *
     * @param pAccurateCounts
     */
    public void setAccurateCounts(boolean pAccurateCounts) {
        accurateCounts = pAccurateCounts;
    }

    /**
     * @return A list of the currently set refinements
     */
    public List<Refinement> getRefinements() {
        return refinements;
    }

    /**
     * @return Is return JSON set to true.
     */
    public boolean isReturnBinary() {
        return returnBinary;
    }

    /**
     * <code>
     * Tells the bridge to return binary data. This is enabled by default in the APIs for more efficient transport.
     * To disable this in an API, set this to `false`.
     * <p/>
     * JSON Reference:
     * If passed a '1', informs the bridge to return binary data rather than JSON.
     * <p/>
     * { rj: '1' }
     * <p/>
     * </code>
     *
     * @param pReturnBinary
     *         Whether to tell the bridge to return binary data rather than JSON.
     *
     * @return
     */
    public Query setReturnBinary(boolean pReturnBinary) {
        returnBinary = pReturnBinary;
        return this;
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
     * <p/>
     * JSON Reference:
     * <p/>
     * { b: 'PopularityBias' }
     * <p/>
     * <p/>
     * </code>
     *
     * @param pBiasingProfile
     *
     * @return
     */
    public Query setBiasingProfile(String pBiasingProfile) {
        biasingProfile = pBiasingProfile;
        return this;
    }

    /**
     * @return The partial fields value
     */
    public String getPartialFields() {
        return partialFields;
    }

    /**
     * <code>
     * A direct pass through of the Partial Fields parameter for the GSA.  See the GSA documentation for further
     * details.
     * [GSA 7.2 Partial Fields Reference](http://www.google
     * .com/support/enterprise/static/gsa/docs/admin/72/gsa_doc_set/xml_reference/request_format.html#1077773)
     * <p/>
     * JSON Reference:
     * <p/>
     * { pf: '...'}
     * <p/>
     * </code>
     *
     * @param pPartialFields
     *         The partial fields value
     *
     * @return
     */
    public Query setPartialFields(String pPartialFields) {
        partialFields = pPartialFields;
        return this;
    }

    /**
     * @return The current required fields value.
     */
    public String getRequiredFields() {
        return requiredFields;
    }

    /**
     * <code>
     * A direct pass through of the Required Fields parameter for the GSA.  See the GSA documentation for further
     * details.
     * [GSA 7.2 Required Fields Reference](http://www.google
     * .com/support/enterprise/static/gsa/docs/admin/72/gsa_doc_set/xml_reference/request_format.html#1077773)
     * <p/>
     * JSON Reference:
     * <p/>
     * { rf: '...'}
     * <p/>
     * </code>
     *
     * @param pRequiredFields
     *
     * @return
     */
    public Query setRequiredFields(String pRequiredFields) {
        requiredFields = pRequiredFields;
        return this;
    }

    /**
     * @return The current language restrict value.
     */
    public String getLanguageRestrict() {
        return languageRestrict;
    }

    /**
     * <code>
     * Sets a language filter on the query. This allows the GSA to return only results from a certain
     * language as well as do accent-insensitive searches. Please see the GSA
     * documentation under [Language Filters](http://www.google
     * .com/support/enterprise/static/gsa/docs/admin/72/gsa_doc_set/xml_reference/request_format.html#1077312)
     * for a detailed list of supported languages and filter options. If you do not specify a language
     * (or pass in an unrecognized language), the language filter will be ignored.
     * <p/>
     * JSON Reference:
     * <p/>
     * { lr: 'lang_fr' }
     * <p/>
     * </code>
     *
     * @param pLanguage
     *         The value for language restrict
     *
     * @return
     */
    public Query setLanguageRestrict(String pLanguage) {
        languageRestrict = pLanguage;
        return this;
    }

    /**
     * @return The current interface language setting.
     */
    public String getInterfaceLanguage() {
        return interfaceLanguage;
    }

    /**
     * <code>
     * Sets the interface language (host language) of your interface. Please see the Google documentation
     * under [Interface Languages](https://developers.google
     * .com/custom-search/docs/xml_results?hl=en&csw=1#wsInterfaceLanguages)
     * for a detailed list of supported interface languages. If you do not specify a language
     * (or pass in an unrecognized language), the interface language will be ignored.
     * <p/>
     * JSON Reference
     * <p/>
     * { hl: 'fr' }
     * <p/>
     * <p/>
     * </code>
     *
     * @param pLanguage
     *         The interface language
     *
     * @return
     */
    public Query setInterfaceLanguage(String pLanguage) {
        interfaceLanguage = pLanguage;
        return this;
    }

    /**
     * @return Are refinements with zero counts being removed.
     */
    public boolean isPruneRefinements() {
        return pruneRefinements;
    }

    /**
     * <code>
     * Specifies whether refinements (which will not change the result set) should be pruned from
     * the available navigation data returned from the GSA. If not set, this will default to true
     * and empty refinements will not be returned.
     * <p/>
     * JSON Reference:
     * <p/>
     * { pr: '1' }
     * <p/>
     * </code>
     *
     * @param pPruneRefinements
     *
     * @return
     */
    public Query setPruneRefinements(boolean pPruneRefinements) {
        pruneRefinements = pPruneRefinements;
        return this;
    }

    public String getRestrictNavigation() {
        return restrictNavigation;
    }

    /**
     * <code>
     * !Warning!  Using this method can have performance implications as it will instruct the bridge to fire two
     * queries.
     * <p/>
     * Typically, this feature is used when you have a large number of navigation items that will overwhelm the end
     * user.
     * It works by using one of the existing navigation items to decide what the query is about and fires a second
     * query to
     * restrict the navigation to the most relevant set of navigation items for this search term.
     * <p/>
     * For example, if you pass in
     * <p/>
     * query.setSearchString("paper")
     * query.setRestrictNavigation("2:category")
     * <p/>
     * The bridge will find the category navigation refinements in the first query and fire a second query for the top 2
     * most populous categories.  Therefore, a search for something generic like "paper" will bring back top category
     * matches like
     * copy paper (1,030), paper pads (567).  The bridge will fire off the second query with the search term,
     * plus an OR refinement
     * with the most likely categories.  The navigation items in the first query are entirely replaced with the
     * navigation items in the
     * second query.
     * <p/>
     * The first parameter specifies how many categories should be used in the navigation restriction in the second
     * query.
     * <p/>
     * JSON Reference:
     * <p/>
     * { rn: '2:category' }
     * </code>
     *
     * @param pRestrictNavigation
     *         Restriction criteria
     *
     * @return
     */
    public Query setRestrictNavigation(String pRestrictNavigation) {
        restrictNavigation = pRestrictNavigation;
        return this;
    }
}
