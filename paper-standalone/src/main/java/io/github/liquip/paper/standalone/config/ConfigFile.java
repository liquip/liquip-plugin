package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ConfigFile {
    private List<String> items;

    @JsonCreator
    public ConfigFile(@JsonProperty(value = "items", required = true) List<String> items) {
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }
}
