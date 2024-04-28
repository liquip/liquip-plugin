package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ConfigStructure(@JsonProperty(value = "items", required = true) List<String> items,
                              @JsonProperty("craftingTable") String craftingTable) {
}
