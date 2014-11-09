package com.groupbyinc.api.model;

import com.groupbyinc.api.parser.Model;

/**
 * <code>
 * The Cluster record is a simpler record type than a main record.
 * It contains only a title, URL and snippet of matching text.
 * </code>
 *
 * @author will
 */
public class ClusterRecord extends Model {
    private static final long serialVersionUID = 1L;
    private String title;
    private String url;
    private String snippet;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public ClusterRecord() {
        // default
    }

    /**
     * @return The matching set of terms for this record.
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * @param pSnippet
     *         Snippet value
     */
    public void setSnippet(String pSnippet) {
        snippet = pSnippet;
    }

    /**
     * @return The title of this record.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param pTitle
     *         The title of this record.
     */
    public void setTitle(String pTitle) {
        title = pTitle;
    }

    /**
     * @return The Unique identifier of this record.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param pUrl
     *         The unique identifier of this record.
     */
    public void setUrl(String pUrl) {
        url = pUrl;
    }

}
