package com.groupbyinc.api;

import com.groupbyinc.api.model.ResultsBase;

/**
 * @internal
 */
public abstract class AbstractBridgeFactory<R extends ResultsBase, B extends AbstractBridge<R>> {
    private B bridge;

    /**
     * <code>
     * Return a bridge that was previously set.
     * </code>
     *
     * @return The bridge
     *
     * @internal
     */
    public B getBridge() {
        if (bridge == null) {
            throw new IllegalStateException("Bridge not set");
        }
        return bridge;
    }

    /**
     * <code>
     * Set the bridge object for future use
     * </code>
     *
     * @param pBridge
     *         The bridge to set
     */
    public void setBridge(B pBridge) {
        bridge = pBridge;
    }

    /**
     * <code>
     * Clean up bridge resources
     * </code>
     */
    public void destroy() {
        bridge = null;
    }

    /**
     * <code>
     * Create a new bridge object
     * </code>
     */
    public abstract B create(String pClientKey, String pBridgeHost, int pBridgePort);
}
