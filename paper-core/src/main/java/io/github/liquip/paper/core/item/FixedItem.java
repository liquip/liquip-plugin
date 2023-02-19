package io.github.liquip.paper.core.item;

import com.google.common.collect.Multimap;
import io.github.liquip.api.Liquip;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
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

public class FixedItem extends ItemBase {

    public FixedItem(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material, @NotNull Component displayName,
        @NotNull List<Component> lore, @NotNull Object2IntMap<Enchantment> enchantments, @NotNull List<Feature> features,
        @NotNull Map<TaggedFeature<?>, ConfigElement> taggedFeatures,
        @NotNull Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> eventHandlers) {
        super(api, key, material, displayName, lore);
        for (final Object2IntMap.Entry<Enchantment> entry : enchantments.object2IntEntrySet()) {
            this.enchantments.put(entry.getKey(), entry.getIntValue());
            entry.getKey()
                .initialize(this, entry.getIntValue());
        }
        for (final Feature feature : features) {
            this.features.add(feature);
            feature.initialize(this);
        }
        for (final Map.Entry<TaggedFeature<?>, ConfigElement> entry : taggedFeatures.entrySet()) {
            final Object res = entry.getKey()
                .initialize(this, entry.getValue());
            if (res == null) {
                this.api.getSystemLogger()
                    .warn("Tagged feature '{}' could not be applied to item '{}'", entry.getKey()
                        .getKey(), this.key);
                continue;
            }
            this.taggedFeatures.put(entry.getKey(), res);
        }
    }
}
