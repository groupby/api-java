package com.groupbyinc.api.model;

/**
 * <code>
 * A RefinementValue extends Refinement and represents a navigation refinement that holds a value.
 * Typically, brand is a value refinement.
 * <p/>
 * - id: an MD5 hash of the content of this value.
 * - value: The value of the refinement.
 * - count: the number of records that will be filtered down to by this navigation value.
 * - displayName: A never blank combination of the value / low and high properties.
 * - navigationName - the name of the parent navigation.
 * - isRange - whether this is a range refinement or a value refinement
 * - type - the type of refinement, Value or Range
 * <p/>
 * </code>
 *
 * @author will
 */
public class RefinementValue extends Refinement {
    private static final long serialVersionUID = 1L;

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
     * @return the value of this refinement
     */
    public String getValue() {
        return value;
    }

    /**
     * @param pValue
     *         Set the value
     *
     * @return
     */
    public Refinement setValue(String pValue) {
        value = pValue;
        return this;
    }

    /**
     * @return Type.Value
     */
    @Override
    public Type getType() {
        return Type.Value;
    }

    /**
     * @internal
     */
    @Override
    public String toGsaString() {
        return getNavigationName() + "=" + value;
    }

    @Override
    public RefinementValue setId(String pId) {
        return (RefinementValue) super.setId(pId);
    }

    @Override
    public RefinementValue setCount(int pCount) {
        return (RefinementValue) super.setCount(pCount);
    }

    @Override
    public RefinementValue setDisplayName(String pDisplayName) {
        return (RefinementValue) super.setDisplayName(pDisplayName);
    }

    @Override
    public RefinementValue setNavigationType(Navigation.Type pNavigationType) {
        return (RefinementValue) super.setNavigationType(pNavigationType);
    }

    @Override
    public RefinementValue setNavigationName(String pName) {
        return (RefinementValue) super.setNavigationName(pName);
    }

    @Override
    public RefinementValue setNavigationDisplayName(String pNavigationDisplayName) {
        return (RefinementValue) super.setNavigationDisplayName(pNavigationDisplayName);
    }
}
