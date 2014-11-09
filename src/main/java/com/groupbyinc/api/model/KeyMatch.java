package com.groupbyinc.api.model;

import com.groupbyinc.api.parser.Model;

/**
 * <code>
 * The GSA supports the concept of key matches which links a search term to a URL.
 * The resulting object represents the response from the GSA.
 * </code>
 *
 * @author will
 */
public class KeyMatch extends Model {
    private static final long serialVersionUID = 1L;

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
     * @param pTitle
     *         The title to set.
     */
    public void setTitle(String pTitle) {
        title = pTitle;
    }

    /**
     * @return The URL associated with the term that triggered this match.
     */
    public String getLink() {
        return link;
    }

    /**
     * @param pLink
     *         Set the link.
     */
    public void setLink(String pLink) {
        link = pLink;
    }

}
