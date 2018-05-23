package com.groupbyinc.api.model;

/**
 * ResultsMetadata
 *
 * @author groupby
 */
public class ResultsMetadata {

  private boolean cached;
  private boolean recordLimitReached;
  private long totalTime;

  /**
   * @return true if the total record count is actually greater than the returned record count in the response, false otherwise.
   * The limit on the amount of records considered by the engine has been reached.
   */
  public boolean isRecordLimitReached() {
    return recordLimitReached;
  }

  public ResultsMetadata setRecordLimitReached(boolean recordLimitReached) {
    this.recordLimitReached = recordLimitReached;
    return this;
  }

  /**
   * @return The total time spent in milliseconds.
   */
  public long getTotalTime() {
    return totalTime;
  }

  public ResultsMetadata setTotalTime(long totalTime) {
    this.totalTime = totalTime;
    return this;
  }

  /**
   *
   * @return true if the query was cached, false otherwise
   */
  public boolean isCached() {
    return cached;
  }

  public ResultsMetadata setCached(boolean cached) {
    this.cached = cached;
    return this;
  }
}
