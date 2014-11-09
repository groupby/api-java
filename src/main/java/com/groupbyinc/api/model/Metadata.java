package com.groupbyinc.api.model;

import com.groupbyinc.api.parser.Model;

/**
 * <code>
 * Metadata is associated with Navigation objects and Areas and allows the merchandiser,
 * from the command center to add additional information about a navigation or area.
 * For example there might be a UI hint that the price range navigation
 * should be displayed as a slider.
 * Or you might set an area metadata to inform the UI of the seasonal colour scheme to use.
 * </code>
 *
 * @author will
 */
public class Metadata extends Model {

    private static final long serialVersionUID = 1L;
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
     * @param pKey
     *         Set the name of this key
     *
     * @return
     */
    public Metadata setKey(String pKey) {
        key = pKey;
        return this;
    }

    /**
     * @return The value associated with this key.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param pValue
     *         Set the vaule.
     *
     * @return
     */
    public Metadata setValue(String pValue) {
        value = pValue;
        return this;
    }

}
