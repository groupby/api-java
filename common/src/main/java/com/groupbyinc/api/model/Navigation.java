package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonInclude;
import com.groupbyinc.common.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>
 * The Navigation object represents dynamic navigation sent back from the search engine.
 * Each navigation item has the following properties:
 *
 * - `id`: an MD5 hash of the name.
 * - `name`: the name of the metadata used to create this dynamic navigation option.
 * - `displayName`: the human digestible version of this name.
 * - `type`: the facet type, values of date, float, int, string.
 * - `range`: true if the navigation option is a range.
 * - `or`: true if the navigation option supports or-queries.
 * - `refinements`: A list of the refinement values for this dynamic navigation
 * - `sort`: If specified, an object detailing the sort field and sort order
 *
 * </code>
 *
 * @author will
 */
public class Navigation {
    public enum Type {
        Date, Float, Integer, String, Range_Date, Range_Integer, Range_Float // NOSONAR
    }

    public enum Sort {
        Count_Ascending, Count_Descending, Value_Ascending, Value_Descending // NOSONAR
    }

    @JsonProperty("_id")
    private String id;
    private String name;
    private String displayName;
    private boolean range = false;
    private boolean or = false;
    private Type type;
    private Sort sort;

    @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
    private Boolean moreRefinements = Boolean.FALSE;
    private List<Refinement> refinements = new ArrayList<Refinement>();
    private List<Metadata> metadata = new ArrayList<Metadata>();

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public Navigation() {
        // default constructor
    }

    /**
     * @return The name of the dynamic navigation attribute. This is the name of
     * the metadata that was uploaded as part of the feed
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name of the navigation
     * @return
     */
    public Navigation setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The human readable label for this navigation.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName Set the display name
     * @return
     */
    public Navigation setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * @return A list of refinement values that represent the ways in which you
     * can filter data.
     */
    public List<Refinement> getRefinements() {
        return refinements;
    }

    /**
     * @param refinements The refinement values
     * @return
     */
    public Navigation setRefinements(List<Refinement> refinements) {
        this.refinements = refinements;
        return this;
    }

    /**
     * @return A MD5 of the name, which means that this navigation ID is unique.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Set the ID
     * @return
     */
    public Navigation setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @return True if this navigation is a of type range.
     */
    @JsonProperty("range")
    public boolean isRange() {
        return range;
    }

    /**
     * @param range Set range
     * @return
     */
    public Navigation setRange(boolean range) {
        this.range = range;
        return this;
    }

    /**
     * <code>
     * If you are using the this object within a JSP you will not be able to reference it directly as
     * `navigation.or` is a reserved field in JSTL.  Instead reference it within quotes:
     *
     *     <div>
     *         ${navigation['or'] ? 'Or Query' : 'And Query'}
     *     </div>
     *
     * </code>
     * @return Is this dynamic navigation going to be treated as an OR field by the search service.
     */
    @JsonProperty("or")
    public boolean isOr() {
        return or;
    }

    /**
     * @param or
     *         Set whether this is an OR field
     *
     * @return
     */
    public Navigation setOr(boolean or) {
        this.or = or;
        return this;
    }

    /**
     * <code>
     * Will return one of the following types:
     *
     *     Date, Float, Integer, String
     *     Range_Date, Range_Integer, Range_Float
     *
     * </code>
     *
     * @return The type of navigation.
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type Set the type of navigation.
     * @return
     */
    public Navigation setType(Type type) {
        this.type = type;
        return this;
    }

    /**
     * <code>
     * Will return one of the following sort types:
     *
     *     Count_Ascending, Count_Descending
     *     Value_Ascending, Value_Descending
     *
     * </code>
     *
     * @return The sort option for this navigation.
     */
    public Sort getSort() {
        return sort;
    }

    /**
     * @param sort Set the sort type
     */
    @JsonIgnore
    public Navigation setSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    /**
     * <code>
     * Helper method
     * </code>
     *
     * @param sort Set the sort by string.
     */
    public Navigation setSort(String sort) {
        this.sort = Sort.valueOf(sort);
        return this;
    }

    /**
     * <code>
     * A list of metadata key-value pairs for a specified navigation item. Each value
     * contains the following properties:
     *
     * - key: a string containing the attribute name
     * - value: a string containing the attribute value
     *
     * </code>
     *
     * @return A list of metadata elements
     */
    public List<Metadata> getMetadata() {
        return metadata;
    }

    /**
     * @param metadata Set the metadata
     * @return
     */
    public Navigation setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * <code>
     * True if this navigation has more refinement values than the ones returned.
     * </code>
     *
     * @return True if this navigation has more refinement values than the ones returned, false otherwise.
     */
    public Boolean isMoreRefinements() {
        return moreRefinements;
    }

    /**
     * @param moreRefinements True if this navigation has more refinement values than the ones returned.
     * @return
     */
    public Navigation setMoreRefinements(Boolean moreRefinements) {
        this.moreRefinements = moreRefinements;
        return this;
    }
}
