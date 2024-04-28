package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecipeStructure(@JsonProperty(value = "shape", required = true) List<String> shape,
                              @JsonProperty(value = "ingredients", required = true) List<IngredientStructure> ingredients,
                              @JsonProperty("count") int count) {
}
