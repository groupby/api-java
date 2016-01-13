package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>
 * A biasing profile defined at query time.
 + </code>
 * Created by groupby on 1/12/16.
 */
public class Biasing {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> bringToTop = new ArrayList<String>();

    /**
     * @return The list of product IDs
     */
    public List<String> getBringToTop() {
        return bringToTop;
    }

    /**
     * <code>
     * A list of product IDs to bring to the top of the result set. This list
     * will ensure that the products are included in the result set and appear in the order
     * defined.
     * </code>
     *
     * @param bringToTop
     *      The list of productIds.
     * @return
     */
    public Biasing setBringToTop(List<String> bringToTop) {
        this.bringToTop = bringToTop;
        return this;
    }
}