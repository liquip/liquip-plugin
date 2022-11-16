package com.github.sqyyy.liquip.core.items.impl;

import com.github.sqyyy.liquip.core.items.Modifier;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

public class LeatherModifier implements Modifier {
    private final Color color;

    public LeatherModifier(Color color) {
        this.color = color;
    }

    @Override
    public void apply(@NotNull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof LeatherArmorMeta leatherMeta) {
            leatherMeta.setColor(this.color);
            itemStack.setItemMeta(leatherMeta);
        }
    }
}
