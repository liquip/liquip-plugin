package io.github.liquip.paper.standalone.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

public class ConfigItem {
    private String key;
    private String material;
    private String displayName;
    private List<String> lore;
    private List<ConfigEnchantment> enchantments;
    private Map<String, JsonNode> features;

    @JsonCreator
    public ConfigItem(@JsonProperty(value = "key", required = true) String key,
                      @JsonProperty(value = "material", required = true) String material,
                      @JsonProperty(value = "displayName", required = true) String displayName,
                      @JsonProperty("lore") List<String> lore,
                      @JsonProperty("enchantments") List<ConfigEnchantment> enchantments,
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

    public List<ConfigEnchantment> getEnchantments() {
        return enchantments;
    }

    public Map<String, JsonNode> getFeatures() {
        return features;
    }
}