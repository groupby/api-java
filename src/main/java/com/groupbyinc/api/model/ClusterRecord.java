package com.groupbyinc.api.model;

/**
 * <code>
 * The Cluster record is a simpler record type than a main record.
 * It contains only a title, URL and snippet of matching text.
 * </code>
 */
public class ClusterRecord {

  private String title;
  private String url;
  private String snippet;

  /**
   * @return The matching set of terms for this record.
   */
  public String getSnippet() {
    return snippet;
  }

  /**
   * @param snippet
   *         Snippet value
   */
  public void setSnippet(String snippet) {
    this.snippet = snippet;
  }

  /**
   * @return The title of this record.
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   *         The title of this record.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return The Unique identifier of this record.
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url
   *         The unique identifier of this record.
   */
  public void setUrl(String url) {
    this.url = url;
  }
}
