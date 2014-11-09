package com.groupbyinc.api.model;

/**
 * <code>
 * A RefinementRange extends Refinement and represents a navigation refinement between two numeric values.
 * Typically, price is a range refinement.
 * <p/>
 * - id: an MD5 hash of the content of this value.
 * - low: The lower bound value.
 * - high: The upper bound value.
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
public class RefinementRange extends Refinement {
    private static final long serialVersionUID = 1L;
    private String high;
    private String low;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public RefinementRange() {
        // default constructor
    }

    /**
     * @return Return the upper bound of this range.
     */
    public String getHigh() {
        return high;
    }

    /**
     * @param pHigh
     *         Set the uppermost value.
     *
     * @return
     */
    public RefinementRange setHigh(String pHigh) {
        high = pHigh;
        return this;
    }

    /**
     * @return Returns the lower bound of this range.
     */
    public String getLow() {
        return low;
    }

    /**
     * @param pLow
     *         Set the lower bound.
     *
     * @return
     */
    public RefinementRange setLow(String pLow) {
        low = pLow;
        return this;
    }

    /**
     * @return Type.Range
     */
    @Override
    public Type getType() {
        return Type.Range;
    }

    /**
     * @internal
     */
    @Override
    public String toGsaString() {
        return getNavigationName() + ":" + low + ".." + high;
    }

    @Override
    public RefinementRange setId(String pId) {
        return (RefinementRange) super.setId(pId);
    }

    @Override
    public RefinementRange setCount(int pCount) {
        return (RefinementRange) super.setCount(pCount);
    }

    @Override
    public RefinementRange setDisplayName(String pDisplayName) {
        return (RefinementRange) super.setDisplayName(pDisplayName);
    }

    @Override
    public RefinementRange setNavigationType(Navigation.Type pNavigationType) {
        return (RefinementRange) super.setNavigationType(pNavigationType);
    }

    @Override
    public RefinementRange setNavigationName(String pName) {
        return (RefinementRange) super.setNavigationName(pName);
    }

    @Override
    public RefinementRange setNavigationDisplayName(String pNavigationDisplayName) {
        return (RefinementRange) super.setNavigationDisplayName(pNavigationDisplayName);
    }

}
