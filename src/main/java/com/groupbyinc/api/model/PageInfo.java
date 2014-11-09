package com.groupbyinc.api.model;

import com.groupbyinc.api.parser.Model;

/**
 * <code>
 * Holds information about this page of data.
 * </code>
 *
 * @author will
 */
public class PageInfo extends Model {
    private static final long serialVersionUID = 1L;
    private int recordStart;
    private int recordEnd;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public PageInfo() {
        // default constructor
    }

    /**
     * @return The record offset for this search and navigation state.
     */
    public int getRecordStart() {
        return recordStart;
    }

    /**
     * @param pRecordStart
     *         Set the record offset.
     *
     * @return
     */
    public PageInfo setRecordStart(int pRecordStart) {
        recordStart = pRecordStart;
        return this;
    }

    /**
     * @return The index of the last record in this page of results.
     */
    public int getRecordEnd() {
        return recordEnd;
    }

    /**
     * @param pRecordEnd
     *         Set the last record index.
     *
     * @return
     */
    public PageInfo setRecordEnd(int pRecordEnd) {
        recordEnd = pRecordEnd;
        return this;
    }

}
