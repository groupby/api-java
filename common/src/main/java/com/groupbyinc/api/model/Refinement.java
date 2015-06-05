package com.groupbyinc.api.model;

import com.groupbyinc.api.model.refinement.RefinementRange;
import com.groupbyinc.api.model.refinement.RefinementValue;
import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonInclude;
import com.groupbyinc.common.jackson.annotation.JsonProperty;
import com.groupbyinc.common.jackson.annotation.JsonSubTypes;
import com.groupbyinc.common.jackson.annotation.JsonTypeId;
import com.groupbyinc.common.jackson.annotation.JsonTypeInfo;

/**
 * <code>
 * Abstract Refinement class holding common methods for RefinementRange and RefinementValue.
 * </code>
 *
 * @author will
 * @internal
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RefinementValue.class, name = "Value"),
        @JsonSubTypes.Type(value = RefinementRange.class, name = "Range")
})
public abstract class Refinement<T extends Refinement<T>> {
    public enum Type {
        Value, Range // NOSONAR
    }

    @JsonProperty("_id")
    private String id;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int count;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean exclude = false;

    /**
     * <code>
     * Types are either `Range` or `Value`
     *
     * They represent the objects RefinementRange and RefinementValue
     * </code>
     *
     * @return The type of this refinement
     */
    @JsonTypeId
    public abstract Type getType();

    /**
     * @return The ID is a MD5 of the name and value of the refinement.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Set the ID
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    /**
     * @return The number of records that will be left if this refinement is
     * selected.
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count Set the number of matches if this refinement is selected
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setCount(int count) {
        this.count = count;
        return (T) this;
    }

    /**
     * @return True if this is a range refinement.
     */
    @JsonIgnore
    public boolean isRange() {
        return getType() == Type.Range;
    }

    /**
     * @return True if the results should exclude this refinement and false to including it. Defaults to false.
     */
    public Boolean getExclude() {
        return exclude;
    }

    /**
     * @param exclude Set to true to get results that exclude this refinement, false to include this refinement.
     * @return
     */
    @SuppressWarnings("unchecked")
    public T setExclude(Boolean exclude) {
        this.exclude = exclude;
        return (T) this;
    }

    /**
     * @return
     * @internal
     */
    public abstract String toTildeString();
}
