package com.groupbyinc.api.model.refinement;

import com.groupbyinc.api.model.Refinement;

/**
 * <code>
 * A RefinementValue extends Refinement and represents a navigation refinement that holds a value.
 * Typically, brand is a value refinement.
 *
 * - `id`: an MD5 hash of the content of this value.
 * - `value`: The value of the refinement.
 * - `count`: the number of records that will be filtered down to by this navigation value.
 * - `isRange`: - whether this is a range refinement or a value refinement
 * - `type`: - the type of refinement, Value or Range
 *
 * </code>
 *
 * @author will
 */
public class RefinementValue extends Refinement<RefinementValue> {
    private String value;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public RefinementValue() {
        // default constructor
    }

    /**
     * @return Type.Value
     * @internal
     */
    @Override
    public Type getType() {
        return Type.Value;
    }

    /**
     * @return the value of this refinement
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value Set the value
     * @return
     */
    public Refinement setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * @internal
     */
    @Override
    public String toTildeString() {
        return "=" + value;
    }
}
