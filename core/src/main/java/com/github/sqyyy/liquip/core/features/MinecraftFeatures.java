package com.github.sqyyy.liquip.core.features;

import com.github.sqyyy.liquip.core.items.Feature;
import com.github.sqyyy.liquip.core.util.Identifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class MinecraftFeatures {
    public static final String NAMESPACE = "minecraft";
    public static final Identifier UNBREAKABLE = new Identifier(NAMESPACE, "unbreakable");
    public static final Identifier HIDE_ENCHANTMENTS = new Identifier(NAMESPACE, "hide_enchantments");
    public static final Identifier HIDE_UNBREAKABLE = new Identifier(NAMESPACE, "hide_unbreakable");

    private MinecraftFeatures() {
    }

    public static class Unbreakable implements Feature {
        @Override
        public void apply(@NotNull ItemStack itemStack) {
            final ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setUnbreakable(true);
            itemStack.setItemMeta(itemMeta);
        }
    }

    public static class HideEnchantments implements Feature {
        @Override
        public void apply(@NotNull ItemStack itemStack) {
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    public static class HideUnbreakable implements Feature {
        @Override
        public void apply(@NotNull ItemStack itemStack) {
            itemStack.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
    }
}
