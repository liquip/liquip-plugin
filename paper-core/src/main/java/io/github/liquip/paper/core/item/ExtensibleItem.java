package io.github.liquip.paper.core.item;

import io.github.liquip.api.Liquip;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link Item} interface.
 * <p>
 * This class is meant to be used if the item should be modifiable after its instantiation.
 * </p>
 */
public class ExtensibleItem extends ItemBase {
    public ExtensibleItem(
        @NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material, @NotNull Component displayName
    ) {
        super(api, key, material, displayName);
    }

    public ExtensibleItem(
        @NotNull Liquip api,
        @NotNull NamespacedKey key,
        @NotNull Material material,
        @NotNull Component displayName,
        @NotNull List<Component> lore
    ) {
        super(api, key, material, displayName, lore);
    }

    public @NotNull List<Component> getLore() {
        return this.lore;
    }

    public @NotNull List<Feature> getFeatures() {
        return this.features;
    }

    public @NotNull Map<TaggedFeature<?>, Object> getTaggedFeatures() {
        return this.taggedFeatures;
    }
}
