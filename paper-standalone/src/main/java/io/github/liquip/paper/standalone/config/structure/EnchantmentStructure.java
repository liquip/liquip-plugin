package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnchantmentStructure {
    private final int level;
    private final String id;

    @JsonCreator
    public EnchantmentStructure(@JsonProperty(value = "level", required = true) int level,
        @JsonProperty(value = "id", required = true) String id) {
        this.level = level;
        this.id = id;
    }

    public int getLevel() {
        return this.level;
    }

    public String getId() {
        return this.id;
    }
}
