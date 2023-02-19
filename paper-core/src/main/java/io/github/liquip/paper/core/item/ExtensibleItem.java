package io.github.liquip.paper.core.item;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.TaggedFeature;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ExtensibleItem extends ItemBase {
    public ExtensibleItem(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material,
        @NotNull Component displayName) {
        super(api, key, material, displayName);
    }

    public ExtensibleItem(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material,
        @NotNull Component displayName, @NotNull List<Component> lore) {
        super(api, key, material, displayName, lore);
    }

    public void appendLore(@NotNull List<Component> lines) {
        Objects.requireNonNull(lines);
        for (final Component line : lines) {
            Objects.requireNonNull(line);
        }
        lore.addAll(lines);
    }

    public void enchant(@NotNull Enchantment enchantment, int level) {
        Objects.requireNonNull(enchantment);
        enchantments.put(enchantment, level);
        enchantment.initialize(this, level);
    }

    public void addFeature(@NotNull Feature feature) {
        Objects.requireNonNull(feature);
        features.add(feature);
        feature.initialize(this);
    }

    public <T> boolean addTaggedFeature(@NotNull TaggedFeature<T> feature, @NotNull ConfigElement config) {
        Objects.requireNonNull(feature);
        Objects.requireNonNull(config);
        final T value = feature.initialize(this, config);
        if (value == null) {
            return false;
        }
        taggedFeatures.put(feature, value);
        return true;
    }
}
