package com.github.sqyyy.liquip.core.features;

import com.github.sqyyy.liquip.core.items.Feature;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MinecraftFeatures {
    private MinecraftFeatures() {
    }

    public static class Unbreakable implements Feature {
        @Override
        public void apply(ItemStack itemStack) {
            final var itemMeta = itemStack.getItemMeta();
            itemMeta.setUnbreakable(true);
            itemStack.setItemMeta(itemMeta);
        }
    }

    public static class HideEnchantments implements Feature {
        @Override
        public void apply(ItemStack itemStack) {
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    public static class HideUnbreakable implements Feature {
        @Override
        public void apply(ItemStack itemStack) {
            itemStack.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
    }
}
