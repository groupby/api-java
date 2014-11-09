package com.groupbyinc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.groupbyinc.api.parser.Model;

/**
 * <code>
 * Abstract Refinement class holding common methods for RefinementRange and RefinementValue.
 * </code>
 *
 * @author will
 * @internal
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@t")
@JsonSubTypes({
                      @Type(value = RefinementValue.class, name = "rv"),
                      @Type(value = RefinementRange.class, name = "rr")
              })
public abstract class Refinement extends Model {

    public enum Type {
        Range, Value // NOSONAR
    }

    private static final long serialVersionUID = 1L;
    @JsonProperty("_id")
    private String id;
    private int count;
    private String displayName;
    private String navigationName;
    private String navigationDisplayName;
    private Navigation.Type navigationType;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public Refinement() {
        // default constructor
    }

    /**
     * @return The ID is a MD5 of the name and value of the refinement.
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
    public Refinement setId(String pId) {
        id = pId;
        return this;
    }

    /**
     * @return The number of records that will be left if this refinement is
     * selected.
     */
    public int getCount() {
        return count;
    }

    /**
     * @param pCount
     *         Set the count
     *
     * @return
     */
    public Refinement setCount(int pCount) {
        count = pCount;
        return this;
    }

    /**
     * @return The human readable version of this refinement. Used for
     * debugging.
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
    public Refinement setDisplayName(String pDisplayName) {
        displayName = pDisplayName;
        return this;
    }

    /**
     * @return The display label name of the enclosing Navigation.
     */
    public String getNavigationDisplayName() {
        return navigationDisplayName;
    }

    /**
     * @param pNavigationDisplayName
     *         The the navigation display label.
     *
     * @return
     */
    public Refinement setNavigationDisplayName(String pNavigationDisplayName) {
        navigationDisplayName = pNavigationDisplayName;
        return this;
    }

    /**
     * @return The name of the enclosing navigation
     */
    public String getNavigationName() {
        return navigationName;
    }

    /**
     * @param pName
     *         Set the navigation name
     *
     * @return
     */
    public Refinement setNavigationName(String pName) {
        navigationName = pName;
        return this;
    }

    /**
     * @return True if this refinement a range.
     */
    @JsonIgnore
    public boolean isRange() {
        return getType() == Type.Range;
    }

    /**
     * <code>
     * The type of navigation can be one of the following:
     * <p/>
     * Currency, Date, Float, Integer, String
     * Range_Currency, Range_Date, Range_Integer, Range_Float
     * <p/>
     * </code>
     *
     * @return The type of Navigation.
     */
    public Navigation.Type getNavigationType() {
        return navigationType;
    }

    /**
     * @param pNavigationType
     *         Set the navigation type
     *
     * @return
     */
    public Refinement setNavigationType(Navigation.Type pNavigationType) {
        navigationType = pNavigationType;
        return this;
    }

    /**
     * <code>
     * Types are either
     * <p/>
     * Range
     * Value
     * <p/>
     * And represent the objects RefinementRange and RefinementValue
     * </code>
     *
     * @return The type of this refinement
     */
    @JsonIgnore
    public abstract Type getType();

    /**
     * @return
     *
     * @internal
     */
    public abstract String toGsaString();
}
