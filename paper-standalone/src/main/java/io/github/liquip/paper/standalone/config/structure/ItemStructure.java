package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

public record ItemStructure(@JsonProperty(value = "key", required = true) String key,
                            @JsonProperty(value = "material", required = true) String material,
                            @JsonProperty(value = "displayName", required = true) String displayName,
                            @JsonProperty("lore") List<String> lore,
                            @JsonProperty("recipes") List<RecipeStructure> recipes,
                            @JsonProperty("features") Map<String, JsonNode> features) {
}