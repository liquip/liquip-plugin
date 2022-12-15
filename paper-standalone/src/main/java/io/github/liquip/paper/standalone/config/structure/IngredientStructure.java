package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class IngredientStructure {
    private final String key;
    private final String c;
    private int count = 0;

    @JsonCreator
    public IngredientStructure(@JsonProperty(value = "key", required = true) String key, @JsonProperty("c") String c) {
        this.key = key;
        this.c = c;
    }

    public String getC() {
        return this.c;
    }

    public String getKey() {
        return this.key;
    }

    public int getCount() {
        return this.count;
    }

    @JsonSetter("count")
    public void setCount(int count) {
        this.count = count;
    }
}
