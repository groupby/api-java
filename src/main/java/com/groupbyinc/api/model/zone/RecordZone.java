package com.groupbyinc.api.model.zone;

import com.groupbyinc.api.model.Record;
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
public class RecordZone extends Zone<RecordZone> {

  private String query;
  private List<Record> records;

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
  public RecordZone setQuery(String query) {
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
  public List<Record> getRecords() {
    return records;
  }

  /**
   * @param records Set the records
   * @return
   */
  public RecordZone setRecords(List<Record> records) {
    this.records = records;
    return this;
  }
}
