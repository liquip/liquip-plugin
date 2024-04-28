package io.github.liquip.paper.core.item;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the {@link Item} interface.
 * <p>
 * This class is meant to be used if the item should not be modifiable after its instantiation.
 * </p>
 */
public class FixedItem extends ItemBase {
    public FixedItem(
        @NotNull Liquip api,
        @NotNull NamespacedKey key,
        @NotNull Material material,
        @NotNull Component displayName,
        @NotNull List<Component> lore,
        @NotNull List<Feature> features,
        @NotNull Map<TaggedFeature<?>, ConfigElement> taggedFeatures
    ) {
        this(api, key, material, displayName, lore, features, taggedFeatures, Map.of());
    }

    public FixedItem(
        @NotNull Liquip api,
        @NotNull NamespacedKey key,
        @NotNull Material material,
        @NotNull Component displayName,
        @NotNull List<Component> lore,
        @NotNull List<Feature> features,
        @NotNull Map<TaggedFeature<?>, ConfigElement> taggedFeatures,
        @NotNull Map<TaggedFeature<?>, ?> initializedTaggedFeatures
    ) {
        super(api, key, material, displayName, lore);
        for (final Feature feature : features) {
            this.features.add(feature);
            feature.initialize(this);
        }
        for (final Map.Entry<TaggedFeature<?>, ConfigElement> entry : taggedFeatures.entrySet()) {
            final Object res = entry.getKey().initialize(this, entry.getValue());
            if (res == null) {
                this.api
                    .getSystemLogger()
                    .warn("Tagged feature '{}' could not be applied to item '{}'", entry.getKey().getKey(), this.key);
                continue;
            }
            this.taggedFeatures.put(entry.getKey(), res);
        }
        this.taggedFeatures.putAll(initializedTaggedFeatures);
    }

    public static class Builder {
        private final List<Component> lore;
        private final List<Feature> features;
        private final Map<TaggedFeature<?>, ConfigElement> taggedFeatures;
        private final Map<TaggedFeature<?>, Object> initializedTaggedFeatures;
        private Liquip api;
        private NamespacedKey key;
        private Material material;
        private Component displayName;

        public Builder() {
            this.lore = new ArrayList<>(0);
            this.features = new ArrayList<>(0);
            this.taggedFeatures = new HashMap<>(0);
            this.initializedTaggedFeatures = new HashMap<>(0);
        }

        public Builder(@NotNull Liquip api) {
            this();
            this.api = api;
        }

        public Builder api(@NotNull Liquip api) {
            Objects.requireNonNull(api);
            this.api = api;
            return this;
        }

        public Builder key(@NotNull NamespacedKey key) {
            Objects.requireNonNull(key);
            this.key = key;
            return this;
        }

        public Builder material(@NotNull Material material) {
            Objects.requireNonNull(material);
            this.material = material;
            return this;
        }

        public Builder name(@NotNull Component displayName) {
            Objects.requireNonNull(displayName);
            this.displayName = displayName;
            return this;
        }

        public Builder lore(@NotNull Component line) {
            Objects.requireNonNull(line);
            this.lore.add(line);
            return this;
        }

        public Builder lore(@NotNull List<Component> lines) {
            Objects.requireNonNull(lines);
            for (final Component line : lines) {
                Objects.requireNonNull(line);
            }
            this.lore.addAll(lines);
            return this;
        }

        public Builder feature(@NotNull Feature feature) {
            Objects.requireNonNull(feature);
            this.features.add(feature);
            return this;
        }

        public Builder features(@NotNull List<Feature> features) {
            Objects.requireNonNull(features);
            for (final Feature feature : features) {
                Objects.requireNonNull(feature);
            }
            this.features.addAll(features);
            return this;
        }

        public <T> Builder taggedFeature(@NotNull TaggedFeature<T> taggedFeature, @NotNull ConfigElement config) {
            Objects.requireNonNull(taggedFeature);
            Objects.requireNonNull(config);
            this.taggedFeatures.put(taggedFeature, config);
            return this;
        }

        public <T> Builder taggedFeature(@NotNull TaggedFeature<T> taggedFeature, @NotNull T config) {
            Objects.requireNonNull(taggedFeature);
            Objects.requireNonNull(config);
            this.initializedTaggedFeatures.put(taggedFeature, config);
            return this;
        }

        public Builder taggedFeatures(@NotNull Map<TaggedFeature<?>, ConfigElement> taggedFeatures) {
            Objects.requireNonNull(taggedFeatures);
            for (final Map.Entry<TaggedFeature<?>, ConfigElement> entry : taggedFeatures.entrySet()) {
                Objects.requireNonNull(entry.getKey());
                Objects.requireNonNull(entry.getValue());
            }
            for (final Map.Entry<TaggedFeature<?>, ConfigElement> entry : taggedFeatures.entrySet()) {
                Objects.requireNonNull(entry.getKey());
                Objects.requireNonNull(entry.getValue());
            }
            this.taggedFeatures.putAll(taggedFeatures);
            return this;
        }

        public FixedItem build() {
            Objects.requireNonNull(this.api);
            Objects.requireNonNull(this.key);
            Objects.requireNonNull(this.material);
            Objects.requireNonNull(this.displayName);
            Objects.requireNonNull(this.lore);
            Objects.requireNonNull(this.features);
            Objects.requireNonNull(this.taggedFeatures);
            Objects.requireNonNull(this.initializedTaggedFeatures);
            return new FixedItem(this.api, this.key, this.material, this.displayName, this.lore, this.features,
                this.taggedFeatures, this.initializedTaggedFeatures);
        }
    }
}
