package io.github.liquip.paper.core.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.liquip.api.Liquip;
import io.github.liquip.api.config.ConfigElement;
import io.github.liquip.api.item.Enchantment;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ItemImpl implements Item {
    private final Liquip api;
    private final Key key;
    private final Material material;
    private final Component displayName;
    private final List<Component> lore;
    private final Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> eventHandlers;
    private final Object2IntMap<Enchantment> enchantments;
    private final List<Feature> features;
    private final Map<TaggedFeature<?>, Object> taggedFeatures;

    public ItemImpl(@NonNull Liquip api, @NonNull Key key, @NonNull Material material, @NonNull Component displayName,
        @NonNull List<Component> lore, @NonNull Object2IntMap<Enchantment> enchantments, @NonNull List<Feature> features,
        @NonNull Map<TaggedFeature<?>, ConfigElement> taggedFeatures,
        @NonNull Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> eventHandlers) {
        this.api = api;
        this.key = key;
        this.material = material;
        this.displayName = displayName;
        this.lore = new ArrayList<>(lore.size());
        this.lore.addAll(lore);
        this.eventHandlers = ArrayListMultimap.create();
        this.eventHandlers.putAll(eventHandlers);
        this.enchantments = new Object2IntOpenHashMap<>(enchantments.size());
        for (final Object2IntMap.Entry<Enchantment> entry : enchantments.object2IntEntrySet()) {
            this.enchantments.put(entry.getKey(), entry.getIntValue());
            entry.getKey().initialize(this, entry.getIntValue());
        }
        this.features = new ArrayList<>(features.size());
        for (final Feature feature : features) {
            this.features.add(feature);
            feature.initialize(this);
        }
        this.taggedFeatures = new LinkedHashMap<>(taggedFeatures.size());
        for (final Map.Entry<TaggedFeature<?>, ConfigElement> entry : taggedFeatures.entrySet()) {
            final Object res = entry.getKey().initialize(this, entry.getValue());
            if (res == null) {
                this.api.getSystemLogger()
                    .warn("Tagged feature '{}' could not be applied to item '{}'", entry.getKey().getKey(), this.key);
                continue;
            }
            this.taggedFeatures.put(entry.getKey(), res);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void applyToTaggedFeature(TaggedFeature<T> key, ItemStack itemStack, Object value) {
        key.apply(this, itemStack, (T) value);
    }

    @Override
    public @NotNull Key key() {
        return this.key;
    }

    @Override
    public @NonNull ItemStack newItemStack() {
        final ItemStack itemStack = new ItemStack(this.material);
        itemStack.editMeta(meta -> {
            meta.displayName(this.displayName);
            meta.lore(this.lore);
        });
        this.enchantments.forEach((enchantment, level) -> enchantment.apply(this, itemStack, level));
        this.features.forEach(feature -> feature.apply(this, itemStack));
        this.taggedFeatures.forEach((key, value) -> this.applyToTaggedFeature(key, itemStack, value));
        this.api.setKeyForItemStack(itemStack, this.key);
        return itemStack;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Event> void callEvent(@NonNull Class<T> eventClass, @NonNull T event, @NonNull ItemStack itemStack) {
        final Collection<BiConsumer<? extends Event, ItemStack>> handlers = this.eventHandlers.get(eventClass);
        for (BiConsumer<? extends Event, ItemStack> handler : handlers) {
            ((BiConsumer<Event, ItemStack>) handler).accept(event, itemStack);
        }
    }

    @Override
    public <T extends Event> void registerEvent(@NonNull Class<T> eventClass, @NonNull BiConsumer<T, ItemStack> eventHandler) {
        this.eventHandlers.put(eventClass, eventHandler);
    }
}
