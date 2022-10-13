package com.github.sqyyy.liquip.core.items.impl;

import com.github.sqyyy.liquip.core.items.Modifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AttributeModifiersModifier implements Modifier {
    private final List<Entry> entries;

    public AttributeModifiersModifier(@NotNull List<@NotNull Entry> entries) {
        this.entries = entries;
    }

    @Override
    public void apply(@NotNull ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        for (Entry entry : entries) {
            entry.apply(itemMeta);
        }
        itemStack.setItemMeta(itemMeta);
    }

    public static class Entry {
        private final Attribute attribute;
        private final AttributeModifier modifier;

        public Entry(Attribute attribute, AttributeModifier modifier) {
            this.attribute = attribute;
            this.modifier = modifier;
        }

        public void apply(@NotNull ItemMeta itemMeta) {
            itemMeta.addAttributeModifier(attribute, modifier);
        }
    }
}
