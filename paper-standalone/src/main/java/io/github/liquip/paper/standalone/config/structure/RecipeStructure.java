package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class RecipeStructure {
    private final List<String> shape;
    private final List<IngredientStructure> ingredients;
    private int count = 1;

    @JsonCreator
    public RecipeStructure(@JsonProperty(value = "shape", required = true) List<String> shape,
        @JsonProperty(value = "ingredients", required = true) List<IngredientStructure> ingredients) {
        this.shape = shape;
        this.ingredients = ingredients;
    }

    public List<String> getShape() {
        return this.shape;
    }

    public List<IngredientStructure> getIngredients() {
        return this.ingredients;
    }

    public int getCount() {
        return this.count;
    }

    @JsonSetter
    public void setCount(int count) {
        this.count = count;
    }
}
