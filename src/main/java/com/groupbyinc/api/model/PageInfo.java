package com.groupbyinc.api.model;

/**
 * <code>
 * Holds information about this page of data.
 * </code>
 */
public class PageInfo {

  private int recordStart;
  private int recordEnd;

  /**
   * @return The record offset for this search and navigation state.
   */
  public int getRecordStart() {
    return recordStart;
  }

  /**
   * @param recordStart
   *         Set the record offset.
   *
   * @return
   */
  public PageInfo setRecordStart(int recordStart) {
    this.recordStart = recordStart;
    return this;
  }

  /**
   * @return The index of the last record in this page of results.
   */
  public int getRecordEnd() {
    return recordEnd;
  }

  /**
   * @param recordEnd
   *         Set the last record index.
   *
   * @return
   */
  public PageInfo setRecordEnd(int recordEnd) {
    this.recordEnd = recordEnd;
    return this;
  }
}
