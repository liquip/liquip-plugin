package com.github.sqyyy.liquip.core.items.impl;

import com.github.sqyyy.liquip.core.items.Modifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class AttributeModifierModifier implements Modifier {
    private final Attribute attribute;
    private final AttributeModifier modifier;

    public AttributeModifierModifier(Attribute attribute, AttributeModifier modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    @Override
    public void apply(@NotNull ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addAttributeModifier(attribute, modifier);
        itemStack.setItemMeta(itemMeta);
    }
}
