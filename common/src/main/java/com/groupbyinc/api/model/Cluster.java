package com.groupbyinc.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>
 * A cluster represents a set of documents that are considered closely related based on a search term.
 * </code>
 *
 * @author user
 */
public class Cluster {
    private String term;
    private List<ClusterRecord> records = new ArrayList<ClusterRecord>();

    /**
     * <code>
     * Default constructor
     * </code>
     */
    public Cluster() {
        // default
    }

    /**
     * @return The list of clustered records
     */
    public List<ClusterRecord> getRecords() {
        return records;
    }

    /**
     * <code>
     * Set a list of cluster records
     * </code>
     *
     * @param records
     *         The list of cluster records to set
     *
     * @return
     */
    public Cluster setRecords(List<ClusterRecord> records) {
        this.records = records;
        return this;
    }

    /**
     * @return The term for this cluster
     */
    public String getTerm() {
        return term;
    }

    /**
     * <code>
     * Set the term for this cluster.
     * </code>
     *
     * @param term
     *         The cluster term
     *
     * @return
     */
    public Cluster setTerm(String term) {
        this.term = term;
        return this;
    }
}
