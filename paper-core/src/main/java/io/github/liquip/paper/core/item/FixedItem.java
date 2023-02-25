package io.github.liquip.paper.core.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.liquip.api.Liquip;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Implementation of the {@link Item} interface.
 * <p>
 * This class is meant to be used if the item should not be modifiable after its instantiation.
 * </p>
 */
public class FixedItem extends ItemBase {
    public FixedItem(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material, @NotNull Component displayName,
        @NotNull List<Component> lore, @NotNull Object2IntMap<Enchantment> enchantments, @NotNull List<Feature> features,
        @NotNull Map<TaggedFeature<?>, ConfigElement> taggedFeatures,
        @NotNull Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> eventHandlers) {
        this(api, key, material, displayName, lore, enchantments, features, taggedFeatures, Map.of(), eventHandlers);
    }

    public FixedItem(@NotNull Liquip api, @NotNull NamespacedKey key, @NotNull Material material, @NotNull Component displayName,
        @NotNull List<Component> lore, @NotNull Object2IntMap<Enchantment> enchantments, @NotNull List<Feature> features,
        @NotNull Map<TaggedFeature<?>, ConfigElement> taggedFeatures, @NotNull Map<TaggedFeature<?>, ?> initializedTaggedFeatures,
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
        this.taggedFeatures.putAll(initializedTaggedFeatures);
    }

    public static class Builder {
        private final List<Component> lore;
        private final Object2IntMap<Enchantment> enchantments;
        private final List<Feature> features;
        private final Map<TaggedFeature<?>, ConfigElement> taggedFeatures;
        private final Map<TaggedFeature<?>, Object> initializedTaggedFeatures;
        private final Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> eventHandlers;
        private Liquip api;
        private NamespacedKey key;
        private Material material;
        private Component displayName;

        public Builder() {
            lore = new ArrayList<>(0);
            enchantments = new Object2IntOpenHashMap<>(0);
            features = new ArrayList<>(0);
            taggedFeatures = new HashMap<>(0);
            initializedTaggedFeatures = new HashMap<>(0);
            eventHandlers = HashMultimap.create(0, 1);
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
            lore.add(line);
            return this;
        }

        public Builder lore(@NotNull List<Component> lines) {
            Objects.requireNonNull(lines);
            for (final Component line : lines) {
                Objects.requireNonNull(line);
            }
            lore.addAll(lines);
            return this;
        }

        public Builder enchant(@NotNull Enchantment enchantment, int level) {
            Objects.requireNonNull(enchantment);
            enchantments.put(enchantment, level);
            return this;
        }

        public Builder enchant(@NotNull Object2IntMap<Enchantment> enchants) {
            Objects.requireNonNull(enchants);
            enchants.forEach((k, v) -> {
                Objects.requireNonNull(k);
                Objects.requireNonNull(v);
            });
            enchantments.putAll(enchants);
            return this;
        }

        public Builder feature(@NotNull Feature feature) {
            Objects.requireNonNull(feature);
            features.add(feature);
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
            taggedFeatures.put(taggedFeature, config);
            return this;
        }

        public <T> Builder taggedFeature(@NotNull TaggedFeature<T> taggedFeature, @NotNull T config) {
            Objects.requireNonNull(taggedFeature);
            Objects.requireNonNull(config);
            initializedTaggedFeatures.put(taggedFeature, config);
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

        @Deprecated(forRemoval = true)
        public <T extends Event> Builder event(@NotNull Class<T> eventClass, @NotNull BiConsumer<T, ItemStack> eventHandler) {
            Objects.requireNonNull(eventClass);
            Objects.requireNonNull(eventHandler);
            eventHandlers.put(eventClass, eventHandler);
            return this;
        }

        @Deprecated(forRemoval = true)
        public Builder events() {
            eventHandlers.clear();
            return this;
        }

        @Deprecated(forRemoval = true)
        public Builder events(@NotNull Multimap<Class<Event>, BiConsumer<Event, ItemStack>> eventHandlers) {
            Objects.requireNonNull(eventHandlers);
            eventHandlers.forEach((k, v) -> {
                Objects.requireNonNull(k);
                Objects.requireNonNull(v);
            });
            this.eventHandlers.putAll(eventHandlers);
            return this;
        }

        public FixedItem build() {
            Objects.requireNonNull(api);
            Objects.requireNonNull(key);
            Objects.requireNonNull(material);
            Objects.requireNonNull(displayName);
            Objects.requireNonNull(lore);
            Objects.requireNonNull(enchantments);
            Objects.requireNonNull(features);
            Objects.requireNonNull(taggedFeatures);
            Objects.requireNonNull(initializedTaggedFeatures);
            Objects.requireNonNull(eventHandlers);
            return new FixedItem(api, key, material, displayName, lore, enchantments, features, taggedFeatures,
                initializedTaggedFeatures, eventHandlers);
        }
    }
}
