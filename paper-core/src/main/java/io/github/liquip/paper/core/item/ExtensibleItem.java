package io.github.liquip.paper.core.item;

import com.google.common.collect.Multimap;
import io.github.liquip.api.Liquip;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Implementation of the {@link Item} interface.
 * <p>
 * This class is meant to be used if the item should be modifiable after its instantiation.
 * </p>
 */
public class ExtensibleItem extends ItemBase {
    public ExtensibleItem(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material,
        @NotNull Component displayName) {
        super(api, key, material, displayName);
    }

    public ExtensibleItem(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material,
        @NotNull Component displayName, @NotNull List<Component> lore) {
        super(api, key, material, displayName, lore);
    }

    public @NotNull List<Component> getLore() {
        return lore;
    }

    public @NotNull Object2IntMap<Enchantment> getEnchants() {
        return enchantments;
    }

    public @NotNull List<Feature> getFeatures() {
        return features;
    }

    public @NotNull Map<TaggedFeature<?>, Object> getTaggedFeatures() {
        return taggedFeatures;
    }

    @Deprecated(forRemoval = true)
    public @NotNull Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> getEventHandlers() {
        return eventHandlers;
    }
}
