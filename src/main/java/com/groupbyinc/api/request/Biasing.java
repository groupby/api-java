package com.groupbyinc.api.request;

import com.groupbyinc.common.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by groupby on 1/12/16.
 */
public class Biasing {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> bringToTop = new ArrayList<String>();

    public List<String> getBringToTop() {
        return bringToTop;
    }

    public Biasing setBringToTop(List<String> bringToTop) {
        this.bringToTop = bringToTop;
        return this;
    }
}