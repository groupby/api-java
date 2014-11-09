package com.groupbyinc.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupbyinc.api.parser.Model;

import java.util.List;

/**
 * <code>
 * Zone represents an area on the page that merchandisers have control over.  They can set the content
 * of a zone from the command center as part of a rule instantiation.  When a rule is triggered the zones are filled
 * in with
 * the pertinent content.
 * Zones support the following content types:
 * <p/>
 * Content, Record, Banner, Rich_Content
 * <p/>
 * Zones contain the following properties:
 * <p/>
 * - id: an MD5 of the zone name
 * - name: the zone name
 * - type: the content type
 * - content: the content type is not Record, a string of text will be stored here.
 * - records: if the content type is Record, a list of zero or more
 * records matching a search.
 * </code>
 *
 * @author will
 */
public class Zone<R extends RecordBase> extends Model {
    private static final long serialVersionUID = 1L;

    public enum Type {
        Content, Record, Banner, Rich_Content
    }

    @JsonProperty("_id")
    private String id;
    private String name;
    private Type type;
    private String content;
    private List<R> records;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public Zone() {
        // default constructor
    }

    /**
     * <code>
     * A maximum of ten records will be returned for each record zone.
     * </code>
     *
     * @return If this zone is a record zone, a list of records returned from
     * the bridge.
     */
    public List<R> getRecords() {
        return records;
    }

    /**
     * @param pRecords
     *         Set the records
     *
     * @return
     */
    public Zone setRecords(List<R> pRecords) {
        records = pRecords;
        return this;
    }

    /**
     * @return ID is a MD5 hash of the name.
     */
    public String getId() {
        return id;
    }

    /**
     * @param pId
     *         Set the ID.
     *
     * @return
     */
    public Zone setId(String pId) {
        id = pId;
        return this;
    }

    /**
     * @return The name of the zone.
     */
    public String getName() {
        return name;
    }

    /**
     * @param pName
     *         Set the name
     *
     * @return
     */
    public Zone setName(String pName) {
        name = pName;
        return this;
    }

    /**
     * <code>
     * Zones support the following content types:
     * <p/>
     * Content, Record, Banner, Rich_Content
     * <p/>
     * </code>
     *
     * @return The type of zone.
     */
    public Type getType() {
        return type;
    }

    /**
     * @param pType
     *         Set the type.
     *
     * @return
     */
    public Zone setType(Type pType) {
        type = pType;
        return this;
    }

    /**
     * @return If this zone is not a Record zone this will represent the value
     * set by the merchandiser.
     */
    public String getContent() {
        return content;
    }

    /**
     * @param pContent
     *         Set the content
     *
     * @return
     */
    public Zone setContent(String pContent) {
        content = pContent;
        return this;
    }

}
