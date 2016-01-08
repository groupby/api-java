package com.groupbyinc.api.request;

import com.groupbyinc.common.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author osman
 */
public class Biasing {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> bringToTop = new ArrayList<String>();

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean augmentBiases = false;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Bias> biases = new ArrayList<Bias>();

    public List<String> getBringToTop() {
        return bringToTop;
    }

    public Biasing setBringToTop(List<String> bringToTop) {
        this.bringToTop = bringToTop;
        return this;
    }
    public boolean isAugmentBiases() {
        return augmentBiases;
    }

    public Biasing setAugmentBiases(boolean augmentBiases) {
        this.augmentBiases = augmentBiases;
        return this;
    }

    public List<Bias> getBiases() {
        return biases;
    }

    public Biasing setBiases(List<Bias> biases) {
        this.biases = biases;
        return this;
    }
}
