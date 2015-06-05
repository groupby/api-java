package com.groupbyinc.api.model;

/**
 * <code>
 * The GSA supports the concept of key matches which links a search term to a URL.
 * The resulting object represents the response from the GSA.
 * </code>
 *
 * @author will
 */
public class KeyMatch {
    private String title;
    private String link;

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public KeyMatch() {
        // default constructor
    }

    /**
     * @return The title of the key match
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *         The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The URL associated with the term that triggered this match.
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link
     *         Set the link.
     */
    public void setLink(String link) {
        this.link = link;
    }
}
