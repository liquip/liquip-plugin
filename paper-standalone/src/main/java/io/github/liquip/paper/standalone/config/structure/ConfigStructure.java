package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ConfigStructure {
    private final List<String> items;

    @JsonCreator
    public ConfigStructure(@JsonProperty(value = "items", required = true) List<String> items) {
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }
}
