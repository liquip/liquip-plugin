package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IngredientStructure(@JsonProperty(value = "key", required = true) String key,
                                  @JsonProperty(value = "c", required = true) String c,
                                  @JsonProperty("count") int count) {
}
