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
    private final List<EnchantmentStructure> enchantments;
    private final Map<String, JsonNode> features;

    @JsonCreator
    public ItemStructure(@JsonProperty(value = "key", required = true) String key,
                         @JsonProperty(value = "material", required = true) String material,
                         @JsonProperty(value = "displayName", required = true) String displayName,
                         @JsonProperty("lore") List<String> lore,
                         @JsonProperty("enchantments") List<EnchantmentStructure> enchantments,
                         @JsonProperty("features") Map<String, JsonNode> features) {
        this.key = key;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.enchantments = enchantments;
        this.features = features;
    }

    public String getKey() {
        return key;
    }

    public String getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<EnchantmentStructure> getEnchantments() {
        return enchantments;
    }

    public Map<String, JsonNode> getFeatures() {
        return features;
    }
}