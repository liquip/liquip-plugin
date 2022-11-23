package io.github.liquip.paper.core.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.liquip.api.Liquip;
import io.github.liquip.api.LiquipProvider;
import io.github.liquip.api.item.Feature;
import io.github.liquip.api.item.Item;
import io.github.liquip.api.item.TaggedFeature;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class ItemImpl implements Item {
    private final Liquip API;
    private final Key key;
    private final Material material;
    private final Component displayName;
    private final List<Component> lore;
    private final List<Feature> features;
    private final Map<TaggedFeature<?>, Object> taggedFeatures;
    private final Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>>
        eventHandlers;

    public ItemImpl(@NonNull Key key, @NonNull Material material, @NonNull Component displayName,
        @NonNull List<Component> lore) {
        this(key, material, displayName, lore, List.of(), Map.of(), ImmutableMultimap.of());
    }

    public ItemImpl(@NonNull Liquip api, @NonNull Key key, @NonNull Material material,
        @NonNull Component displayName, @NonNull List<Component> lore) {
        this(api, key, material, displayName, lore, List.of(), Map.of(), ImmutableMultimap.of());
    }

    public ItemImpl(@NonNull Key key, @NonNull Material material, @NonNull Component displayName,
        @NonNull List<Component> lore, @NonNull List<Feature> features,
        @NonNull Map<TaggedFeature<?>, Object> taggedFeatures,
        @NonNull Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> eventHandlers) {
        this(LiquipProvider.get(), key, material, displayName, lore, features, taggedFeatures,
            eventHandlers);
    }

    public ItemImpl(@NonNull Liquip api, @NonNull Key key, @NonNull Material material,
        @NonNull Component displayName, @NonNull List<Component> lore,
        @NonNull List<Feature> features, @NonNull Map<TaggedFeature<?>, Object> taggedFeatures,
        @NonNull Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> eventHandlers) {
        this.API = api;
        this.key = key;
        this.material = material;
        this.displayName = displayName;
        this.lore = new ArrayList<>();
        this.lore.addAll(lore);
        this.features = new ArrayList<>();
        this.features.addAll(features);
        this.taggedFeatures = new LinkedHashMap<>();
        this.taggedFeatures.putAll(taggedFeatures);
        this.eventHandlers = ArrayListMultimap.create();
        this.eventHandlers.putAll(eventHandlers);
    }

    @SuppressWarnings("unchecked")
    private <T> void applyToTaggedFeature(TaggedFeature<T> key, ItemStack itemStack, Object value) {
        key.apply(this, itemStack, (T) value);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NonNull ItemStack newItemStack() {
        final ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(meta -> {
            meta.displayName(this.displayName);
            meta.lore(this.lore);
            API.setKeyForItemStack(itemStack, this.key);
        });
        features.forEach(feature -> feature.apply(this, itemStack));
        taggedFeatures.forEach((key, value) -> this.applyToTaggedFeature(key, itemStack, value));
        return itemStack;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void callEvent(@NonNull Class<T> eventClass, @NonNull T event,
        @NonNull ItemStack itemStack) {
        final Collection<BiConsumer<? extends Event, ItemStack>> handlers =
            eventHandlers.get(eventClass);
        for (BiConsumer<? extends Event, ItemStack> handler : handlers) {
            ((BiConsumer<Event, ItemStack>) handler).accept(event, itemStack);
        }
    }

    @Override
    public <T extends Event> void registerEvent(@NonNull Class<T> eventClass,
        @NonNull BiConsumer<T, ItemStack> eventHandler) {
        eventHandlers.put(eventClass, eventHandler);
    }
}
