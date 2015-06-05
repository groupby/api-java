package com.groupbyinc.api.model;

/**
 * <code>
 * Metadata is associated with Navigation objects and Areas and allows the merchandiser,
 * from the command center to add additional information about a navigation or area.
 * For example there might be a UI hint that the price range navigation
 * should be displayed as a slider.
 * Or you might set an area metadata to inform the UI of the seasonal color scheme to use.
 * </code>
 *
 * @author will
 */
public class Metadata {
    private String key;
    private String value;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public Metadata() {
        // default constructor
    }

    /**
     * @return The name of this metadata.
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     *         Set the name of this key
     *
     * @return
     */
    public Metadata setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * @return The value associated with this key.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *         Set the value.
     *
     * @return
     */
    public Metadata setValue(String value) {
        this.value = value;
        return this;
    }
}
