package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigEnchantment {
    private int level;
    private String id;

    @JsonCreator
    public ConfigEnchantment(@JsonProperty(value = "level", required = true) int level,
                             @JsonProperty(value = "id", required = true) String id) {
        this.level = level;
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }
}
