package com.groupbyinc.api.request.refinement;

import com.groupbyinc.api.request.SelectedRefinement;

/**
 * <code>
 * A RefinementValue extends Refinement and represents a navigation refinement that holds a value.
 * Typically, brand is a value refinement.
 *
 * - `id`: an MD5 hash of the content of this value.
 * - `value`: The value of the refinement.
 * - `count`: the number of records that will be filtered down to by this navigation value.
 * - `displayName`: A never blank combination of the value / low and high properties.
 * - `navigationName`: - the name of the parent navigation.
 * - `isRange`: - whether this is a range refinement or a value refinement
 * - `type`: - the type of refinement, Value or Range
 *
 * </code>
 *
 * @author will
 */
public class SelectedRefinementValue extends SelectedRefinement<SelectedRefinementValue> {
    private String value;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public SelectedRefinementValue() {
        // default constructor
    }

    /**
     * @return Type.Value
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
    public SelectedRefinementValue setValue(String value) {
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
