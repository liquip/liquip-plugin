package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

public class ItemStructure {
    private final String key;
    private final String material;
    private final String displayName;
    private final List<String> lore;
    private final List<RecipeStructure> recipes;
    private final Map<String, JsonNode> features;

    @JsonCreator
    public ItemStructure(@JsonProperty(value = "key", required = true) String key,
        @JsonProperty(value = "material", required = true) String material,
        @JsonProperty(value = "displayName", required = true) String displayName, @JsonProperty("lore") List<String> lore,
        @JsonProperty("recipes") List<RecipeStructure> recipes, @JsonProperty("features") Map<String, JsonNode> features) {
        this.key = key;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.recipes = recipes;
        this.features = features;
    }

    public String getKey() {
        return this.key;
    }

    public String getMaterial() {
        return this.material;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public List<RecipeStructure> getRecipes() {
        return this.recipes;
    }

    public Map<String, JsonNode> getFeatures() {
        return this.features;
    }
}