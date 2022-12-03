package io.github.liquip.paper.core.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
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

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class ItemImpl implements Item {
    private final Liquip api;
    private final Key key;
    private final Material material;
    private final Component displayName;
    private final List<Component> lore;
    private final Object2IntMap<Enchantment> enchantments;
    private final List<Feature> features;
    private final Map<TaggedFeature<?>, Object> taggedFeatures;
    private final Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>>
        eventHandlers;

    public ItemImpl(@NonNull Liquip api, @NonNull Key key, @NonNull Material material,
                    @NonNull Component displayName, @NonNull List<Component> lore,
                    @NonNull Object2IntMap<Enchantment> enchantments,
                    @NonNull List<Feature> features,
                    @NonNull Map<TaggedFeature<?>, ConfigElement> taggedFeatures,
                    @NonNull Multimap<Class<? extends Event>, BiConsumer<? extends Event, ItemStack>> eventHandlers) {
        this.api = api;
        this.key = key;
        this.material = material;
        this.displayName = displayName;
        this.lore = new ArrayList<>(lore.size());
        this.lore.addAll(lore);
        this.enchantments = new Object2IntOpenHashMap<>(enchantments.size());
        this.enchantments.putAll(enchantments);
        this.features = new ArrayList<>(features.size());
        this.features.addAll(features);
        this.taggedFeatures = new LinkedHashMap<>(taggedFeatures.size());
        this.taggedFeatures.putAll(Maps.transformEntries(taggedFeatures,
            (taggedFeature, config) -> taggedFeature.initialize(this, config)));
        this.eventHandlers = ArrayListMultimap.create();
        this.eventHandlers.putAll(eventHandlers);
    }

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
            api.setKeyForItemStack(itemStack, this.key);
        });
        enchantments.forEach((enchantment, level) -> enchantment.apply(this, itemStack, level));
        features.forEach(feature -> feature.apply(this, itemStack));
        taggedFeatures.forEach((key, value) -> this.applyToTaggedFeature(key, itemStack, value));
        return itemStack;
    }

    @Override
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
