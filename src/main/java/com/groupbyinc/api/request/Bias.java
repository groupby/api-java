package com.groupbyinc.api.request;

import com.groupbyinc.common.jackson.annotation.JsonIgnore;
import com.groupbyinc.common.jackson.annotation.JsonProperty;

public class Bias {
    public enum Strength {
        Absolute_Increase,
        Strong_Increase,
        Medium_Increase,
        Weak_Increase,
        Leave_Unchanged,
        Weak_Decrease,
        Medium_Decrease,
        Strong_Decrease,
        Absolute_Decrease
    }

    private String name;
    private String content;

    @JsonProperty
    private Strength strength;

    public String getName() {
        return name;
    }

    public Bias setName(String name) {
        this.name = name;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Bias setContent(String content) {
        this.content = content;
        return this;
    }

    public Strength getStrength() {
        return strength;
    }

    @JsonIgnore
    public Bias setStrength(Strength strength) {
        this.strength = strength;
        return this;
    }

    public Bias setStrength(String strength) {
        this.strength = Strength.valueOf(strength);
        return this;
    }
}
