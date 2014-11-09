package com.groupbyinc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupbyinc.api.parser.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>
 * The Navigation object represents dynamic navigation sent back from the GSA.
 * Each navigation item has the following properties:
 * <p/>
 * - id: an MD5 hash of the name.
 * - name: the name of the metadata used to create this dynamic navigation option.
 * - displayName: the human digestible version of this name.
 * - type: the facet type, values of currency, date, float, int, string.
 * - isRange: true if the navigation option is a range.
 * - refinementValues: A list of the refinement values for this dynamic navigation
 * <p/>
 * </code>
 *
 * @author will
 */
public class Navigation extends Model {
    private static final long serialVersionUID = 1L;

    public enum Type {
        Currency, Date, Float, Integer, String, Range_Currency, Range_Date, Range_Integer, Range_Float // NOSONAR
    }

    public enum Sort {
        Count_Ascending, Count_Descending, Value_Ascending, Value_Descending // NOSONAR
    }

    @JsonProperty("_id")
    private String id;
    private String name;
    @JsonProperty("isRange")
    private boolean range = false;
    @JsonProperty("isUnionable")
    private boolean unionable = false;
    private Type type;
    private Sort sort;
    private String displayName;
    private List<Refinement> refinementValues = new ArrayList<Refinement>();
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
     * @param pName
     *         The name of the navigation
     *
     * @return
     */
    public Navigation setName(String pName) {
        name = pName;
        return this;
    }

    /**
     * @return The human readable label for this navigation.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param pDisplayName
     *         Set the display name
     *
     * @return
     */
    public Navigation setDisplayName(String pDisplayName) {
        displayName = pDisplayName;
        return this;
    }

    /**
     * @return A list of refinement values that represent the ways in which you
     * can filter data.
     */
    public List<Refinement> getRefinementValues() {
        return refinementValues;
    }

    /**
     * @param pValues
     *         The refinement values
     *
     * @return
     */
    public Navigation setRefinementValues(List<Refinement> pValues) {
        refinementValues = pValues;
        return this;
    }

    /**
     * @return A MD5 of the name, which means that this navigation ID is unique.
     */
    public String getId() {
        return id;
    }

    /**
     * @param pId
     *         Set the ID
     *
     * @return
     */
    public Navigation setId(String pId) {
        id = pId;
        return this;
    }

    /**
     * @return True if this navigation is a of type range.
     */
    public boolean isRange() {
        return range;
    }

    /**
     * @param pRange
     *         Set range
     *
     * @return
     */
    public Navigation setRange(boolean pRange) {
        range = pRange;
        return this;
    }

    /**
     * @return Is this dynamic navigation going to be treated as an OR field in
     * the bridge layer.
     */
    public boolean isUnionable() {
        return unionable;
    }

    /**
     * @param pUnionable
     *         Set whether this is an OR field
     *
     * @return
     */
    public Navigation setUnionable(boolean pUnionable) {
        unionable = pUnionable;
        return this;
    }

    /**
     * <code>
     * Will return one of the following types:
     * <p/>
     * Currency, Date, Float, Integer, String
     * Range_Currency, Range_Date, Range_Integer, Range_Float
     * <p/>
     * </code>
     *
     * @return The type of navigation.
     */
    public Type getType() {
        return type;
    }

    /**
     * @param pType
     *         Set the type of navigation.
     *
     * @return
     */
    public Navigation setType(Type pType) {
        type = pType;
        return this;
    }

    /**
     * <code>
     * Will return one of the following sort types:
     * <p/>
     * Count_Ascending, Count_Descending
     * Value_Ascending, Value_Descending
     * <p/>
     * </code>
     *
     * @return The sort option for this navigation.
     */
    public Sort getSort() {
        return sort;
    }

    /**
     * @param pSort
     *         Set the sort type
     */
    @JsonIgnore
    public void setSort(Sort pSort) {
        sort = pSort;
    }

    /**
     * <code>
     * Helper method
     * </code>
     *
     * @param pSort
     *         Set the sort by string.
     */
    public void setSort(String pSort) {
        sort = Sort.valueOf(pSort);
    }

    /**
     * <code>
     * A list of metadata key-value pairs for a specified navigation item. Each value
     * contains the following properties:
     * <p/>
     * - key: a string containing the attribute name
     * - value: a string containing the attribute value
     * <p/>
     * </code>
     *
     * @return A list of metadata elements
     */
    public List<Metadata> getMetadata() {
        return metadata;
    }

    /**
     * @param pMetadata
     *         Set the metadata
     *
     * @return
     */
    public Navigation setMetadata(List<Metadata> pMetadata) {
        metadata = pMetadata;
        return this;
    }

}
