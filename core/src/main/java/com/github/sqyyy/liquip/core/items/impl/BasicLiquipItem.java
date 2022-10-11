package com.github.sqyyy.liquip.core.items.impl;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.items.LeveledEnchantment;
import com.github.sqyyy.liquip.core.items.LiquipItem;
import com.github.sqyyy.liquip.core.util.Identifier;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class BasicLiquipItem implements LiquipItem {
    private final Identifier id;
    private final Component name;
    private final Material material;
    private final List<Component> lore;
    private final List<LeveledEnchantment> enchantments;
    private final List<Feature> features;
    private final Multimap<Class<? extends Event>, Consumer<? extends Event>> events;

    public BasicLiquipItem(@NotNull Identifier id, @NotNull Component name, @NotNull Material material,
                           @NotNull List<@NotNull Component> lore,
                           @NotNull List<@NotNull LeveledEnchantment> enchantments,
                           @NotNull List<@NotNull Feature> features,
                           @NotNull Multimap<@NotNull Class<? extends Event>, @NotNull Consumer<? extends Event>> events) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.enchantments = enchantments;
        this.features = features;
        this.events = events;
        for (final Feature feature : features) {
            feature.initialize(this);
        }
    }

    @Override
    public @NotNull Identifier getId() {
        return id;
    }

    @Override
    public @NotNull Component getName() {
        return name;
    }

    @Override
    public @NotNull Material getMaterial() {
        return material;
    }

    @Override
    public @NotNull List<@NotNull Component> getLore() {
        return lore;
    }

    @Override
    public @NotNull List<@NotNull LeveledEnchantment> getEnchantments() {
        return enchantments;
    }

    @Override
    public @NotNull List<@NotNull Feature> getFeatures() {
        return features;
    }

    @Override
    public @NotNull ItemStack newItem() {
        final ItemStack itemStack = new ItemStack(material);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(name);
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        for (final LeveledEnchantment enchantment : enchantments) {
            enchantment.apply(itemStack);
        }
        for (final Feature feature : features) {
            feature.apply(itemStack);
        }
        return LiquipItem.setIdentifier(itemStack, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void callEvent(@NotNull Class<T> eventClass, @NotNull T event) {
        for (final Consumer<? extends Event> eventConsumer : events.get(eventClass)) {
            ((Consumer<Event>) eventConsumer).accept(event);
        }
    }

    @Override
    public <T extends Event> void registerEvent(@NotNull Class<T> eventClass,
                                                @NotNull Consumer<@NotNull T> eventHandler) {
        events.put(eventClass, eventHandler);
    }
}
