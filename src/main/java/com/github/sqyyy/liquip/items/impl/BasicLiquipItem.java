package com.github.sqyyy.liquip.items.impl;

import com.github.sqyyy.liquip.items.Feature;
import com.github.sqyyy.liquip.items.LeveledEnchantment;
import com.github.sqyyy.liquip.items.LiquipItem;
import com.github.sqyyy.liquip.util.Identifier;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

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

    public BasicLiquipItem(Identifier id, Component name, Material material, List<Component> lore,
                           List<LeveledEnchantment> enchantments, List<Feature> features,
                           Multimap<Class<? extends Event>, Consumer<? extends Event>> events) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.enchantments = enchantments;
        this.features = features;
        this.events = events;

        for (final var feature : features) {
            feature.initialize(this);
        }
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public List<Component> getLore() {
        return lore;
    }

    @Override
    public List<LeveledEnchantment> getEnchantments() {
        return enchantments;
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public ItemStack newItem() {
        // TODO - version dependent
        final var itemStack =
                org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack.asCraftCopy(new ItemStack(material));
        final var itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(name);
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        final var handle = itemStack.handle;
        handle.getOrCreateTag().putString("liquip:identifier", id.toString());
        // TODO - version dependent

        for (final var enchantment : enchantments) {
            enchantment.apply(itemStack);
        }
        for (final var feature : features) {
            feature.apply(itemStack);
        }

        return itemStack;
    }

    @Override
    public <T extends Event> void callEvent(Class<T> eventClass, T event) {
        for (final var eventConsumer : events.get(eventClass)) {
            ((Consumer<Event>) eventConsumer).accept(event);
        }
    }

    @Override
    public <T extends Event> void registerEvent(Class<T> eventClass, Consumer<T> eventHandler) {
        events.put(eventClass, eventHandler);
    }
}
