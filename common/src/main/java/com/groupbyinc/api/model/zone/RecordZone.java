package com.groupbyinc.api.model.zone;

import com.groupbyinc.api.model.AbstractRecord;
import com.groupbyinc.api.model.Zone;

import java.util.List;

/**
 * <code>
 * Zone represents an area on the page that merchandisers have control over.  They can set the content
 * of a zone from the command center as part of a rule instantiation.  When a rule is triggered the zones are filled
 * in with the pertinent content.
 *
 * Zones contain the following properties:
 *
 * - `id`: an MD5 of the zone name
 * - `name`: the zone name
 * - `type`: `Records`: A list of zero or more records.
 * </code>
 * @author will
 */
public class RecordZone<D extends AbstractRecord<D>> extends Zone<RecordZone<D>> {
    private String query;
    private List<D> records;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public RecordZone() {
        // default constructor
    }

    /**
     * @internal
     * @return
     */
    @Override
    public Type getType() {
        return Type.Record;
    }

    /**
     * <code>
     * The query that was fired for this zone.
     * </code>
     *
     * @return The query that was fired for this zone.
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query Set the query
     * @return
     */
    public RecordZone<D> setQuery(String query) {
        this.query = query;
        return this;
    }

    /**
     * <code>
     * A maximum of ten records will be returned for each record zone.
     * </code>
     *
     * @return A list of records returned from the search service.
     */
    public List<D> getRecords() {
        return records;
    }

    /**
     * @param records Set the records
     * @return
     */
    public RecordZone<D> setRecords(List<D> records) {
        this.records = records;
        return this;
    }
}
